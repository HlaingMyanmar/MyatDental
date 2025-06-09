package org.sspd.myatdental.treatmentinvoiceoptions.Controller;

import com.jfoenix.controls.JFXCheckBox;
import jakarta.validation.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.ErrorHandler.Validation.GenericValidator;
import org.sspd.myatdental.alert.AlertBox;
import org.sspd.myatdental.appointmentsoptions.model.Appointment;
import org.sspd.myatdental.treatmentinvoiceoptions.model.TreatmentInvoice;
import org.sspd.myatdental.treatmentinvoiceoptions.service.TreatmentInvoiceRecordService;
import org.sspd.myatdental.treatmentinvoiceoptions.service.TreatmentInvoiceService;
import org.sspd.myatdental.paymentoptions.model.Payment;
import org.sspd.myatdental.treatmentoptions.model.TreatRecordViewModel;
import org.sspd.myatdental.treatmentoptions.model.Treatment;
import org.sspd.myatdental.treatmentoptions.model.TreatmentRecord;
import org.sspd.myatdental.treatmentoptions.service.TreatmentService;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Controller
public class TreatmentInvoiceController implements Initializable {

    private final Payment payment;
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
    private Button delbtn;


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

    @FXML
    private Label countlb;

    @FXML
    private Label totallb;


    @FXML
    private TextField discountxt;

    @FXML
    private TextField payamountxt;

    @FXML
    private JFXCheckBox bankbox;

    @FXML
    private JFXCheckBox cashbox;

    @FXML
    private JFXCheckBox mobilebox;


    @FXML
    private Label finaltotallb;

    @FXML
    private Label discountlb;

    @FXML
    private Label grandtotallb;


    @FXML
    private Button submitbtn;


    @FXML
    private TextArea paymentNote;



    private final TreatmentService treatmentService;
    private final TreatmentInvoiceService invoiceService;
    private final TreatmentInvoiceRecordService treatmentInvoiceRecordService;
    private final Validator validator;

    private ObservableList<TreatmentRecord> treatments = FXCollections.observableArrayList();

    public TreatmentInvoiceController(Payment payment, TreatmentService treatmentService, TreatmentInvoiceService invoiceService, TreatmentInvoiceRecordService treatmentInvoiceRecordService, Validator validator) {
        this.payment = payment;
        this.treatmentService = treatmentService;
        this.invoiceService = invoiceService;
        this.treatmentInvoiceRecordService = treatmentInvoiceRecordService;
        this.validator = validator;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        treatments.clear();

        if (appointment != null) {
            appointmentcodelb.setText(String.valueOf(appointment.getAppointment_id()));
            patientNamelb.setText(appointment.getPatient() != null ? appointment.getPatient().getName() : "Unknown");
        } else {
            appointmentcodelb.setText("N/A");
            patientNamelb.setText("No appointment selected");
        }
        fillTreatmentComboBox();

        actionEvent();

        initializeTable();

        setupPaymentCheckBoxes();

        finaltotallb.setText(String.valueOf(updateFinalTotal()));






    }

    private void initializeTable() {
        treatmentCol.setCellValueFactory(new PropertyValueFactory<>("treatmentName"));
        tnumCol.setCellValueFactory(new PropertyValueFactory<>("toothNumber"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : new DecimalFormat("#,##0.00").format(item));
            }
        });
        noteCol.setCellValueFactory(new PropertyValueFactory<>("notes"));
        recordtable.setItems(loadTreatmentRecords());
    }

    private void setupPaymentCheckBoxes() {
        // Ensure only one checkbox can be selected at a time
        bankbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                cashbox.setSelected(false);
                mobilebox.setSelected(false);
            }
        });
        cashbox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                bankbox.setSelected(false);
                mobilebox.setSelected(false);
            }
        });
        mobilebox.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                bankbox.setSelected(false);
                cashbox.setSelected(false);
            }
        });
    }

    private void actionEvent() {

        addbtn.setOnAction(event -> {
            String toothNum = toothnumtxt.getText();
            String note = notetxt.getText() != null ? notetxt.getText() : "";
            String treatmentName = treatmentComboBox.getValue();

            // Validate inputs
            if (appointment == null) {
                AlertBox.showErrorDialog("Add Treatment", "Invalid Appointment", "No appointment selected.");
                return;
            }
            if (treatmentName == null || treatmentName.trim().isEmpty()) {
                AlertBox.showErrorDialog("Add Treatment", "Invalid Treatment", "Please select a treatment.");
                return;
            }
            if (toothNum == null || toothNum.trim().isEmpty()) {
                AlertBox.showErrorDialog("Add Treatment", "Invalid Tooth Number", "Please enter a valid tooth number.");
                return;
            }

            Treatment treatment = getTreatmentByName(treatmentName);
            if (treatment == null) {
                AlertBox.showErrorDialog("Add Treatment", "Treatment Not Found", "Selected treatment does not exist.");
                return;
            }

            // Create and save TreatmentRecord
            TreatmentRecord record = new TreatmentRecord(appointment, treatment, toothNum, note, treatment.getStandard_price());
            treatments.add(record);

            recordtable.setItems(loadTreatmentRecords());
            setCount(countlb);
            setTotal(totallb);
            setTotal(finaltotallb);
            grandtotallb.setText(String.valueOf(updateFinalTotal()));
            clearFields();
        });

        delbtn.setOnAction(event -> {
            TreatRecordViewModel viewModel = recordtable.getSelectionModel().getSelectedItem();
            if (viewModel == null) {
                AlertBox.showWarningDialog("Delete", "No Selection", "Please select a treatment record to delete.");
                return;
            }

                recordtable.getItems().remove(viewModel);
            treatments.stream()
                    .filter(t -> t.getTreatment().getName().equals(viewModel.getTreatmentName()) &&
                            t.getToothNumber().equals(viewModel.getToothNumber()))
                    .findFirst().ifPresent(toDelete -> treatments.remove(toDelete));
            setCount(countlb);
                setTotal(totallb);
            setTotal(finaltotallb);
            grandtotallb.setText(String.valueOf(updateFinalTotal()));

        });

        setupDiscountListener();

        setupPayListener();

        submitbtn.setOnAction(event -> {

            if (treatments == null || treatments.isEmpty()) {
                AlertBox.showErrorDialog("Submit Payment", "No Treatments", "Cannot submit payment without treatment records.");
                return;
            }

            String payText = payamountxt.getText();
            if (payText == null || payText.trim().isEmpty()) {
                AlertBox.showErrorDialog("Submit Payment", "Invalid Payment Amount", "Please enter a valid payment amount.");
                return;
            }

            double paymentAmount;
            try {
                paymentAmount = Double.parseDouble(payText.trim());
                if (paymentAmount <= 0.0) {
                    AlertBox.showErrorDialog("Submit Payment", "Invalid Payment Amount", "Payment amount must be greater than zero.");
                    return;
                }
            } catch (NumberFormatException e) {
                AlertBox.showErrorDialog("Submit Payment", "Invalid Input", "Please enter a valid number for payment amount.");
                return;
            }

            if (!bankbox.isSelected() && !cashbox.isSelected() && !mobilebox.isSelected()) {
                AlertBox.showErrorDialog("Submit Payment", "No Payment Method", "Please select a payment method.");
                return;
            }

            Payment.PaymentMethod paymentMethod = bankbox.isSelected() ? Payment.PaymentMethod.Bank_Transfer :
                    cashbox.isSelected() ?Payment.PaymentMethod.Cash :
                            mobilebox.isSelected() ? Payment.PaymentMethod.Mobile_Money : null;

            double payment = Double.parseDouble(grandtotallb.getText().trim());

            String grandTotalText = grandtotallb.getText();
            if (grandTotalText == null || grandTotalText.trim().isEmpty()) {
                return;
            }

            double grandTotal = Double.parseDouble(grandTotalText.trim());

            // Treatment Record List
         //   treatments




            int discount  = (int) Double.parseDouble(discountlb.getText());
            double totalamount  = Double.parseDouble(finaltotallb.getText());
            double balance =grandTotal -  paymentAmount;
            TreatmentInvoice.InvoiceStatus status ;

            if(balance==0.0){
               status = TreatmentInvoice.InvoiceStatus.Paid;

            }
            else  if (paymentAmount>grandTotal){
                status = TreatmentInvoice.InvoiceStatus.Partially_Paid;
            }
            else {
                status = TreatmentInvoice.InvoiceStatus.Unpaid;
            }

            TreatmentInvoice ti = new TreatmentInvoice(discount,totalamount,balance,status,appointment.getPatient(),appointment);


            String paymentnote = paymentNote.getText();
            LocalDate date = LocalDate.now();


            Payment p1 = new Payment(paymentAmount,date,paymentMethod,paymentnote,ti);

            boolean tresult = false;
            for(TreatmentRecord t :treatments){

              tresult   = new GenericValidator<TreatmentRecord>(validator).validate(t);

            }

            boolean tinvoiceresult = new GenericValidator<TreatmentInvoice>(validator).validate(ti);

            boolean paymentresult = new GenericValidator<Payment>(validator).validate(p1);

            if(tresult && tinvoiceresult && paymentresult){


                if(treatmentInvoiceRecordService.addTreatmentRecordInvoicePayment(treatments, ti, p1)){


                    AlertBox.showInformationDialog("Treatment Record", "Treatment Record", "Treatment Record added successfully.");


                }
                else {
                    AlertBox.showErrorDialog("Treatment Record", "Treatment Record", "Treatment Record already added.");
                }


            }






        });

    }




    private void setupDiscountListener() {
        discountxt.textProperty().addListener((obs, oldValue, newValue) -> {
            grandtotallb.setText(String.valueOf(updateFinalTotal()));
            discountlb.setText(String.valueOf(Double.parseDouble(discountxt.getText())));
        });
    }

    private void setupPayListener() {
        payamountxt.textProperty().addListener((obs, oldValue, newValue) -> {
            try {
                // Ensure the new value is not empty or invalid
                if (newValue == null || newValue.trim().isEmpty()) {
                    return; // Ignore empty input
                }

                double payment = Double.parseDouble(newValue.trim());

                String grandTotalText = grandtotallb.getText();
                if (grandTotalText == null || grandTotalText.trim().isEmpty()) {
                    return;
                }

                double grandTotal = Double.parseDouble(grandTotalText.trim());

                if (payment > grandTotal) {
                    AlertBox.showErrorDialog("Payment Invalid", "Payment", "Payment amount cannot exceed total.");
                    payamountxt.setText(oldValue); // Revert to previous valid value
                }

            } catch (NumberFormatException e) {
                // Optional: prevent non-numeric values
                payamountxt.setText(oldValue); // Revert to previous valid value
            }
        });
    }

    private ObservableList<TreatRecordViewModel> loadTreatmentRecords() {
        ObservableList<TreatRecordViewModel> treatmentView = FXCollections.observableArrayList();
        treatments.forEach(treatment -> {
            if (treatment.getTreatment() != null) {
                TreatRecordViewModel viewModel = new TreatRecordViewModel(
                        treatment.getToothNumber(),
                        treatment.getNotes(),
                        treatment.getPrice() != 0 ? treatment.getPrice() : 0.0,
                        treatment.getTreatment().getName()
                );
                treatmentView.add(viewModel);
            }
        });
        return treatmentView;
    }

    private void clearFields(){

        toothnumtxt.setText("");
        notetxt.setText("");
        treatmentComboBox.getItems().clear();
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

    private void setCount(Label count) {
        if (count != null) {
            count.setText(String.valueOf(treatments.size()));
        } else {
            AlertBox.showWarningDialog("UI Error", "Label Missing", "Count label is not initialized.");
        }
    }

    private void setTotal(Label totallb) {
        double total = treatments.stream()
                .map(TreatmentRecord::getPrice)
                .reduce(0.0, Double::sum);

        totallb.setText(String.valueOf(total));

    }

    private double updateFinalTotal() {
        String totalText = totallb.getText();
        String discountText = discountxt.getText();

        double treatmentTotal = (totalText == null || totalText.isEmpty()) ? 0.0 : Double.parseDouble(totalText);
        double discount = (discountText == null || discountText.isEmpty()) ? 0.0 : Double.parseDouble(discountText);

        return treatmentTotal - discount;
    }


    private boolean isValidToothNumber(String toothNum) {
        // Example: Validate tooth number (customize based on dental standards)
        return toothNum.matches("\\d{1,2}|[UR|UL|LR|LL]\\d{1,2}");
    }



}
