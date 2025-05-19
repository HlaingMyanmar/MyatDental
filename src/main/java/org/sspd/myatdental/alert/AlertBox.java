package org.sspd.myatdental.alert;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import org.springframework.stereotype.Component;

@Component
public  class AlertBox {


    public static void showInformationDialog(String title, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }

}
