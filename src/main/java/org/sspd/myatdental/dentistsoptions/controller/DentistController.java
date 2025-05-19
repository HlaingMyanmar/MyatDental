package org.sspd.myatdental.dentistsoptions.controller;

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
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.ErrorHandler.Validation.GenericValidator;
import org.sspd.myatdental.dentistsoptions.model.Dentist;
import org.sspd.myatdental.dentistsoptions.service.DentistServices;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;

import static org.sspd.myatdental.alert.AlertBox.showInformationDialog;

@Controller
public class DentistController implements Initializable {

    @FXML
    private Label countxt;

    @FXML
    private Button deletebtn;

    @FXML
    private TableView<Dentist> dentailtable;

    @FXML
    private TableColumn<Dentist, String> emailCol;

    @FXML
    private TextField emailtxt;

    @FXML
    private TableColumn<Dentist, String> nameCol;

    @FXML
    private TextField nametxt;

    @FXML
    private TableColumn<Dentist, String> phoneCol;

    @FXML
    private TextField phonetxt;

    @FXML
    private Button savebtn;

    @FXML
    private TextField searchtxt;

    @FXML
    private TableColumn<Dentist, String> specializationCol;

    @FXML
    private TextField specializationtxt;

    @FXML
    private BorderPane mainPane;

    private final DentistServices dentistServices;

    private final GenericValidator<Dentist> genericValidator;

    private final Validator validator;


    public DentistController(DentistServices dentistServices, GenericValidator<Dentist> genericValidator, Validator validator) {
        this.dentistServices = dentistServices;
        this.genericValidator = genericValidator;
        this.validator = validator;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dentistInitializable();
        getCURDAction();
        dentailtable.setEditable(true);
        dentailtable.setItems(filterData());
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

    private void addTextFieldListener(TextField textField, FilteredList<Dentist> filteredData) {

        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(dentist -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                if (dentist.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (dentist.getSpecialization().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (dentist.getPhone().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (dentist.getEmail().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false;
            });
        });

    }

    private void initializeColumns() {
        setCellFactoryColumn(nameCol, "name");
        setCellFactoryColumn(phoneCol, "phone");
        setCellFactoryColumn(specializationCol, "specialization");
        setCellFactoryColumn(emailCol, "email");
    }

    private void setCellFactoryColumn(TableColumn<Dentist, String> column, String fieldName) {
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(event -> {
            String value = String.valueOf(event.getNewValue());

            if (value != null && !value.isEmpty()) {

                Dentist dentist= dentailtable.getSelectionModel().getSelectedItem();

                dentist.setDentist_id(getDentistID(dentist.getName(),dentist.getEmail()));
                updateField(event.getRowValue(), fieldName, value);

                if (dentistServices.updateDentist(dentist)) {
                    showInformationDialog("Data Update","Dentist","Dentist Data Update Successful");

                }
            }
        });
    }

    private void updateField(Dentist dentist, String fieldName, String value) {
        try {
            Field field = Dentist.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(dentist, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }



    private ObservableList<Dentist> filterData(){

        ObservableList<Dentist> observableList = FXCollections.observableArrayList(dentistServices.getDentists());

        FilteredList<Dentist> filteredData = new FilteredList<>(observableList, b -> true);

        addTextFieldListener(searchtxt, filteredData);

        SortedList<Dentist> sortedData = new SortedList<>(filteredData);

        sortedData.comparatorProperty().bind(dentailtable.comparatorProperty());

        return sortedData;

        //dentailtable.setItems(sortedData);

    }

    private void saveAction() {

        String name =  nametxt.getText();
        String specialization =  specializationtxt.getText();
        String phone =  phonetxt.getText();
        String email =  emailtxt.getText();

        Dentist dentist = new Dentist(name, specialization, phone, email);

        boolean validatecheck = new GenericValidator<Dentist>(validator).validate(dentist);


        if (validatecheck) {

            boolean result =  dentistServices.addDentist(dentist);


            if(result) {
                showInformationDialog("Data Insert","Dentist","Dentist Data Insert Successful");
                dentailtable.setItems(filterData());
                countxt.setText(String.valueOf(filterData().size()));
                setClear();
            }

        }

    }

    private void deleteAction() {

       Dentist dentist =  dentailtable.getSelectionModel().getSelectedItem();

       boolean result = dentistServices.deleteById(getDentistID(dentist.getName(), dentist.getEmail()));

        if(result) {
            showInformationDialog("Data Remove","Dentist","Dentist Data Remove Successful");
            dentailtable.setItems(filterData());
            countxt.setText(String.valueOf(filterData().size()));
            setClear();
        }


    }

    private int getDentistID (String name , String email){
        return getDentist().stream()
                .filter(dentist -> dentist.getName().equals(name) && dentist.getEmail().equals(email))
                .map(Dentist::getDentist_id)
                .findFirst().orElse(-1);

    }

    private void dentistInitializable(){

        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        specializationCol.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

    }

    private void setClear(){
        nametxt.setText("");
        specializationtxt.setText("");
        emailtxt.setText("");
        phonetxt.setText("");
    }

    private ObservableList<Dentist> getDentist(){

        return FXCollections.observableArrayList(dentistServices.getDentists());

    }

    private int dentistCount(){
        return  dentailtable.getItems().size();
    }

}
