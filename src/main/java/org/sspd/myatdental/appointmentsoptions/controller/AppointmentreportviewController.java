package org.sspd.myatdental.appointmentsoptions.controller;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.appointmentsoptions.model.AppointmentView;
import org.sspd.myatdental.appointmentsoptions.service.AppointmentService;

import java.net.URL;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AppointmentreportviewController implements Initializable {

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
    private TableColumn<AppointmentView, Date> dateCol; // Changed to Date

    @FXML
    private PieChart doctorChart;

    @FXML
    private TableColumn<AppointmentView, String> drnameCol;

    @FXML
    private Spinner<Integer> monthlyfilterbox;

    @FXML
    private TableColumn<AppointmentView, String> patNameCol;

    @FXML
    private TableColumn<AppointmentView, String> purposeCol;

    @FXML
    private DatePicker searchapDate;

    @FXML
    private JFXButton searchapDatebtn;

    @FXML
    private JFXButton searchapDatebtn1;

    @FXML
    private BarChart<String, Number> statusChart;

    @FXML
    private TableColumn<AppointmentView, String> statusCol;

    @FXML
    private AnchorPane switchPane;

    @FXML
    private TableColumn<AppointmentView, Time> timeCol; // Changed to Time

    @FXML
    private Spinner<Integer> yearlyfilterbox;

    private final AppointmentService appointmentService;
    private ObservableList<AppointmentView> appointmentList;
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat TIME_FORMATTER = new SimpleDateFormat("HH:mm");

    public AppointmentreportviewController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize spinners
        monthlyfilterbox.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, LocalDate.now().getMonthValue()));
        yearlyfilterbox.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(2000, 2030, LocalDate.now().getYear()));

        // Initialize table columns
        codeCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAppointment_id()).asObject());
        dateCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAppointment_date()));
        dateCol.setCellFactory(column -> new TableCell<AppointmentView, Date>() {
            @Override
            protected void updateItem(Date item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(DATE_FORMATTER.format(item));
                }
            }
        });
        drnameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDoctor_name()));
        patNameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPatient_name()));
        purposeCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPurpose()));
        statusCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        timeCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getAppointment_time()));
        timeCol.setCellFactory(column -> new TableCell<AppointmentView, Time>() {
            @Override
            protected void updateItem(Time item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(TIME_FORMATTER.format(item));
                }
            }
        });

        // Load initial data
        loadAppointments();

        // Set up event handlers
        setupEventHandlers();
    }

    private void loadAppointments() {
        List<AppointmentView> appointments = appointmentService.getAppointmentViews();
        appointmentList = FXCollections.observableArrayList(appointments);
        appdashboard.setItems(appointmentList);
        updateCountLabel();
        updateCharts();
    }

    private void setupEventHandlers() {
        // Search by exact date
        searchapDatebtn.setOnAction(event -> searchByDate());

        // Search by date range (month/year)
        searchapDatebtn1.setOnAction(event -> searchByDateRange());

        // Reset button
        allresetbtn.setOnAction(event -> resetFilters());

        // Search text field
        allsearchtxt.textProperty().addListener((observable, oldValue, newValue) -> searchAppointments(newValue));

        // Monthly filter
        monthlyfilterbox.valueProperty().addListener((obs, oldValue, newValue) -> filterByMonthAndYear());

        // Yearly filter
        yearlyfilterbox.valueProperty().addListener((obs, oldValue, newValue) -> filterByMonthAndYear());
    }

    private void searchByDate() {

        LocalDate selectedDate = searchapDate.getValue();



        if (selectedDate != null) {
            List<AppointmentView> filteredAppointments = appointmentService.getAppointmentViews()
                    .stream()
                    .filter(app -> {
                        try {
                            // appointment_date ကို java.sql.Date အဖြစ်ယူပြီး LocalDate ပြောင်း
                            java.sql.Date sqlDate = app.getAppointment_date();
                            LocalDate appointmentDate = sqlDate.toLocalDate();
                            return appointmentDate.equals(selectedDate);
                        } catch (Exception e) {
                            e.printStackTrace(); // အမှား debug အတွက်
                            return false;
                        }
                    })
                    .collect(Collectors.toList());



            // Filter လုပ်ပြီး UI ကို update
            appointmentList.setAll(filteredAppointments);
            updateCountLabel();
            updateCharts();
        }
    }


    private void searchByDateRange() {
        LocalDate selectedDate = searchapDate.getValue();

        if (selectedDate != null) {
            int targetMonth = selectedDate.getMonthValue(); // e.g. 5 for May
            int targetYear = selectedDate.getYear();        // e.g. 2025

            List<AppointmentView> filteredAppointments = appointmentService.getAppointmentViews()
                    .stream()
                    .filter(app -> {
                        try {
                            java.sql.Date sqlDate = app.getAppointment_date();
                            if (sqlDate == null) {
                                return false;
                            }
                            LocalDate appointmentDate = sqlDate.toLocalDate();
                            return appointmentDate.getMonthValue() == targetMonth &&
                                    appointmentDate.getYear() == targetYear;
                        } catch (Exception e) {
                            e.printStackTrace(); // Debug purpose
                            return false;
                        }
                    })
                    .collect(Collectors.toList());



            appointmentList.setAll(filteredAppointments);
            updateCountLabel();
            updateCharts();
        }
    }


    private void searchAppointments(String searchText) {
        if (searchText == null || searchText.isEmpty()) {
            loadAppointments();
            return;
        }

        List<AppointmentView> filteredAppointments = appointmentService.getAppointmentViews()
                .stream()
                .filter(app -> app.getPatient_name().toLowerCase().contains(searchText.toLowerCase()) ||
                        app.getDoctor_name().toLowerCase().contains(searchText.toLowerCase()) ||
                        String.valueOf(app.getAppointment_id()).contains(searchText))
                .collect(Collectors.toList());
        appointmentList.setAll(filteredAppointments);
        updateCountLabel();
        updateCharts();
    }

    private void filterByMonthAndYear() {
        Integer month = monthlyfilterbox.getValue(); // 1 = January, ..., 12 = December
        Integer year = yearlyfilterbox.getValue();   // e.g., 2025

        if (month == null || year == null) {
            return;
        }

        List<AppointmentView> filteredAppointments = appointmentService.getAppointmentViews()
                .stream()
                .filter(app -> {
                    try {
                        // appointment_date ကို java.sql.Date အဖြစ်ယူပြီး LocalDate ပြောင်း
                        java.sql.Date sqlDate = app.getAppointment_date();
                        if (sqlDate == null) {
                            return false;
                        }
                        LocalDate appointmentDate = sqlDate.toLocalDate();
                        return appointmentDate.getMonthValue() == month && appointmentDate.getYear() == year;
                    } catch (Exception e) {
                        e.printStackTrace(); // Debuging purpose
                        return false;
                    }
                })
                .collect(Collectors.toList());



        // Filter လုပ်ပြီး UI ကို update
        appointmentList.setAll(filteredAppointments);
        updateCountLabel();
        updateCharts();
    }






    private void resetFilters() {
        allsearchtxt.clear();
        searchapDate.setValue(null);
        monthlyfilterbox.getValueFactory().setValue(LocalDate.now().getMonthValue());
        yearlyfilterbox.getValueFactory().setValue(LocalDate.now().getYear());
        loadAppointments();
    }

    private void updateCountLabel() {
        counttxt.setText("Total Appointments: " + appointmentList.size());
    }

    private void updateCharts() {
        // Update PieChart (Doctor distribution)
        Map<String, Long> doctorCounts = appointmentList.stream()
                .collect(Collectors.groupingBy(AppointmentView::getDoctor_name, Collectors.counting()));

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        doctorCounts.forEach((doctor, count) ->
                pieChartData.add(new PieChart.Data(doctor, count)));
        doctorChart.setData(pieChartData);

        // Update BarChart (Status distribution)
        Map<String, Long> statusCounts = appointmentList.stream()
                .collect(Collectors.groupingBy(AppointmentView::getStatus, Collectors.counting()));

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Appointment Status");
        statusCounts.forEach((status, count) ->
                series.getData().add(new XYChart.Data<>(status, count)));

        statusChart.getData().clear();
        statusChart.getData().add(series);
    }
}