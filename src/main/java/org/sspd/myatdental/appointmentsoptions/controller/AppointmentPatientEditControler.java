package org.sspd.myatdental.appointmentsoptions.controller;

import com.jfoenix.controls.JFXCheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.patientoptions.model.Patient;
import org.sspd.myatdental.patientoptions.service.PatientService;

import java.net.URL;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


@Controller
public class AppointmentPatientEditControler implements Initializable {

    @FXML
    private TextArea addresstxt;

    @FXML
    private TextField agetxt;

    @FXML
    private DatePicker dateofbirthbox;

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

    @FXML
    private VBox mainPane;


    @FXML
    private Button downbtn;

    @FXML
    private Button savebtn;

    private Patient PATIENT =null;

    private PatientService patientService;

    public AppointmentPatientEditControler(PatientService patientService) {
        this.patientService = patientService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        townshipFilter();
        textFormatter();
        checkgender();
        limitallFuturedate();



        KeyCodeCombination ctrlS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);

        mainPane.setOnKeyPressed(event -> {
            if (ctrlS.match(event)) {


                System.out.println(PATIENT.getPatient_id());

                event.consume();
            }
        });

        downbtn.setOnAction(event -> {



             fillOldInformation();

        });




    }


    private void fillOldInformation(){

        patientnametxt.setText(PATIENT.getName());
        phonetxt.setText(PATIENT.getPhone());
        dateofbirthbox.setValue(PATIENT.getDate_of_birth().toLocalDate());
        agetxt.setText(String.valueOf(PATIENT.getAge()));

        if(Objects.equals(PATIENT.getGender(), "Female")){
            femalecheck.setSelected(true);
        }
        else if(Objects.equals(PATIENT.getGender(), "Male")){
            malecheck.setSelected(true);
        }
        townshipconbo.setValue(PATIENT.getTownship());

        medicaltxt.setText(PATIENT.getMedical_history());

        addresstxt.setText(PATIENT.getAddress());



    }

    public void setPatient(Patient patient) {

        this.PATIENT = patientService.getPatients().stream()
                 .filter(patient1 -> patient1.getPatient_id()==patient.getPatient_id())
                .findFirst().orElse(null);
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

    private String  checkgender(){

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

        return malecheck.isSelected()  ? "Male" :"Female";
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
}
