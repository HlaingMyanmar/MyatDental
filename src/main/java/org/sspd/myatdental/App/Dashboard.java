package org.sspd.myatdental.App;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.DateTime.TimeComponent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.sspd.myatdental.appointmentsoptions.controller.AppointmentDashboardController._username;

@Controller
public class Dashboard implements Initializable {


    @FXML
    private Label clockbtn;

    @FXML
    private FontAwesomeIconView closebtn;

    @FXML
    private Button dashboardbtn;

    @FXML
    private Button dentistbtn;

    @FXML
    private Button invoicebtn;

    @FXML
    private Button logoffbtn;

    @FXML
    private FontAwesomeIconView minimizebtn;

    @FXML
    private Button patientbtn;

    @FXML
    private Button paymentbtn;

    @FXML
    private Button reportbtn;

    @FXML
    private AnchorPane switchPane;

    @FXML
    private Button treatmentbtn;

    @FXML
    private Button userbtn;

    @FXML
    private Label usernamelb;


    @FXML
    private Button appointmentbtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        new TimeComponent().initializeClock(clockbtn);

        usernamelb.setText(_username);

        dentistbtn.setOnAction(e -> {
            loadDentistView();
        });

        treatmentbtn.setOnAction(e -> {
            loadTreatmentView();
        });

        closebtn.setOnMouseClicked(event -> {
            Platform.exit();
        });

        patientbtn.setOnAction(e -> {

            loadPatientView();

        });

        paymentbtn.setOnAction(e -> {

            loadPaymentView();


        });

        appointmentbtn.setOnAction(e -> {

            loadAppointmentView();


        });

    }
    private void loadDentistView() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/dentistlayout/dentailregister.fxml"));
            fxmlLoader.setControllerFactory(App.context::getBean);
            Parent dentistView = fxmlLoader.load();

            // Clear existing content in switchPane
            switchPane.getChildren().clear();

            // Add dentistView to switchPane
            switchPane.getChildren().add(dentistView);

            // Optional: anchor it to all sides if it is resizable and supports anchoring
            AnchorPane.setTopAnchor(dentistView, 10.5);
            AnchorPane.setBottomAnchor(dentistView, 0.0);
            AnchorPane.setLeftAnchor(dentistView, 0.0);
            AnchorPane.setRightAnchor(dentistView, 0.0);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load dentailregister.fxml", e);
        }
    }

    private void loadAppointmentView() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/appointment/appointmentdashboard.fxml"));
            fxmlLoader.setControllerFactory(App.context::getBean);
            Parent dentistView = fxmlLoader.load();

            // Clear existing content in switchPane
            switchPane.getChildren().clear();

            // Add dentistView to switchPane
            switchPane.getChildren().add(dentistView);

            // Optional: anchor it to all sides if it is resizable and supports anchoring
            AnchorPane.setTopAnchor(dentistView, 10.5);
            AnchorPane.setBottomAnchor(dentistView, 0.0);
            AnchorPane.setLeftAnchor(dentistView, 0.0);
            AnchorPane.setRightAnchor(dentistView, 0.0);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load dentailregister.fxml", e);
        }
    }

    private void loadTreatmentView() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/treatmentslayout/treatmentview.fxml"));
            fxmlLoader.setControllerFactory(App.context::getBean);
            Parent treatmentView = fxmlLoader.load();

            // Clear existing content in switchPane
            switchPane.getChildren().clear();

            // Add treatmentView to switchPane
            switchPane.getChildren().add(treatmentView);

            // Optional: anchor it to all sides if it is resizable and supports anchoring
            AnchorPane.setTopAnchor(treatmentView, 0.0);
            AnchorPane.setBottomAnchor(treatmentView, 0.0);
            AnchorPane.setLeftAnchor(treatmentView, 0.0);
            AnchorPane.setRightAnchor(treatmentView, 0.0);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load treatmentregister.fxml", e);
        }
    }

    private void loadPatientView() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/patients/patientview.fxml"));
            fxmlLoader.setControllerFactory(App.context::getBean);
            Parent dentistView = fxmlLoader.load();

            // Clear existing content in switchPane
            switchPane.getChildren().clear();

            // Add dentistView to switchPane
            switchPane.getChildren().add(dentistView);

            // Optional: anchor it to all sides if it is resizable and supports anchoring
            AnchorPane.setTopAnchor(dentistView, 10.5);
            AnchorPane.setBottomAnchor(dentistView, 0.0);
            AnchorPane.setLeftAnchor(dentistView, 0.0);
            AnchorPane.setRightAnchor(dentistView, 0.0);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load dentailregister.fxml", e);
        }
    }

    private void loadPaymentView() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/payment/paymentview.fxml"));
            fxmlLoader.setControllerFactory(App.context::getBean);
            Parent dentistView = fxmlLoader.load();

            // Clear existing content in switchPane
            switchPane.getChildren().clear();

            // Add dentistView to switchPane
            switchPane.getChildren().add(dentistView);

            // Optional: anchor it to all sides if it is resizable and supports anchoring
            AnchorPane.setTopAnchor(dentistView, 10.5);
            AnchorPane.setBottomAnchor(dentistView, 0.0);
            AnchorPane.setLeftAnchor(dentistView, 0.0);
            AnchorPane.setRightAnchor(dentistView, 0.0);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load dentailregister.fxml", e);
        }
    }





}
