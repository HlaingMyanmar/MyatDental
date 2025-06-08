package org.sspd.myatdental.treatmentoptions.controller;

import com.jfoenix.controls.JFXCheckBox;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

@Controller
public class TreatmentPlanEditController {

    @FXML
    private Button addtreatmentbtn;

    @FXML
    private JFXCheckBox isinstallmentscheck;

    @FXML
    private JFXCheckBox istemplatecheck;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private TableColumn<?, ?> noteCol;

    @FXML
    private ComboBox<?> patientbox;

    @FXML
    private TextField plantitletxt;

    @FXML
    private TableColumn<?, ?> priceCol;

    @FXML
    private TextField totalcost;

    @FXML
    private ComboBox<?> treatmentbox;

    @FXML
    private TableView<?> treatmentplantable;

}
