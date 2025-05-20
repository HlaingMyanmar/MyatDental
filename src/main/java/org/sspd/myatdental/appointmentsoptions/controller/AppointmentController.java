package org.sspd.myatdental.appointmentsoptions.controller;

import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.dentistsoptions.model.Dentist;
import org.sspd.myatdental.dentistsoptions.service.DentistServices;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Controller
public class AppointmentController implements Initializable {

    @FXML
    private TextArea addresstxt;

    @FXML
    private TextField agetxt;

    @FXML
    private DatePicker appdatebox;

    @FXML
    private TextArea appnotetxt;

    @FXML
    private TextArea apppurposetxt;

    @FXML
    private ComboBox<String> appstatusbox;

    @FXML
    private TextField apptimetxt;

    @FXML
    private DatePicker dateofbirthbox;

    @FXML
    private ComboBox<String> doctorlistconbo;

    @FXML
    private JFXCheckBox femalecheck;

    @FXML
    private JFXCheckBox malecheck;

    @FXML
    private TextArea medicaltxt;

    @FXML
    private TextField patientnametxt;

    @FXML
    private TextField phonetxt;

    @FXML
    private ComboBox<String> townshipconbo;

    private final DentistServices dentistServices;

    public AppointmentController(DentistServices dentistServices) {
        this.dentistServices = dentistServices;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        townshipFilter();
        textFormatter();
        checkgender();

        limitallFuturedate();

        setTooltip();

        doctorFilter();

        limitAppointmentDate();

        limitAppointmentTimeWithAmPm();


    }

    private void setTooltip() {

        Tooltip dobTooltip = new Tooltip("Date of Birth must be between 5 and 120 years ago.");
        dateofbirthbox.setTooltip(dobTooltip);

        Tooltip ageTooltip = new Tooltip("Age must be number.");
        agetxt.setTooltip(ageTooltip);


    }

    private void limitallFuturedate(){

        dateofbirthbox.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (empty) return;

                LocalDate today = LocalDate.now();
                LocalDate minDate = today.minusYears(120);
                LocalDate maxDate = today.minusYears(5);

                if (date.isAfter(maxDate) || date.isBefore(minDate)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffc0cb; -fx-opacity: 0.5;");
                }
            }
        });
    }

    private void limitAppointmentDate() {
        appdatebox.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);

                if (empty) return;

                LocalDate today = LocalDate.now();

                // ယနေ့မတိုင်သေးတဲ့ နေ့တွေပဲ ရွေးလို့ရမယ်
                if (date.isBefore(today)) {
                    setDisable(true);
                    setStyle("-fx-background-color: #ffcccc; -fx-opacity: 0.5;");
                }
            }
        });

        // Tooltip for explanation
        Tooltip appTooltip = new Tooltip("Only today or future dates are allowed for appointment.");
        appdatebox.setTooltip(appTooltip);
    }

    private void checkgender(){

        malecheck.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                femalecheck.setSelected(false);
            }
        });

        femalecheck.selectedProperty().addListener((obs, wasSelected, isSelected) -> {
            if (isSelected) {
                malecheck.setSelected(false);
            }
        });
    }

    private void textFormatter(){

        UnaryOperator<TextFormatter.Change> numberFilter = change -> {
            String newText = change.getControlNewText();

            if (Pattern.matches("\\d{0,3}", newText) && (newText.isEmpty() || Integer.parseInt(newText) <= 150)) {
                return change;
            }
            return null;
        };

        TextFormatter<Integer> textFormatter = new TextFormatter<>(new StringConverter<Integer>() {
            @Override
            public String toString(Integer value) {
                return value == null ? "" : value.toString();
            }

            @Override
            public Integer fromString(String string) {
                try {
                    return string.isEmpty() ? null : Integer.parseInt(string);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        }, null, numberFilter);

        agetxt.setTextFormatter(textFormatter);
    }

    private void townshipFilter(){

        townshipconbo.setEditable(true);


        townshipconbo.setItems(townshiplist());


        townshipconbo.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object == null ? "" : object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        });


        TextField editor = townshipconbo.getEditor();
        editor.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                townshipconbo.setItems(townshiplist());
            } else {
                String filter = newValue.toLowerCase();
                ObservableList<String> filteredList = townshiplist().stream()
                        .filter(township -> township.toLowerCase().contains(filter))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                townshipconbo.setItems(filteredList);
                townshipconbo.show();
            }
        });
    }

    private void doctorFilter(){

        doctorlistconbo.setEditable(true);


        doctorlistconbo.setItems(getDentistlist());


        doctorlistconbo.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String object) {
                return object == null ? "" : object;
            }

            @Override
            public String fromString(String string) {
                return string;
            }
        });


        TextField editor = doctorlistconbo.getEditor();
        editor.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                doctorlistconbo.setItems(getDentistlist());
            } else {
                String filter = newValue.toLowerCase();
                ObservableList<String> filteredList = getDentistlist().stream()
                        .filter(township -> township.toLowerCase().contains(filter))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
                doctorlistconbo.setItems(filteredList);
                doctorlistconbo.show();
            }
        });
    }



    private void limitAppointmentTimeWithAmPm() {
        // Regex for partial input: supports hh, hh:, hh:mm, hh:mm A, hh:mm AM/PM
        Pattern partialPattern = Pattern.compile("(?i)^(1[0-2]|0?[1-9])(:([0-5]\\d?)?)?\\s*(A|P|AM|PM)?$");

        TextFormatter<String> timeFormatter = new TextFormatter<>(change -> {
            String newText = change.getControlNewText();

            if (newText.isEmpty()) {
                return change; // Allow clearing the field
            }

            // Check if the input matches the partial pattern
            if (!partialPattern.matcher(newText).matches()) {
                return null; // Reject invalid input
            }

            // Validate complete input (e.g., "9:00 AM" or "12:00 PM")
            if (newText.matches("(?i)^(1[0-2]|0?[1-9]):[0-5]\\d\\s+(AM|PM)$")) {
                try {
                    String[] parts = newText.split("\\s+");
                    String timePart = parts[0];
                    String ampmPart = parts[1].toUpperCase();

                    String[] timeSplit = timePart.split(":");
                    int hour = Integer.parseInt(timeSplit[0]);
                    int minute = Integer.parseInt(timeSplit[1]);

                    // Convert to 24-hour format for time range validation
                    int hour24 = ampmPart.equals("AM") ? (hour == 12 ? 0 : hour) : (hour == 12 ? 12 : hour + 12);

                    // Restrict to 9:00 AM - 5:00 PM
                    if (hour24 < 9 || hour24 > 17 || (hour24 == 17 && minute > 0)) {
                        return null; // Reject times outside 9:00 AM - 5:00 PM
                    }
                } catch (Exception e) {
                    return null; // Handle parsing errors
                }
            }

            return change; // Accept valid partial or complete input
        });

        apptimetxt.setTextFormatter(timeFormatter);
    }





    private ObservableList<String> townshiplist(){

        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("Ahlone");
        list.add("Bahan");
        list.add("Botahtaung");
        list.add("Dagon");
        list.add("Dagon Seikkan");
        list.add("Dala");
        list.add("Dawbon");
        list.add("East Dagon");
        list.add("Hlaing");
        list.add("Hlaingthaya");
        list.add("Insein");
        list.add("Kamayut");
        list.add("Kyauktada");
        list.add("Kyimyindaing");
        list.add("Lanmadaw");
        list.add("Latha");
        list.add("Mayangone");
        list.add("Mingala Taungnyunt");
        list.add("Mingaladon");
        list.add("North Dagon");
        list.add("North Okkalapa");
        list.add("Pabedan");
        list.add("Pazundaung");
        list.add("Sanchaung");
        list.add("Seikkyi Kanaungto");
        list.add("Shwepyitha");
        list.add("South Dagon");
        list.add("South Okkalapa");
        list.add("Tamwe");
        list.add("Thaketa");
        list.add("Thingangyun");
        list.add("Yankin");
        list.add("Kyeemyindaing");


        return list;
    }

    private ObservableList<String>getDentistlist(){

        return dentistServices.getDentists().stream()
                .map(Dentist::getName).collect(Collectors.toCollection(FXCollections::observableArrayList));

    }
}
