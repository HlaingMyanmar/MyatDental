package org.sspd.myatdental.useroptions;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.alert.AlertBox;

import java.net.URL;
import java.util.ResourceBundle;

@Controller
public class LoginController implements Initializable {

    @FXML
    private Button loginbtn;

    @FXML
    private PasswordField passwordtxt;

    @FXML
    private ComboBox<Users> usernamecb;

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
                AlertBox.showInformationDialog("အောင်မြင်ပါပြီ", "အကောင့်ဝင်ခြင်း အောင်မြင်ပါသည်: " + selectedUser.getDisplayName(), "");
                // TODO: Navigate to main application or dashboard
            } else {
                AlertBox.showErrorDialog("အမှား", "အသုံးပြုသူအမည် သို့မဟုတ် စကားဝှက် မမှန်ကန်ပါ။", "");
            }
        });

        // Enable Enter key to trigger login
        passwordtxt.setOnAction(event -> loginbtn.fire());
    }
}
