package com.AlbertoMrMekko.resat.service;

import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class NotificationService
{
    public void showErrorAlert(String errorTitle, String errorMessage)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("RESAT - Error");
        alert.setHeaderText(errorTitle);
        alert.setContentText(errorMessage);

        applyStyle(alert);

        alert.showAndWait();
    }

    public void showInfoAlert(String infoTitle, String infoMessage)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("RESAT - Info");
        alert.setHeaderText(infoTitle);
        alert.setContentText(infoMessage);

        applyStyle(alert);

        alert.showAndWait();
    }

    public void showWarningAlert(String warningTitle, String warningMessage)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("RESAT - Aviso");
        alert.setHeaderText(warningTitle);
        alert.setContentText(warningMessage);

        applyStyle(alert);

        alert.showAndWait();
    }

    public void showCriticalErrorAlert(String errorMessage)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR, errorMessage);

        alert.setTitle("RESAT - Error crítico");
        alert.setHeaderText("Error crítico");

        applyStyle(alert);

        alert.showAndWait();
        System.exit(1);
    }

    private void applyStyle(Alert alert)
    {
        DialogPane dialogPane = alert.getDialogPane();
        String css = Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm();
        dialogPane.getStylesheets().add(css);
    }
}
