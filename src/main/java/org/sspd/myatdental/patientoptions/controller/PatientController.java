package org.sspd.myatdental.patientoptions.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;


@Controller
public class PatientController {

    @FXML
    private TableColumn<?, ?> actionCol;

    @FXML
    private Button addpatientbtn;

    @FXML
    private TableColumn<?, ?> ageCol;

    @FXML
    private TableColumn<?, ?> dobCol;

    @FXML
    private Button filterbtn;

    @FXML
    private TableColumn<?, ?> genderCol;

    @FXML
    private ComboBox<?> genderbox;

    @FXML
    private TableColumn<?, ?> mhCol;

    @FXML
    private TableColumn<?, ?> patientCol;

    @FXML
    private TableView<?> patienttable;

    @FXML
    private TableColumn<?, ?> phoneCol;

    @FXML
    private TextField searchtxt;

    @FXML
    private TableColumn<?, ?> statusCol;

    @FXML
    private ComboBox<?> townshopbox;

    @FXML
    private TableColumn<?, ?> tsCol;

}
