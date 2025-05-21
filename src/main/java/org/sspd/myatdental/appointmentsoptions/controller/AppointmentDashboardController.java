package org.sspd.myatdental.appointmentsoptions.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.appointmentsoptions.model.AppointmentView;
import org.sspd.myatdental.appointmentsoptions.service.AppointmentService;

import java.net.URL;
import java.sql.Time;
import java.util.Date;
import java.util.ResourceBundle;

@Controller
public class AppointmentDashboardController implements Initializable {

    @FXML
    private JFXButton addAppbtn;

    @FXML
    private JFXCheckBox allcheckbox;

    @FXML
    private JFXButton allresetbtn;

    @FXML
    private TextField allsearchtxt;

    @FXML
    private TableView<AppointmentView> appdashboard;

    @FXML
    private TableColumn<AppointmentView, Integer> codeCol;

    @FXML
    private Label counttxt;

    @FXML
    private TableColumn<AppointmentView, Date> dateCol;

    @FXML
    private JFXButton datesearchbtn;

    @FXML
    private JFXButton delAppbtn;

    @FXML
    private TableColumn<AppointmentView, java.sql.Date> dobCol;

    @FXML
    private TableColumn<AppointmentView, String> drnameCol;

    @FXML
    private JFXButton editAppbtn;

    @FXML
    private JFXButton editinvoicebtn;

    @FXML
    private JFXCheckBox femalecheckbox;

    @FXML
    private TableColumn<AppointmentView, String> genderCol;

    @FXML
    private JFXButton invoicebtn;

    @FXML
    private JFXCheckBox malecheckbox;

    @FXML
    private TextField monthtxt;

    @FXML
    private TableColumn<AppointmentView, String> patNameCol;

    @FXML
    private TableColumn<AppointmentView, String> patPhoneCol;

    @FXML
    private TableColumn<AppointmentView, String> purposeCol;

    @FXML
    private JFXButton searchapCodebtn;

    @FXML
    private TextField searchapCodetxt;

    @FXML
    private DatePicker searchapDate;

    @FXML
    private JFXButton searchapDatebtn;

    @FXML
    private JFXButton searchapTimebtn;

    @FXML
    private TextField searchapTimetxt;

    @FXML
    private JFXButton searchbytownshipbtn;

    @FXML
    private TextField searchbytownshiptxt;

    @FXML
    private JFXButton searchdbrNamebtn;

    @FXML
    private TextField searchdbrNametxt;

    @FXML
    private TableColumn<AppointmentView, String> statusCol;

    @FXML
    private TableColumn<AppointmentView, Time> timeCol;

    @FXML
    private TableColumn<AppointmentView, String> townshipCol;

    @FXML
    private TextField yeartxt;

    private final AppointmentService  appointmentService;


    public AppointmentDashboardController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


    }
}
