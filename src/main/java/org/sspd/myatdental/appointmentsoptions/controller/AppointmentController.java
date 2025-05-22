package org.sspd.myatdental.appointmentsoptions.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import jakarta.validation.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import jfxtras.scene.control.LocalTimePicker;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.ErrorHandler.AppointmentConflictException;
import org.sspd.myatdental.ErrorHandler.Validation.GenericValidator;
import org.sspd.myatdental.alert.AlertBox;
import org.sspd.myatdental.appointmentsoptions.model.Appointment;
import org.sspd.myatdental.appointmentsoptions.service.PatientAppointmentService;
import org.sspd.myatdental.dentistsoptions.model.Dentist;
import org.sspd.myatdental.dentistsoptions.service.DentistServices;
import org.sspd.myatdental.patientoptions.model.Patient;
import org.sspd.myatdental.patientoptions.service.PatientService;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.HashSet;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
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
    private LocalTimePicker timepicker;

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


    @FXML
    private Button appointSearchbtn;

    @FXML
    private JFXButton submitbtn;

    private final DentistServices dentistServices;
    private final Validator validator;
    private final PatientService patientService;
    private final PatientAppointmentService patientAppointmentService;
    private final SessionFactory sessionFactory;

    public AppointmentController(DentistServices dentistServices, Validator validator, PatientService patientService, PatientAppointmentService patientAppointmentService, SessionFactory sessionFactory) {
        this.dentistServices = dentistServices;
        this.validator = validator;
        this.patientService = patientService;
        this.patientAppointmentService = patientAppointmentService;
        this.sessionFactory = sessionFactory;
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

        appstatusbox.setItems(statuslist());




        actionEvent();






    }

    private void displayDentistAppointments(int dentistId,String status) {
        List<Appointment[]> appointments = patientAppointmentService.getDentistAppointments(dentistId,status);
        if (appointments.isEmpty()) {
            AlertBox.showInformationDialog("Availability", "No scheduled appointments for this doctor.", "");
        } else {
            StringBuilder message = new StringBuilder("Scheduled appointments for the selected doctor:\n");
            for (Object[] appt : appointments) {
                Date date = (Date) appt[0];
                Time time = (Time) appt[1];
                message.append(date).append(" at ").append(time).append("\n");
            }
            AlertBox.showInformationDialog("Scheduled Appointments", message.toString(), "");
        }
    }

    private void actionEvent() {

        appointSearchbtn.setOnAction(event -> {
            // Option 1: Use selected dentist from doctorlistconbo
            String selectedDoctor = doctorlistconbo.getValue();
            if (selectedDoctor == null) {
                AlertBox.showErrorDialog("Error", "Please select a doctor first.", "");
                return;
            }
            int dentistId = getDentistid(selectedDoctor);

            // Option 2: Hard-code dentist_id = 1 (uncomment if preferred)
            // int dentistId = 1;

            displayDentistAppointments(dentistId,appstatusbox.getValue());
        });



        dateofbirthbox .valueProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue != null) {
                int age = calculateAge(newValue, LocalDate.now());
                agetxt.setText(String.valueOf(age));
            } else {
                agetxt.setText("Age: Not calculated");
            }
        });

        submitbtn.setOnAction(event -> {


            try {
                // Patient Information
                String patientname = patientnametxt.getText();
                String phone = phonetxt.getText();
                Date dob = Date.valueOf(dateofbirthbox.getValue());
                int age = Integer.parseInt(agetxt.getText());
                String gender = checkgender();
                String township = townshipconbo.getValue();
                String mhistory = medicaltxt.getText();
                String address = addresstxt.getText();

                // Appointment Information
                int dentistId = getDentistid(doctorlistconbo.getValue());
                Date apDate = Date.valueOf(appdatebox.getValue());
                LocalTime localTime = timepicker.getLocalTime().withSecond(0);
                Time apTime = Time.valueOf(localTime);
                String status = appstatusbox.getValue();
                String purpose = apppurposetxt.getText();
                String note = appnotetxt.getText();

                Appointment appointment = new Appointment(apDate, apTime, status, purpose, note, getDentist(dentistId));
                Patient patient = new Patient(patientname, phone, township, address, dob, age, gender, mhistory);

                boolean presult = new GenericValidator<Patient>(validator).validate(patient);
                boolean appresult = new GenericValidator<Appointment>(validator).validate(appointment);

                if (presult && appresult) {

                    boolean result = patientAppointmentService.addPatientAppointment(patient, appointment);
                    if (result) {
                        AlertBox.showInformationDialog("Success", "Appointment added successfully.", "");
                    } else {
                        AlertBox.showErrorDialog("Error", "Failed to add appointment.", "");
                    }
                } else {
                    AlertBox.showErrorDialog("Validation Error", "Invalid patient or appointment data.", "");
                }
            } catch (NullPointerException e) {
                AlertBox.showErrorDialog("Error", "Please select a Dr., appointment time, and date of birth.", "");
            } catch (IllegalStateException | AppointmentConflictException e) {
                AlertBox.showErrorDialog("Error", "Please Select other Time.", "");
            }
        });





    }

    private int calculateAge(LocalDate dob, LocalDate currentDate) {
        if (dob == null || currentDate == null) {
            return 0;
        }
        return Period.between(dob, currentDate).getYears();
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


    private Dentist getDentist(int dentistID){
        return dentistServices.getDentists().stream()
                .filter(dentist1 -> dentist1.getDentist_id()==dentistID)
                .findFirst().orElse(null);

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

    private ObservableList<String> statuslist(){

        ObservableList<String> list = FXCollections.observableArrayList();
        list.add("Scheduled");
        list.add("Completed");
        list.add("Cancelled");

        return list;
    }

    private int getDentistid(String dentist){
       return dentistServices.getDentists().stream()
                .filter(dentist1 -> dentist1.getName().equals(dentist))
                .map(Dentist::getDentist_id).findFirst().orElse(-1);
    }

    private ObservableList<String>getDentistlist(){

        return dentistServices.getDentists().stream()
                .map(Dentist::getName).collect(Collectors.toCollection(FXCollections::observableArrayList));

    }
}
