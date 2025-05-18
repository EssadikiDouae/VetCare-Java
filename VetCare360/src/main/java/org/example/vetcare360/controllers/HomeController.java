package org.example.vetcare360.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;
import org.example.vetcare360.utils.SceneSwitcher;

public class HomeController {

    @FXML
    private VBox rootVBox;

    @FXML
    public void initialize() {
        try {
            rootVBox.getStylesheets().add(getClass().getResource("/org/example/vetcare360/css/style.css").toExternalForm());
        } catch (Exception e) {
            showError(
                    "Erreur lors du chargement du style",
                    "Le fichier CSS n'a pas pu être chargé.",
                    e.getMessage()
            );
        }
    }

    @FXML
    public void handleGoToOwner(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        SceneSwitcher.switchTo(stage, "/org/example/vetcare360/views/OwnersList.fxml");
    }

    @FXML
    private void handleOwners(ActionEvent event) {
        openPage("/org/example/vetcare360/views/OwnersSearch.fxml", "Owners", event);
    }

    @FXML
    private void handleVeterinarians(ActionEvent event) {
        openPage("/org/example/vetcare360/views/Veterinarians.fxml", "Veterinarians", event);
    }

    @FXML
    private void handleShowOwnersWithPets(ActionEvent event) {
        openPage("/org/example/vetcare360/views/OwnersAnimalsList.fxml", "Owners & Pets", event);
    }

    private void openPage(String fxmlPath, String title, ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            showError(
                    "Erreur lors de l'ouverture de la page",
                    "Une erreur s'est produite lors de la tentative d'ouverture de la page: " + title,
                    e.getMessage()
            );
        }
    }


    private void showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
