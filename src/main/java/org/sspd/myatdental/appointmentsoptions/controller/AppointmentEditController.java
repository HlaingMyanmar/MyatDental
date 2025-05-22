package org.sspd.myatdental.appointmentsoptions.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import jfxtras.scene.control.LocalTimePicker;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;


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
    private JFXButton submitbtn;

    @FXML
    private LocalTimePicker timepicker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
