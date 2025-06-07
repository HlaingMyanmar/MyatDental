package org.sspd.myatdental.useroptions;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.alert.AlertBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static org.sspd.myatdental.App.App.context;
import static org.sspd.myatdental.appointmentsoptions.controller.AppointmentDashboardController._username;

@Controller
public class LoginController implements Initializable {

    @FXML
    private Button loginbtn;

    @FXML
    private PasswordField passwordtxt;

    @FXML
    private ComboBox<Users> usernamecb;

    @FXML
    private VBox loginPane;

    private double xOffset = 0;
    private double yOffset = 0;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Populate usernamecb with Users enum
        usernamecb.getItems().addAll(Users.values());

        // Login button action
        loginbtn.setOnAction(event -> {
            Users selectedUser = usernamecb.getValue();
            String password = passwordtxt.getText();

            if (selectedUser == null) {
                AlertBox.showErrorDialog("အမှား", "အသုံးပြုသူအမည်ကို ရွေးပါ သို့မဟုတ် ရိုက်ထည့်ပါ။", "");
                return;
            }
            if (password.isEmpty()) {
                AlertBox.showErrorDialog("အမှား", "စကားဝှက်ကို ထည့်ပါ။", "");
                return;
            }

            // Check if the entered password matches the user's password
            if (password.equals(selectedUser.getPassword())) {



                _username =selectedUser.getDisplayName();


                Stage stage = new Stage();

                // Load FXML with Spring's ApplicationContext
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
                fxmlLoader.setControllerFactory(context::getBean); // Set before load()

                Scene scene = null;
                try {
                    scene = new Scene(fxmlLoader.load());


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                scene.setOnMousePressed(event1 -> {
                    xOffset = event1.getSceneX();
                    yOffset = event1.getSceneY();
                });

                scene.setOnMouseDragged(event2 -> {
                    stage.setX(event2.getScreenX() - xOffset);
                    stage.setY(event2.getScreenY() - yOffset);
                });

                // Configure stage
                stage.initStyle(StageStyle.UNDECORATED);
                stage.initModality(Modality.WINDOW_MODAL);
                Stage mainStage = (Stage) loginbtn.getScene().getWindow();
                stage.setTitle("ပင်မ စာမျက်နှာ");
                stage.setScene(scene);
                mainStage.close();
                stage.show();


            } else {
                AlertBox.showErrorDialog("အမှား", "အသုံးပြုသူအမည် သို့မဟုတ် စကားဝှက် မမှန်ကန်ပါ။", "");
            }
        });

        // Enable Enter key to trigger login
        passwordtxt.setOnAction(event -> loginbtn.fire());
    }


}
