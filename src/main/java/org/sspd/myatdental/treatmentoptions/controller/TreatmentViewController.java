package org.sspd.myatdental.treatmentoptions.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.App.App;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class TreatmentViewController  implements Initializable {

    @FXML
    private Button categorybtn;

    @FXML
    private TableColumn<?, ?> codeCol;

    @FXML
    private Label countlb;

    @FXML
    private TableColumn<?, ?> createCol;

    @FXML
    private Button editbtn;

    @FXML
    private TableColumn<?, ?> installCol;

    @FXML
    private Button newplanbtn;

    @FXML
    private Button newtreatmentbtn;

    @FXML
    private TableColumn<?, ?> noteCol;

    @FXML
    private TableColumn<?, ?> planCol;

    @FXML
    private TableColumn<?, ?> priceCol;

    @FXML
    private Button removebtn;

    @FXML
    private TableColumn<?, ?> statusCol;

    @FXML
    private TableColumn<?, ?> tenplateCol;

    @FXML
    private TableColumn<?, ?> totalCol;

    @FXML
    private TableView<?> treatitemlistable;

    @FXML
    private TableColumn<?, ?> treatmentCol;

    @FXML
    private TableView<?> treatmentplantable;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        categorybtn.setOnAction(event -> {

            openCategoryView();




        });

        newplanbtn.setOnAction(event -> {
            openAddPlanView();
        });

        editbtn.setOnAction(event -> {openEditPlanView();});

    }
    private void openCategoryView() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/treatmentslayout/treatmentcat.fxml"));
            fxmlLoader.setControllerFactory(App.context::getBean);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(removebtn.getScene().getWindow());
            stage.setTitle("Treatment Category");
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(event1 -> {



            });




        } catch (IOException e) {
            throw new RuntimeException("Failed to load Appointmenteditchooseview.fxml", e);
        }


    }

    private void openAddPlanView() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/treatmentslayout/treatmentplanview.fxml"));
            fxmlLoader.setControllerFactory(App.context::getBean);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(removebtn.getScene().getWindow());
            stage.setTitle("Add Treatment Plan");
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(event1 -> {



            });




        } catch (IOException e) {
            throw new RuntimeException("Failed to load Appointmenteditchooseview.fxml", e);
        }


    }

    private void openEditPlanView() {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/treatmentslayout/treatmentplaneditview.fxml"));
            fxmlLoader.setControllerFactory(App.context::getBean);
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.initStyle(StageStyle.UTILITY);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(removebtn.getScene().getWindow());
            stage.setTitle("Edit Treatment Plan");
            stage.setScene(scene);
            stage.show();

            stage.setOnCloseRequest(event1 -> {



            });




        } catch (IOException e) {
            throw new RuntimeException("Failed to load Appointmenteditchooseview.fxml", e);
        }


    }

}
