package org.sspd.myatdental.appointmentsoptions.controller;

import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import org.springframework.stereotype.Controller;
import org.sspd.myatdental.appointmentsoptions.model.Appointment;

@Controller
public class AppMainController {

    @FXML
    private TableColumn<Appointment, Void> actionCol; // Replace Appointment with your model class
    @FXML
    private TableView<Appointment> appdashboard; // Replace Appointment with your model class



    @FXML
    public void initialize() {

        actionCol.setCellFactory(param -> new TableCell<>() {
            private final JFXButton editButton = new JFXButton();
            private final JFXButton deleteButton = new JFXButton();

            {
                // Set FontAwesome icons for Edit and Delete buttons
                FontAwesomeIconView editIcon = new FontAwesomeIconView(FontAwesomeIcon.PENCIL);
                FontAwesomeIconView deleteIcon = new FontAwesomeIconView(FontAwesomeIcon.TRASH);

                // Optional: Set icon size and color
                editIcon.setSize("16px");
                deleteIcon.setSize("16px");
                editIcon.setStyle("-fx-fill: blue;");
                deleteIcon.setStyle("-fx-fill: red;");

                editButton.setGraphic(editIcon);
                deleteButton.setGraphic(deleteIcon);

                // Optional: Add tooltips
                editButton.setTooltip(new javafx.scene.control.Tooltip("Edit Appointment"));
                deleteButton.setTooltip(new javafx.scene.control.Tooltip("Delete Appointment"));

                // Define button actions
                editButton.setOnAction(event -> {
                    Appointment appointment = getTableView().getItems().get(getIndex());
                    handleEditAction(appointment);
                });

                deleteButton.setOnAction(event -> {
                    Appointment appointment = getTableView().getItems().get(getIndex());
                    handleDeleteAction(appointment);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    // Display buttons in an HBox
                    HBox hbox = new HBox(5, editButton, deleteButton);
                    setGraphic(hbox);
                }
            }
        });
    }

    // Example method to handle Edit action
    private void handleEditAction(Appointment appointment) {
        System.out.println("Editing appointment: " + appointment);
        // Add your edit logic here (e.g., open a dialog to edit the appointment)
    }

    // Example method to handle Delete action
    private void handleDeleteAction(Appointment appointment) {
        System.out.println("Deleting appointment: " + appointment);
        // Add your delete logic here (e.g., remove from database and update TableView)
        appdashboard.getItems().remove(appointment);
    }
}