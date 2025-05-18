package org.example.vetcare360.controllers;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.example.vetcare360.models.Animal;
import org.example.vetcare360.models.Proprietaire;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OwnerDetailsController {

    private static final Logger LOGGER = Logger.getLogger(OwnerDetailsController.class.getName());

    @FXML
    private Label nameLabel;
    @FXML
    private Label addressLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label telephoneLabel;

    @FXML
    private TableView<Animal> petsTable;
    @FXML
    private TableColumn<Animal, String> petNameColumn;
    @FXML
    private TableColumn<Animal, String> petBirthDateColumn;
    @FXML
    private TableColumn<Animal, String> petTypeColumn;
    @FXML
    private TableColumn<Animal, Void> petActionsColumn;

    private Proprietaire owner;

    public void setOwner(Proprietaire owner) {
        this.owner = owner;

        if (owner != null) {
            nameLabel.setText(owner.getFirstName() + " " + owner.getLastName());
            addressLabel.setText(owner.getAddress());
            cityLabel.setText(owner.getCity());
            telephoneLabel.setText(owner.getTelephone());
            loadPets();
        } else {
            LOGGER.warning("Owner is null while setting data!");
        }
    }

    public void reloadPets() {
        loadPets();
    }

    private void loadPets() {
        List<Animal> animals = AnimalController.getAnimalsByOwnerId(owner.getId());
        petsTable.setItems(FXCollections.observableArrayList(animals));

        petNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        petBirthDateColumn.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        petTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        petActionsColumn.setCellFactory(col -> new TableCell<>() {
            private final Button editPetBtn = new Button("Edit Pet");
            private final Button addVisitBtn = new Button("Add Visit");
            private final Button deletePetBtn = new Button("Delete Pet");
            private final HBox hbox = new HBox(10, editPetBtn, addVisitBtn, deletePetBtn);

            {
                editPetBtn.setOnAction(e -> handleEditPet(getTableView().getItems().get(getIndex())));
                addVisitBtn.setOnAction(e -> handleAddVisit(getTableView().getItems().get(getIndex())));
                deletePetBtn.setOnAction(e -> handleDeletePet(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : hbox);
            }
        });
    }

    @FXML
    private void handleEditOwner() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/vetcare360/views/OwnerForm.fxml"));
            Parent root = loader.load();
            OwnerFormController controller = loader.getController();
            controller.setOwnerToEdit(owner);

            String cssPath = getClass().getResource("/org/example/vetcare360/css/style.css").toExternalForm();
            root.getStylesheets().add(cssPath);

            Stage stage = new Stage();
            stage.setTitle("Edit Owner");
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> {
                Proprietaire updatedOwner = ProprietaireController.getOwnerById(owner.getId());
                if (updatedOwner != null) {
                    setOwner(updatedOwner);
                } else {
                    LOGGER.warning("Owner not found after editing.");
                }
            });
            stage.show();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while editing owner", e);
        }
    }

    @FXML
    private void handleAddNewPet() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/vetcare360/views/AnimalForm.fxml"));
            Parent root = loader.load();
            AnimalFormController controller = loader.getController();
            controller.setOwner(owner);
            controller.setOwnerDetailsController(this);

            String cssPath = getClass().getResource("/org/example/vetcare360/css/style.css").toExternalForm();
            root.getStylesheets().add(cssPath);

            Stage stage = new Stage();
            stage.setTitle("Add New Pet");
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> reloadPets());
            stage.show();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while adding new pet", e);
        }
    }

    private void handleEditPet(Animal animal) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/vetcare360/views/AnimalForm.fxml"));
            Parent root = loader.load();
            AnimalFormController controller = loader.getController();
            controller.setAnimalToEdit(animal);
            controller.setOwner(owner);
            controller.setOwnerDetailsController(this);

            String cssPath = getClass().getResource("/org/example/vetcare360/css/style.css").toExternalForm();
            root.getStylesheets().add(cssPath);

            Stage stage = new Stage();
            stage.setTitle("Edit Pet");
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> reloadPets());
            stage.show();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while editing pet", e);
        }
    }

    private void handleAddVisit(Animal animal) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/vetcare360/views/VisiteForm.fxml"));
            Parent root = loader.load();
            VisiteFormController controller = loader.getController();
            controller.setAnimal(animal);
            controller.setOwnerDetailsController(this);

            String cssPath = getClass().getResource("/org/example/vetcare360/css/style.css").toExternalForm();
            root.getStylesheets().add(cssPath);

            Stage stage = new Stage();
            stage.setTitle("Add Visit");
            stage.setScene(new Scene(root));
            stage.setOnHidden(e -> reloadPets());
            stage.show();
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while adding visit", e);
        }
    }

    private void handleDeletePet(Animal animal) {
        AnimalController.deleteAnimal(animal.getId());
        reloadPets();
    }

    public int getOwnerId() {
        return owner != null ? owner.getId() : -1;
    }
}
