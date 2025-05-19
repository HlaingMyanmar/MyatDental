package org.sspd.myatdental.alert;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.stage.Modality;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

@Component
public class AlertBox {

    // Resource bundle for localization, using system default locale
    private static ResourceBundle messages = ResourceBundle.getBundle("alertstyle.messages", Locale.getDefault());

    // Set locale dynamically
    public static void setLocale(String languageTag) {
        Locale locale = Locale.forLanguageTag(languageTag); // e.g., "my" or "en"
        messages = ResourceBundle.getBundle("alertstyle.messages", locale);
    }

    // Helper method to style the alert with CSS and icon
    private static void styleAlert(Alert alert, FontAwesomeIconView icon) {
        DialogPane dialogPane = alert.getDialogPane();
        String cssPath = "/style/alert.css";
        try {
            dialogPane.getStylesheets().add(AlertBox.class.getResource(cssPath).toExternalForm());
        } catch (NullPointerException e) {
            System.err.println("Error: Could not load alert.css at " + cssPath);
            dialogPane.setStyle("-fx-background-color: #ffffff; -fx-padding: 20px; -fx-border-radius: 12px; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.25), 12, 0, 0, 3);");
        }
        dialogPane.setGraphic(icon);
        dialogPane.setPrefWidth(500);
        dialogPane.setMinWidth(400);
        dialogPane.setMaxWidth(600);
    }

    // Customize button styles
    private static void customizeButtons(Alert alert, String customStyleClass) {
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getButtonTypes().forEach(buttonType -> {
            Button button = (Button) dialogPane.lookupButton(buttonType);
            button.getStyleClass().add(customStyleClass);
        });
    }

    // Show Information Dialog
    public static void showInformationDialog(String title, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.OK);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle(getLocalizedText(messages.getString("warning.title")));
            alert.setHeaderText(getLocalizedText(header));
            alert.setContentText(getLocalizedText(content));

            FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.INFO_CIRCLE);
            icon.setStyle("-fx-fill: #3498db;");
            icon.setSize("24");

            styleAlert(alert, icon);
            customizeButtons(alert, "custom-button");
            alert.showAndWait();
        });
    }

    // Show Error Dialog
    public static void showErrorDialog(String title, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, content, ButtonType.OK);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle(getLocalizedText(title));
            alert.setHeaderText(getLocalizedText(header));
            alert.setContentText(getLocalizedText(content));

            FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_TRIANGLE);
            icon.setStyle("-fx-fill: #e74c3c;");
            icon.setSize("24");

            styleAlert(alert, icon);
            customizeButtons(alert, "custom-button");
            alert.showAndWait();
        });
    }

    // Show Warning Dialog
    public static void showWarningDialog(String title, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.WARNING, content, ButtonType.OK);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle(getLocalizedText(title));
            alert.setHeaderText(getLocalizedText(header));
            alert.setContentText(getLocalizedText(content));

            FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.EXCLAMATION_CIRCLE);
            icon.setStyle("-fx-fill: #f1c40f;");
            icon.setSize("24");

            styleAlert(alert, icon);
            customizeButtons(alert, "custom-button");
            alert.showAndWait();
        });
    }



    // Helper method for localization
    private static String getLocalizedText(String key) {
        try {
            return messages.getString(key);
        } catch (Exception e) {
            return key; // Fallback to key if translation is missing
        }
    }
}