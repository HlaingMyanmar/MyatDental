package org.sspd.myatdental.ErrorHandler.Validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GenericValidator<T> {  // Renamed for clarity
    @Autowired
    private final Validator validator;


    public GenericValidator(Validator validator) {
        this.validator = validator;
    }

    public boolean validate(T objectToValidate) {
        Set<ConstraintViolation<T>> violations = validator.validate(objectToValidate);

        if (!violations.isEmpty()) {
            String errorMessages = violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("\n\n"));

            showErrorDialog("Error","Please Fill Required Data",errorMessages);
            //throw new ValidationException("Validation failed:\n" + errorMessages);

            return false;
        }

        return true;
    }


    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {
            super(message);
        }
    }

    private void showErrorDialog(String title, String header, String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);
            alert.showAndWait();
        });
    }



}