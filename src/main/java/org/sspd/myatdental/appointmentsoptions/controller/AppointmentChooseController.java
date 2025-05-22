package org.sspd.myatdental.appointmentsoptions.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.App.App;
import org.sspd.myatdental.appointmentsoptions.model.Appointment;
import org.sspd.myatdental.appointmentsoptions.model.AppointmentView;
import org.sspd.myatdental.appointmentsoptions.service.AppointmentService;
import org.sspd.myatdental.patientoptions.model.Patient;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


@Controller
public class AppointmentChooseController implements Initializable {

    @FXML
    private Button appointmentInfoButton;

    @FXML
    private Button patientInfoButton;

    private  int APPOINTMENT_ID =0;

    private AppointmentService appointmentService;

    public AppointmentChooseController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {




        appointmentInfoButton.setOnAction(event -> {

            if(getAppointment(APPOINTMENT_ID)!=null){
                openEditChooseView(getAppointment(APPOINTMENT_ID));
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "ကျေးဇူးပြု၍ တည်းဖြတ်ရန် ရက်ချိန်းတစ်ခုရွေးပါ။"); // "Please select an appointment to edit."
                alert.showAndWait();
            }

        });

        patientInfoButton.setOnAction(event -> {


                Patient patientID =getPatientId(APPOINTMENT_ID);


            if (patientID !=null) {

                openEditChooseView(patientID);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "ကျေးဇူးပြု၍ တည်းဖြတ်ရန် ရက်ချိန်းတစ်ခုရွေးပါ။"); // "Please select an appointment to edit."
                alert.showAndWait();
            }


        });

    }


    public void setAppointmentId(int appointmentId) {
        this.APPOINTMENT_ID = appointmentId;
    }

    private void openEditChooseView(Patient patient) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/appointmentlayouts/appointmentpatienteditview.fxml"));
            fxmlLoader.setControllerFactory(App.context::getBean);
            Scene scene = new Scene(fxmlLoader.load());
            AppointmentPatientEditControler controller = fxmlLoader.getController();
            controller.setPatient(patient);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(patientInfoButton.getScene().getWindow());
            stage.setTitle("ရက်ချိန်းရွေးချယ်မှုများကို တည်းဖြတ်ရန်");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Appointmenteditchooseview.fxml", e);
        }
    }

    private void openEditChooseView(Appointment appointment) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/appointmentlayouts/appointmenteditview.fxml"));
            fxmlLoader.setControllerFactory(App.context::getBean);
            Scene scene = new Scene(fxmlLoader.load());
            AppointmentEditController controller = fxmlLoader.getController();
            controller.setAppointment(appointment);
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(patientInfoButton.getScene().getWindow());
            stage.setTitle("ရက်ချိန်းရွေးချယ်မှုများကို တည်းဖြတ်ရန်");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException("Failed to load Appointmenteditchooseview.fxml", e);
        }
    }


    private Patient  getPatientId(int appointmentId) {

        Patient patient = getAppointments().stream()
            .filter(appointment -> appointment.getAppointment_id()==appointmentId)
            .map(Appointment::getPatient).findFirst().orElse(null);

        return  patient;
    }
    private Appointment getAppointment(int appointmentId) {
       return getAppointments().stream().filter(appointment -> appointment.getAppointment_id()==appointmentId)
               .findFirst().orElse(null);

    }

    private List<Appointment> getAppointments() {

      return   appointmentService.getAppointments();


    }




}
