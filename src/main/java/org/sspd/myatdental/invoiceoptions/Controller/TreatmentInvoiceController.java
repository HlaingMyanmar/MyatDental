package org.sspd.myatdental.invoiceoptions.Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.alert.AlertBox;
import org.sspd.myatdental.appointmentsoptions.model.Appointment;
import org.sspd.myatdental.invoiceoptions.service.TreatmentInvoiceService;
import org.sspd.myatdental.treatmentoptions.model.TreatRecordViewModel;
import org.sspd.myatdental.treatmentoptions.model.Treatment;
import org.sspd.myatdental.treatmentoptions.model.TreatmentRecord;
import org.sspd.myatdental.treatmentoptions.service.TreatmentService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Controller
public class TreatmentInvoiceController implements Initializable {

    @FXML
    private Label appointmentcodelb;

    @FXML
    private Label patientNamelb;

    @FXML
    private ComboBox<String> treatmentComboBox;

    @Setter
    private Appointment appointment;

    @FXML
    private TextField toothnumtxt;

    @FXML
    private Button addbtn;


    @FXML
    private TextArea notetxt;


    @FXML
    private TableColumn<TreatRecordViewModel, String> noteCol;


    @FXML
    private TableColumn<TreatRecordViewModel, Double> priceCol;



    @FXML
    private TableView<TreatRecordViewModel> recordtable;


    @FXML
    private TableColumn<TreatRecordViewModel, String> tnumCol;

    @FXML
    private TableColumn<TreatRecordViewModel, String> treatmentCol;


    private final TreatmentService treatmentService;
    private final TreatmentInvoiceService invoiceService;

    private ObservableList<TreatmentRecord> treatments = FXCollections.observableArrayList();

    @Autowired
    public TreatmentInvoiceController(TreatmentService treatmentService, TreatmentInvoiceService invoiceService) {
        this.treatmentService = treatmentService;
        this.invoiceService = invoiceService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        if (appointment != null) {
            appointmentcodelb.setText(String.valueOf(appointment.getAppointment_id()));
            patientNamelb.setText(appointment.getPatient() != null ? appointment.getPatient().getName() : "Unknown");
        } else {
            appointmentcodelb.setText("N/A");
            patientNamelb.setText("No appointment selected");
        }
        fillTreatmentComboBox();

        actionEvent();

        tableIni();






    }

    private void tableIni(){

        treatmentCol.setCellValueFactory(new PropertyValueFactory<>("treatmentName"));
        tnumCol.setCellValueFactory(new PropertyValueFactory<>("toothNumber"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        noteCol.setCellValueFactory(new PropertyValueFactory<>("notes"));

    }

    private void actionEvent() {

        addbtn.setOnAction(event -> {

            String toothNum  = toothnumtxt.getText();
            String note = notetxt.getText();


            Treatment treatment  = getTreatmentByName(treatmentComboBox.getValue());

            assert treatment != null;
            TreatmentRecord record = new TreatmentRecord(appointment,treatment,toothNum,note,treatment.getStandard_price());
            treatments.add(record);
            recordtable.setItems(getLoadData());


        });

    }

    private ObservableList<TreatRecordViewModel> getLoadData(){


        ObservableList<TreatRecordViewModel> treatmentview = FXCollections.observableArrayList();

        treatments.forEach(treatment -> {

            TreatRecordViewModel m1 = new TreatRecordViewModel(treatment.getToothNumber(),treatment.getNotes(),treatment.getPrice(),treatment.getTreatment().getName());
            treatmentview.add(m1);
        });


    return treatmentview;

    }

    private void fillTreatmentComboBox() {

        treatmentComboBox.setEditable(true);
        ObservableList<String> treatmentNames = getTreatments();
        treatmentComboBox.setItems(treatmentNames);

        // Set StringConverter for consistency (optional for String-based ComboBox)
        treatmentComboBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String name) {
                return name != null ? name : "";
            }

            @Override
            public String fromString(String string) {
                return string; // Return the input string as-is
            }
        });

        // Add listener for filtering treatment names as user types
        TextField editor = treatmentComboBox.getEditor();
        editor.textProperty().addListener((obs, oldValue, newValue) -> {
            ObservableList<String> filteredList;
            if (newValue == null || newValue.trim().isEmpty()) {
                filteredList = getTreatments();
            } else {
                String filter = newValue.toLowerCase();
                filteredList = getTreatments().stream()
                        .filter(name -> name.toLowerCase().contains(filter))
                        .collect(Collectors.toCollection(FXCollections::observableArrayList));
            }
            treatmentComboBox.setItems(filteredList);
            if (!filteredList.isEmpty()) {
                treatmentComboBox.show();
            }
        });

        // Warn user if no treatments are available
        if (treatmentNames.isEmpty()) {
            AlertBox.showInformationDialog("Treatment","Not Found","No treatments available. Please add treatments in the system.");
        }

    }

    private ObservableList<String> getTreatments() {
        List<Treatment> treatments = treatmentService.getTreatments();
        List<String> treatmentNames = treatments != null ?
                treatments.stream().map(Treatment::getName).collect(Collectors.toList()) :
                List.of();
        return FXCollections.observableArrayList(treatmentNames);
    }

    private Treatment getTreatmentByName(String treatmentName) {
        List<Treatment> treatments = treatmentService.getTreatments();

        if (treatments != null) {
            return treatments.stream()
                    .filter(t -> t.getName().equalsIgnoreCase(treatmentName))
                    .findFirst()
                    .orElse(null); // or throw an exception if not found
        }
        return null;
    }



}
