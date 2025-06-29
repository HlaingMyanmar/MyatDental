package org.sspd.myatdental.treatmentoptions.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.alert.AlertBox;
import org.sspd.myatdental.treatmentoptions.model.Treatment;
import org.sspd.myatdental.treatmentoptions.model.TreatmentCategory;
import org.sspd.myatdental.treatmentoptions.service.TreatmentCategoryService;

import java.lang.reflect.Field;
import java.net.URL;
import java.util.ResourceBundle;

import static org.sspd.myatdental.alert.AlertBox.showInformationDialog;

@Controller
public class TreatmentCategoryController implements Initializable {

    @FXML
    private Button addbtn;

    @FXML
    private TableView<TreatmentCategory> cattable;

    @FXML
    private TableColumn<TreatmentCategory, Integer> codeCol;

    @FXML
    private TableColumn<TreatmentCategory, String> nameCol;

    @FXML
    private TextField nametxt;


    private TreatmentCategoryService treatmentCategoryService;


    public TreatmentCategoryController(TreatmentCategoryService treatmentCategoryService) {
        this.treatmentCategoryService = treatmentCategoryService;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {



        tableIni();

        cattable.setItems(getDataList());

        addbtn.setOnAction(event -> {

           String name = nametxt.getText();


           if(name!=null){

               TreatmentCategory tc = new TreatmentCategory();
               tc.setName(name);
               boolean result = treatmentCategoryService.saveTreatmentCategory(tc);

               if(result){
                   AlertBox.showInformationDialog("Treatment Options","Treatment Category", "Successful Operation");
                   cattable.setItems(getDataList());
                   nametxt.setText("");

               }

           }








        });

        cattable.setOnKeyPressed(event -> {

            TreatmentCategory tc = cattable.getSelectionModel().getSelectedItem();

                if(event.getCode() == KeyCode.DELETE){

                    if(tc!=null){

                        boolean result = treatmentCategoryService.deleteTreatmentCategory(tc);

                        if(result){

                            AlertBox.showInformationDialog("Treatment Options","Treatment Category", "Successful Operation");
                            cattable.getItems().clear();
                            cattable.setItems(getDataList());
                        }
                    }

                }

        });


        cattable.setEditable(true);

        initializeColumns();


    }

    private void tableIni(){

        codeCol.setCellValueFactory(new PropertyValueFactory<>("category_id"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

    }

    private ObservableList getDataList(){

        return FXCollections.observableArrayList(treatmentCategoryService.getAllTreatmentCategories());

    }

    private void initializeColumns() {
        setCellFactoryColumn(nameCol, "Name");



    }

    private void setCellFactoryColumn(TableColumn<TreatmentCategory, String> column, String fieldName) {
        column.setCellFactory(TextFieldTableCell.forTableColumn());
        column.setOnEditCommit(event -> {
            String value = event.getNewValue();
            if (value != null && !value.isEmpty()) {
               TreatmentCategory treatment = cattable.getSelectionModel().getSelectedItem();
               treatment.setName(value);
                updateField(treatment, fieldName, value);

                if (treatmentCategoryService.updateTreatmentCategory(treatment)) {
                    showInformationDialog("Data Update", "Treatment Category", "Treatment Category Data Update Successful");
                }
            }
        });
    }
    private void updateField(TreatmentCategory treatment, String fieldName, Object value) {
        try {
            Field field = Treatment.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(treatment, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }







}
