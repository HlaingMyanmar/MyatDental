package org.sspd.myatdental.treatmentoptions.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class TreatmentCategoryController implements Initializable {

    @FXML
    private Button addbtn;

    @FXML
    private TableView<?> cattable;

    @FXML
    private TableColumn<?, ?> codeCol;

    @FXML
    private TableColumn<?, ?> nameCol;

    @FXML
    private TextField nametxt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
}
