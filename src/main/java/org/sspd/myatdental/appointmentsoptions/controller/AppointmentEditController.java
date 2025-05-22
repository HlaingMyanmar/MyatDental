package org.sspd.myatdental.appointmentsoptions.controller;

import com.jfoenix.controls.JFXButton;
import jakarta.validation.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.scene.control.LocalTimePicker;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.ErrorHandler.Validation.GenericValidator;
import org.sspd.myatdental.alert.AlertBox;
import org.sspd.myatdental.appointmentsoptions.model.Appointment;
import org.sspd.myatdental.appointmentsoptions.service.AppointmentService;
import org.sspd.myatdental.dentistsoptions.model.Dentist;
import org.sspd.myatdental.dentistsoptions.service.DentistServices;

import java.net.URL;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ResourceBundle;
import java.util.stream.Collectors;


@Controller
public class AppointmentEditController implements Initializable {

    @FXML
    private DatePicker appdatebox;

    @FXML
    private TextArea appnotetxt;

    @FXML
    private Button appointSearchbtn;

    @FXML
    private TextArea apppurposetxt;

    @FXML
    private ComboBox<String> appstatusbox;

    @FXML
    private ComboBox<String> doctorlistconbo;

    @FXML
    private Button downbtn;

    @FXML
    private Button savebtn;


    @FXML
    private VBox mainPane;


    @FXML
    private LocalTimePicker timepicker;

    private final DentistServices dentistServices;

    private Validator validator;

    private AppointmentService appointmentService;

    @Setter
    private Appointment appointment = null;

    public AppointmentEditController(DentistServices dentistServices, Validator validator, AppointmentService appointmentService) {
        this.dentistServices = dentistServices;
        this.validator = validator;
        this.appointmentService = appointmentService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        doctorFilter();
        limitAppointmentDate();
        appstatusbox.setItems(statuslist());

        KeyCodeCombination ctrlS = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);

        downbtn.setOnAction(event -> {


            fillappointmentInformation();

        });

        mainPane.setOnKeyPressed(event -> {
            if (ctrlS.match(event)) {

                // Appointment Information
                int dentistId = getDentistid(doctorlistconbo.getValue());
                Date apDate = Date.valueOf(appdatebox.getValue());
                LocalTime localTime = timepicker.getLocalTime().withSecond(0);
                Time apTime = Time.valueOf(localTime);
                String status = appstatusbox.getValue();
                String purpose = apppurposetxt.getText();
                String note = appnotetxt.getText();

                Appointment editappointment = new Appointment(appointment.getAppointment_id(),apDate, apTime, status, purpose, note,appointment.getPatient(), getDentist(dentistId));

                boolean appresult = new GenericValidator<Appointment>(validator).validate(editappointment);
                if (appresult) {

                    boolean result = appointmentService.updateAppointment(editappointment);

                    if (result) {
                        AlertBox.showInformationDialog("Success", "Appointment Edit successfully.", "");
                        Stage stage = (Stage) downbtn.getScene().getWindow();
                        stage.close();


                    } else {
                        AlertBox.showErrorDialog("Error", "Failed to Edit appointment.", "");
                    }

                }
                else {
                    AlertBox.showErrorDialog("Validation Error", "Invalid patient data.", "");
                }

                event.consume();
            }
        });


        savebtn.setOnAction(event -> {


            // Appointment Information
            int dentistId = getDentistid(doctorlistconbo.getValue());
            Date apDate = Date.valueOf(appdatebox.getValue());
            LocalTime localTime = timepicker.getLocalTime().withSecond(0);
            Time apTime = Time.valueOf(localTime);
            String status = appstatusbox.getValue();
            String purpose = apppurposetxt.getText();
            String note = appnotetxt.getText();

            Appointment editappointment = new Appointment(appointment.getAppointment_id(),apDate, apTime, status, purpose, note,appointment.getPatient(), getDentist(dentistId));

            boolean appresult = new GenericValidator<Appointment>(validator).validate(editappointment);
            if (appresult) {

                boolean result = appointmentService.updateAppointment(editappointment);

                if (result) {
                    AlertBox.showInformationDialog("Success", "Appointment Edit successfully.", "");
                    Stage stage = (Stage) downbtn.getScene().getWindow();
                    stage.close();


                } else {
                    AlertBox.showErrorDialog("Error", "Failed to Edit appointment.", "");
                }

            }
            else {
                AlertBox.showErrorDialog("Validation Error", "Invalid patient data.", "");
            }

        });

    }

    private void fillappointmentInformation(){

        doctorlistconbo.setValue(appointment.getDentist().getName());
        appdatebox.setValue(appointment.getAppointment_date().toLocalDate());
        timepicker.setLocalTime(appointment.getAppointment_time().toLocalTime());
        appstatusbox.setValue(appointment.getStatus());
        apppurposetxt.setText(appointment.getPurpose());
        appnotetxt.setText(appointment.getNotes());


    }







    private void doctorFilter(){

        doctorlistconbo.setEditable(true);


        doctorlistconbo.setItems(getDentistlist());


        doctorlistconbo.setConverter(new StringConverter<>() {
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

    private ObservableList<String>getDentistlist(){

        return dentistServices.getDentists().stream()
                .map(Dentist::getName).collect(Collectors.toCollection(FXCollections::observableArrayList));

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

    private Dentist getDentist(int dentistID){
        return dentistServices.getDentists().stream()
                .filter(dentist1 -> dentist1.getDentist_id()==dentistID)
                .findFirst().orElse(null);

    }
}


