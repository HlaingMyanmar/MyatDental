package org.sspd.myatdental.appointmentsoptions.controller;

import com.jfoenix.controls.JFXButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.appointmentsoptions.model.AppointmentView;
import org.sspd.myatdental.appointmentsoptions.service.AppointmentService;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static org.sspd.myatdental.App.App.context;

@Controller
public class AppointmentDashboardController implements Initializable {

    @FXML
    private JFXButton addAppbtn;

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
    private TableColumn<AppointmentView, String> genderCol;

    @FXML
    private JFXButton invoicebtn;


    @FXML
    private TableColumn<AppointmentView, String> patNameCol;

    @FXML
    private TableColumn<AppointmentView, String> patPhoneCol;

    @FXML
    private TableColumn<AppointmentView, String> purposeCol;


    @FXML
    private DatePicker searchapDate;

    @FXML
    private JFXButton searchapDatebtn;


    @FXML
    private TableColumn<AppointmentView, String> statusCol;

    @FXML
    private TableColumn<AppointmentView, Time> timeCol;

    @FXML
    private TableColumn<AppointmentView, String> townshipCol;

    @FXML
    private TextField yeartxt;

    private final AppointmentService appointmentService;


    public AppointmentDashboardController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        treatmentInitializable();

        ini();


        actionEvent();

        getFilteredData();




    }

    private void actionEvent() {


        // Handle searchapDatebtn click to filter by selected date





        addAppbtn.setOnAction(event -> {

            Stage stage = new Stage();
            // Load FXML with Spring's ApplicationContext
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/appointmentlayouts/appointmentinsertview.fxml"));
            fxmlLoader.setControllerFactory(context::getBean); // Set before load()
            Scene scene = null;
            try {
                scene = new Scene(fxmlLoader.load());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Configure stage
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            Stage mainStage = (Stage) addAppbtn.getScene().getWindow();
            stage.initOwner(mainStage);
            stage.setTitle("New Appointment");
            stage.setScene(scene);
            stage.show();


            stage.setOnCloseRequest(event1 -> {


                getFilteredData();


            });



        });
    }

    private void ini(){




    }



    private void treatmentInitializable() {

        codeCol.setCellValueFactory(new PropertyValueFactory<>("appointment_id"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("appointment_date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("appointment_time"));
        drnameCol.setCellValueFactory(new PropertyValueFactory<>("doctor_name"));
        patNameCol.setCellValueFactory(new PropertyValueFactory<>("patient_name"));
        patPhoneCol.setCellValueFactory(new PropertyValueFactory<>("patient_phone"));

        dobCol.setCellValueFactory(new PropertyValueFactory<>("date_of_birth"));
        genderCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        purposeCol.setCellValueFactory(new PropertyValueFactory<>("purpose"));
        townshipCol.setCellValueFactory(new PropertyValueFactory<>("township"));




    }

    private ObservableList<AppointmentView> getAppdashboard() {
        return FXCollections.observableArrayList(appointmentService.getAppointmentViews());
    }

    private ObservableList<AppointmentView> getTodayAppdashboard(LocalDate date) {

        java.sql.Date datefilter = java.sql.Date.valueOf(date);
        return FXCollections.observableArrayList(
                appointmentService.getAppointmentViews().stream()
                        .filter(app -> app.getAppointment_date().equals(datefilter))
                        .collect(Collectors.toList())
        );
    }

    private void updateCountLabel(FilteredList<AppointmentView> filteredData) {
        counttxt.setText("Total Appointments: " + filteredData.size());
    }

    private ObservableList<AppointmentView> getFilteredData() {
        ObservableList<AppointmentView> masterData = getAppdashboard();

        // Create a FilteredList to wrap the master data
        FilteredList<AppointmentView> filteredData = new FilteredList<>(masterData, p -> true);

        // Set the TableView to use the filtered data
        appdashboard.setItems(filteredData);

        // Set placeholder for empty TableView
        appdashboard.setPlaceholder(new Label("ယနေ့အတွက် ရက်ချိန်းများ မရှိပါ။"));

        // Helper method to update the predicate based on current filters
        Runnable updatePredicate = () -> {
            filteredData.setPredicate(appointment -> {
                // Get current search text and date
                String searchText = allsearchtxt.getText();
                LocalDate selectedDate = searchapDate.getValue();

                // Text filter logic
                boolean textMatch = true;
                if (searchText != null && !searchText.isEmpty()) {
                    String lowerCaseFilter = searchText.toLowerCase();
                    textMatch = appointment.getPatient_name().toLowerCase().contains(lowerCaseFilter) ||
                            appointment.getDoctor_name().toLowerCase().contains(lowerCaseFilter) ||
                            appointment.getPatient_phone().toLowerCase().contains(lowerCaseFilter) ||
                            appointment.getPurpose().toLowerCase().contains(lowerCaseFilter) ||
                            appointment.getTownship().toLowerCase().contains(lowerCaseFilter) ||
                            appointment.getGender().toLowerCase().contains(lowerCaseFilter) ||
                            appointment.getStatus().toLowerCase().contains(lowerCaseFilter);
                }

                // Date filter logic
                boolean dateMatch = true;
                if (selectedDate != null) {
                    java.sql.Date sqlDate = java.sql.Date.valueOf(selectedDate);
                    dateMatch = appointment.getAppointment_date().equals(sqlDate);
                }

                // Combine both filters
                return textMatch && dateMatch;
            });

            // Update count label
            updateCountLabel(filteredData);
        };

        // Add listener to allsearchtxt to update filter on text change
        allsearchtxt.textProperty().addListener((observable, oldValue, newValue) -> updatePredicate.run());

        // Filter by selected date when searchapDatebtn is clicked
        searchapDatebtn.setOnAction(event -> updatePredicate.run());

        // Handle reset button to clear filters
        allresetbtn.setOnAction(event -> {
            searchapDate.setValue(null); // Clear DatePicker
            allsearchtxt.setText(""); // Clear search text
            updatePredicate.run(); // Update predicate
        });

        // Update count label initially
        updateCountLabel(filteredData);

        return filteredData;
    }











}
