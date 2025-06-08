package org.sspd.myatdental.treatmentoptions.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.treatmentoptions.model.TreatmentCategory;
import org.sspd.myatdental.treatmentoptions.service.TreatmentCategoryService;

import java.net.URL;
import java.util.ResourceBundle;

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

    }
}
