package org.sspd.myatdental.appointmentsoptions.controller;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.App.App;
import org.sspd.myatdental.DateTime.TimeComponent;
import org.sspd.myatdental.ErrorHandler.AppointmentConflictException;
import org.sspd.myatdental.appointmentsoptions.model.Appointment;
import org.sspd.myatdental.appointmentsoptions.model.AppointmentView;
import org.sspd.myatdental.appointmentsoptions.service.AppointmentService;
import org.sspd.myatdental.appointmentsoptions.service.PatientAppointmentService;
import org.sspd.myatdental.invoiceoptions.Controller.TreatmentInvoiceController;
import org.sspd.myatdental.patientoptions.model.Patient;
import org.sspd.myatdental.patientoptions.service.PatientService;
import org.sspd.myatdental.useroptions.Users;

import java.io.IOException;
import java.net.URL;
import java.sql.Time;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static org.sspd.myatdental.App.App.context;
import static org.sspd.myatdental.alert.AlertBox.showErrorDialog;
import static org.sspd.myatdental.alert.AlertBox.showInformationDialog;

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
    private Label usernamelb;


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

    @FXML
    private Label clockbtn;

    @FXML
    private FontAwesomeIconView closebtn;

    @FXML
    private FontAwesomeIconView minimizebtn;

    @FXML
    private JFXButton dentistbtn;


    @FXML
    private JFXButton treatementbtn;

    @FXML
    private AnchorPane switchPane;


    @FXML
    private JFXButton appointmentreportbtn;


    public static String _username  = "";

    private final AppointmentService appointmentService;

    private final PatientService patientService;

    private final PatientAppointmentService patientAppointmentService;

    private final TreatmentInvoiceController treatmentInvoiceController;


    public AppointmentDashboardController(AppointmentService appointmentService, PatientService patientService, PatientAppointmentService patientAppointmentService, TreatmentInvoiceController treatmentInvoiceController) {
        this.appointmentService = appointmentService;
        this.patientService = patientService;
        this.patientAppointmentService = patientAppointmentService;
        this.treatmentInvoiceController = treatmentInvoiceController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        treatmentInitializable();

        actionEvent();

        getFilteredData();

        Tooltip.install(closebtn, new Tooltip("Close application"));

        Tooltip.install(minimizebtn, new Tooltip("Minimize application"));

        new TimeComponent().initializeClock(clockbtn);



    }

    private void actionEvent() {

        editAppbtn.setOnAction(event -> {
            AppointmentView selectedAppointment = appdashboard.getSelectionModel().getSelectedItem();
            if (selectedAppointment != null) {
                openEditChooseView(selectedAppointment.getAppointment_id());
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "ကျေးဇူးပြု၍ တည်းဖြတ်ရန် ရက်ချိန်းတစ်ခုရွေးပါ။"); // "Please select an appointment to edit."
                alert.showAndWait();
            }
        });

        appdashboard.setOnMouseClicked(event -> {

            delAppbtn.setOnAction(event1 ->
            {
                try {

               AppointmentView appointmentView  =  appdashboard.getSelectionModel().getSelectedItem();

                Appointment selectedAppointment = getAppointment(appointmentView.getAppointment_id()); // Replace with your method to get the selected appointment
                Patient patient = getPatient(selectedAppointment.getPatient().getPatient_id()); // Replace with your method to get the patient
                Users currentUser  = getUser();



                if (currentUser == null) {
                    showErrorDialog( "Error", "Please select an appointment, patient, and ensure you are logged in.","");
                    return;
                }
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Deletion");
                confirmAlert.setHeaderText(null);
                confirmAlert.setContentText("Are you sure you want to delete this appointment?");
                Optional<ButtonType> result = confirmAlert.showAndWait();

                if (result.isPresent() && result.get() == ButtonType.OK) {

                    Dialog<String> dialog = new Dialog<>();
                    dialog.setTitle("Deletion Reason");
                    dialog.setHeaderText("Why are you deleting this appointment?");

                    TextArea textArea = new TextArea();
                    textArea.setPromptText("Enter reason here...");
                    textArea.setPrefRowCount(5);
                    textArea.setPrefColumnCount(30);
                    textArea.setWrapText(true);

                    // Set the content of the dialog
                    dialog.getDialogPane().setContent(textArea);

                    // Request focus on the TextArea
                    Platform.runLater(() -> textArea.requestFocus());

                    ButtonType submitButtonType = new ButtonType("Submit", ButtonBar.ButtonData.OK_DONE);
                    dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

                    // Convert the result to a string when Submit is clicked
                    dialog.setResultConverter(dialogButton -> {
                        if (dialogButton == submitButtonType) {
                            return textArea.getText();
                        }
                        return null;
                    });

                    Optional<String> reasonResult = dialog.showAndWait();

                    String reason = reasonResult.orElse("").trim();

                    if (reasonResult.isPresent() && !reasonResult.get().trim().isEmpty()) {

                        boolean deleted = patientAppointmentService.deletePatientAppointment(patient, selectedAppointment, currentUser,reason);

                        if (deleted) {
                            showInformationDialog( "Success", "Appointment deleted successfully.","");
                            getFilteredData();
                        } else {
                            showErrorDialog( "Error", "Failed to delete the appointment.","");
                        }

                    }

                    else {
                        // No action taken if the user cancels or provides an empty reason
                        showInformationDialog("Info", "Appointment deletion canceled.", "");
                    }




                }
            } catch (AppointmentConflictException e) {
                showErrorDialog( "Error", e.getMessage(),"");
            } catch (Exception e) {
                showErrorDialog( "Unexpected Error", "An unexpected error occurred: " + e.getMessage(),"");
            }










            });


        });

        closebtn.setOnMouseClicked(event -> {
            Platform.exit(); // application thread ကို gracefully ပိတ်သည်
        });

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

        dentistbtn.setOnAction(event -> {


            openDentistView();

        });

        treatementbtn.setOnAction(event -> {

            openTreatmentView();
        });

        invoicebtn.setOnAction(event -> {

            AppointmentView selectedAppointment = appdashboard.getSelectionModel().getSelectedItem();

            if (selectedAppointment != null) {
                Appointment appointment = getAppointment(selectedAppointment.getAppointment_id());
                if (appointment != null &&
                        !appointment.getStatus().equals("Completed") &&
                        !appointment.getStatus().equals("Cancelled")) {

                    treatmentInvoiceController.setAppointment(appointment);
                    openInvoiceView();
                } else {
                    showErrorDialog("Invoice","Not Found","Appointment not found.");
                }
            } else {
                showErrorDialog("Invoice","Select Invoice","Please select an appointment to view the invoice.");
            }
        });


        appointmentreportbtn.setOnAction(event -> {

            switchReportView();

        });



    }



    private Users getUser() {
        if (_username == null) {
            return null;
        }
        return switch (_username) {
            case "Admin" -> Users.ADMIN;
            case "Staff" -> Users.DR_AUNG;
            case "Dentist" -> Users.DR_MYINT;
            default -> null;
        };
    }

    private void openInvoiceView() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/treatmentinvoice/treatementinvoice.fxml"));
            fxmlLoader.setControllerFactory(App.context::getBean);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(editAppbtn.getScene().getWindow());
            stage.setTitle("Treatment Information");
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(event1 -> {

                getFilteredData();

            });




        } catch (IOException e) {
            throw new RuntimeException("Failed to load Appointmenteditchooseview.fxml", e);
        }


    }

    private void openTreatmentView() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/treatmentslayout/treatmentregister.fxml"));
            fxmlLoader.setControllerFactory(App.context::getBean);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(editAppbtn.getScene().getWindow());
            stage.setTitle("Treatment Information");
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(event1 -> {

                getFilteredData();

            });




        } catch (IOException e) {
            throw new RuntimeException("Failed to load Appointmenteditchooseview.fxml", e);
        }


    }

    private void switchReportView() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/appointmentlayouts/appointmentreportview.fxml"));
            fxmlLoader.setControllerFactory(App.context::getBean);
            Parent root = fxmlLoader.load(); // Load the root node (e.g., a Pane) from FXML
            switchPane.getChildren().clear(); // Clear existing content
            switchPane.getChildren().add(root); // Add the loaded FXML root to switchPane
        } catch (IOException e) {
            throw new RuntimeException("Failed to load appointmentreportview.fxml", e);
        }
    }

    private void openDentistView() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/dentistlayout/dentailregister.fxml"));
            fxmlLoader.setControllerFactory(App.context::getBean);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(editAppbtn.getScene().getWindow());
            stage.setTitle("Dentist Information");
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(event1 -> {

                getFilteredData();

            });




        } catch (IOException e) {
            throw new RuntimeException("Failed to load Appointmenteditchooseview.fxml", e);
        }


    }

    private void openEditChooseView(int appointmentId) {


        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/appointmentlayouts/appointmenteditchooseview.fxml"));
            fxmlLoader.setControllerFactory(App.context::getBean);
            Scene scene = new Scene(fxmlLoader.load());
            AppointmentChooseController controller = fxmlLoader.getController();
            controller.setAppointmentId(appointmentId); // Use camelCase method name
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(editAppbtn.getScene().getWindow());
            stage.setTitle("ရက်ချိန်းရွေးချယ်မှုများကို တည်းဖြတ်ရန်");
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(event1 -> {

                getFilteredData();

            });




        } catch (IOException e) {
            throw new RuntimeException("Failed to load Appointmenteditchooseview.fxml", e);
        }
    }

    private void treatmentInitializable() {

        codeCol.setCellValueFactory(new PropertyValueFactory<>("appointment_id"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("appointment_date"));
        timeCol.setCellValueFactory(new PropertyValueFactory<>("appointment_time"));
        drnameCol.setCellValueFactory(new PropertyValueFactory<>("doctor_name"));
        patNameCol.setCellValueFactory(new PropertyValueFactory<>("patient_name"));
        patPhoneCol.setCellValueFactory(new PropertyValueFactory<>("patient_phone"));

          dobCol.setCellValueFactory(new PropertyValueFactory<>("date_of_birth"));

        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        purposeCol.setCellValueFactory(new PropertyValueFactory<>("purpose"));





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


        usernamelb.setText(_username);




        ObservableList<AppointmentView> masterData = getAppdashboard();


        FilteredList<AppointmentView> filteredData = new FilteredList<>(masterData, p -> true);


        appdashboard.setItems(filteredData);


        appdashboard.setPlaceholder(new Label("ယနေ့အတွက် ရက်ချိန်းများ မရှိပါ။"));


        Runnable updatePredicate = () -> {
            filteredData.setPredicate(appointment -> {

                String searchText = allsearchtxt.getText();
                LocalDate selectedDate = searchapDate.getValue();


                boolean textMatch = true;
                if (searchText != null && !searchText.isEmpty()) {
                    String lowerCaseFilter = searchText.toLowerCase();
                    textMatch = appointment.getPatient_name().toLowerCase().contains(lowerCaseFilter) ||
                            appointment.getDoctor_name().toLowerCase().contains(lowerCaseFilter) ||
                            appointment.getPatient_phone().toLowerCase().contains(lowerCaseFilter) ||
                            appointment.getPurpose().toLowerCase().contains(lowerCaseFilter) ||
                            appointment.getTownship().toLowerCase().contains(lowerCaseFilter) ||
                            appointment.getGender().toLowerCase().contains(lowerCaseFilter) ||
                            appointment.getAppointment_date().toString().contains(lowerCaseFilter) ||
                            appointment.getStatus().toLowerCase().contains(lowerCaseFilter);
                }


                boolean dateMatch = true;
                if (selectedDate != null) {
                    java.sql.Date sqlDate = java.sql.Date.valueOf(selectedDate);
                    dateMatch = appointment.getAppointment_date().equals(sqlDate);
                }


                return textMatch && dateMatch;
            });


            updateCountLabel(filteredData);
        };


        allsearchtxt.textProperty().addListener((observable, oldValue, newValue) -> updatePredicate.run());


        searchapDatebtn.setOnAction(event -> updatePredicate.run());


        allresetbtn.setOnAction(event -> {
            searchapDate.setValue(null);
            allsearchtxt.setText("");
            updatePredicate.run();
            appdashboard.getSelectionModel().clearSelection();
        });


        updateCountLabel(filteredData);

        return filteredData;
    }

    private Appointment getAppointment(int id) {

     return appointmentService.getAppointments().stream().filter(app -> app.getAppointment_id() == id).findFirst().orElse(null);

    }

    private Patient getPatient(int id) {

        return patientService.getPatients().stream().filter(patient -> patient.getPatient_id() == id).findFirst().orElse(null);

    }


    











}
