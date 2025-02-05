package com.AlbertoMrMekko.resat.service;

import javafx.scene.control.Alert;
import org.springframework.stereotype.Service;

@Service
public class NotificationService
{
    public void showErrorAlert(String errorTitle, String errorMessage)
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        alert.setTitle("RESAT - Error");
        alert.setHeaderText(errorTitle);
        alert.setContentText(errorMessage);

        alert.showAndWait();
    }

    public void showInfoAlert(String infoTitle, String infoMessage)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("RESAT - Info");
        alert.setHeaderText(infoTitle);
        alert.setContentText(infoMessage);

        alert.showAndWait();
    }

    public void showWarningAlert(String warningTitle, String warningMessage)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);

        alert.setTitle("RESAT - Aviso");
        alert.setHeaderText(warningTitle);
        alert.setContentText(warningMessage);

        alert.showAndWait();
    }
}
