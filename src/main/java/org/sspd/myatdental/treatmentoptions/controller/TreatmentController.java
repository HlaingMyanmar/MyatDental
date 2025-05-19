package org.sspd.myatdental.treatmentoptions.controller;

import jakarta.validation.Validator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.util.converter.DoubleStringConverter;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.ErrorHandler.Validation.GenericValidator;
import org.sspd.myatdental.dentistsoptions.model.Dentist;
import org.sspd.myatdental.treatmentoptions.model.Treatment;
import org.sspd.myatdental.treatmentoptions.service.TreatmentService;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;

import static org.sspd.myatdental.alert.AlertBox.showErrorDialog;
import static org.sspd.myatdental.alert.AlertBox.showInformationDialog;

@Controller
public class TreatmentController implements Initializable {

    @FXML
    private Label countxt;

    @FXML
    private Button deletebtn;

    @FXML
    private TableColumn<Treatment, String> descCol;

    @FXML
    private TextField desctxt;

    @FXML
    private TableColumn<Treatment, String> nameCol;

    @FXML
    private TextField nametxt;

    @FXML
    private TableColumn<Treatment, Double> priceCol;

    @FXML
    private TextField pricetxt;

    @FXML
    private Button savebtn;

    @FXML
    private TextField searchtxt;


    @FXML
    private BorderPane mainPane;

    @FXML
    private TableView<Treatment> treatmenttable;

    private final TreatmentService treatmentService;

    private final GenericValidator<Dentist> genericValidator;

    private final Validator validator;

    public TreatmentController(TreatmentService treatmentService, GenericValidator<Dentist> genericValidator, Validator validator) {
        this.treatmentService = treatmentService;
        this.genericValidator = genericValidator;
        this.validator = validator;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        treatmentInitializable();
        getCURDAction();
        treatmenttable.setEditable(true);
        treatmenttable.setItems(filterData());
        countxt.setText(String.valueOf(filterData().size()));

    }

    private void getCURDAction() {

        savebtn.setOnAction(event -> {

            saveAction();

        });

        deletebtn.setOnAction(event -> {
            deleteAction();
        });

        mainPane.setOnKeyPressed(event -> {
            if (event.isControlDown()) {
                if(event.getCode() == KeyCode.S){
                    saveAction();

                }
            }

            if (event.getCode() == KeyCode.DELETE) {
                deleteAction();
            }

        });

        initializeColumns();


    }

    private void initializeColumns() {
        setCellFactoryColumn(nameCol, "name");
        setCellFactoryColumn(descCol, "description");
        setCellFactoryColumnn(priceCol, "standard_price");

    }

    private void setCellFactoryColumn(TableColumn<Treatment, String> column, String fieldName) {
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(event -> {
            String value = event.getNewValue();
            if (value != null && !value.isEmpty()) {
                Treatment treatment = treatmenttable.getSelectionModel().getSelectedItem();
                treatment.setTreatment_id(getTreatmentID(treatment.getName(), treatment.getStandard_price()));
                updateField(treatment, fieldName, value);

                if (treatmentService.updateTreatment(treatment)) {
                    showInformationDialog("Data Update", "Treatment", "Treatment Data Update Successful");
                }
            }
        });
    }

    private void setCellFactoryColumnn(TableColumn<Treatment, Double> column, String fieldName) {
        column.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        column.setOnEditCommit(event -> {
            Double value = event.getNewValue();
            if (value != null) {
                Treatment treatment = treatmenttable.getSelectionModel().getSelectedItem();
                treatment.setTreatment_id(getTreatmentID(treatment.getName(), treatment.getStandard_price()));
                updateField(treatment, fieldName, value);

                if (treatmentService.updateTreatment(treatment)) {
                    showInformationDialog("Data Update", "Treatment", "Treatment Data Update Successful");
                }
            }
        });
    }

    private void updateField(Treatment treatment, String fieldName, Object value) {
        try {
            Field field = Treatment.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(treatment, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void addTextFieldListener(TextField textField, FilteredList<Treatment> filteredData) {

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(treatment -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (treatment.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (treatment.getDescription().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(treatment.getStandard_price()).toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

    }

    private ObservableList<Treatment> filterData(){

        ObservableList<Treatment> observableList = FXCollections.observableArrayList(treatmentService.getTreatments());

        FilteredList<Treatment> filteredData = new FilteredList<>(observableList, b -> true);

        addTextFieldListener(searchtxt, filteredData);

        SortedList<Treatment> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(treatmenttable.comparatorProperty());

        return sortedData;

        //dentailtable.setItems(sortedData);

    }

    private void saveAction() {

        String name =  nametxt.getText();
        String desc =  desctxt.getText();
        double price = 0;
        if(pricetxt.getText().isEmpty()){

            showErrorDialog("Treatment","Notice","Please Fill Standard Price");return;
        }
        else {
            price =  Double.parseDouble(pricetxt.getText());
        }



       Treatment treatment = new Treatment(name, desc, price);

        boolean validatecheck = new GenericValidator<Treatment>(validator).validate(treatment);


        if (validatecheck) {

            boolean result = treatmentService.addTreatment(treatment);


            if(result) {
                showInformationDialog("Data Insert","Treatment","treatment Data Insert Successful");
                treatmenttable.setItems(filterData());
                countxt.setText(String.valueOf(filterData().size()));
                setClear();
            }

        }

    }

    private void deleteAction() {

        Treatment treatment=  treatmenttable.getSelectionModel().getSelectedItem();

        boolean result = treatmentService.deleteById(getTreatmentID(treatment.getName(), treatment.getStandard_price()));

        if(result) {
            showInformationDialog("Data Remove","Treatment","Treatment Data Remove Successful");
            treatmenttable.setItems(filterData());
            countxt.setText(String.valueOf(filterData().size()));
            setClear();
        }


    }

    private ObservableList<Treatment> getTreatments() {

        return FXCollections.observableArrayList(treatmentService.getTreatments());

    }

    private int getTreatmentID (String name , Double standard_price) {
        return getTreatments().stream()
                .filter(treatment -> treatment.getName().equals(name) && treatment.getStandard_price()==standard_price)
                .map(Treatment::getTreatment_id)
                .findFirst().orElse(-1);

    }

    private void treatmentInitializable(){

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("standard_price"));


    }

    private void setClear(){
        nametxt.setText("");
        desctxt.setText("");
        pricetxt.setText("");

    }
}
