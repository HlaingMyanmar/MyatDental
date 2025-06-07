package org.sspd.myatdental.App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class Dashboard implements Initializable {

    @FXML
    private Button dentistbtn;

    @FXML
    private AnchorPane switchPane;

    @FXML
    private Button treatmentbtn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        dentistbtn.setOnAction(e -> {
            loadDentistView();
        });

        treatmentbtn.setOnAction(e -> {
            loadTreatmentView();
        });

    }
    private void loadDentistView() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/dentistlayout/dentailregister.fxml"));
            fxmlLoader.setControllerFactory(App.context::getBean);
            Parent dentistView = fxmlLoader.load();

            // Clear existing content in switchPane
            switchPane.getChildren().clear();

            // Add dentistView to switchPane
            switchPane.getChildren().add(dentistView);

            // Optional: anchor it to all sides if it is resizable and supports anchoring
            AnchorPane.setTopAnchor(dentistView, 0.0);
            AnchorPane.setBottomAnchor(dentistView, 0.0);
            AnchorPane.setLeftAnchor(dentistView, 0.0);
            AnchorPane.setRightAnchor(dentistView, 0.0);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load dentailregister.fxml", e);
        }
    }

    private void loadTreatmentView() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/treatmentslayout/treatmentregister.fxml"));
            fxmlLoader.setControllerFactory(App.context::getBean);
            Parent treatmentView = fxmlLoader.load();

            // Clear existing content in switchPane
            switchPane.getChildren().clear();

            // Add treatmentView to switchPane
            switchPane.getChildren().add(treatmentView);

            // Optional: anchor it to all sides if it is resizable and supports anchoring
            AnchorPane.setTopAnchor(treatmentView, 0.0);
            AnchorPane.setBottomAnchor(treatmentView, 0.0);
            AnchorPane.setLeftAnchor(treatmentView, 0.0);
            AnchorPane.setRightAnchor(treatmentView, 0.0);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load treatmentregister.fxml", e);
        }
    }





}
