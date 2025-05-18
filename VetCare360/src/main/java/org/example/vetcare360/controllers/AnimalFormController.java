package org.example.vetcare360.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.vetcare360.models.Animal;
import org.example.vetcare360.models.Proprietaire;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class AnimalFormController {
    @FXML
    private Label ownerNameLabel;
    @FXML
    private TextField animalNameField;
    @FXML
    private DatePicker birthDatePicker;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private Button addOrUpdatePetButton;

    private Proprietaire owner;
    private Animal animalToEdit = null;
    private OwnerDetailsController ownerDetailsController;

    @FXML
    public void initialize() {
        loadAnimalTypes();
    }

    private void loadAnimalTypes() {
        typeComboBox.getItems().setAll("cat", "dog", "bird", "hamster", "lizard", "snake", "other");
    }

    public void setOwner(Proprietaire owner) {
        this.owner = owner;
        ownerNameLabel.setText("Owner: " + owner.getFirstName() + " " + owner.getLastName());
        addOrUpdatePetButton.setText("Add Pet");
    }

    public void setOwnerDetailsController(OwnerDetailsController controller) {
        this.ownerDetailsController = controller;
    }

    public void setAnimalToEdit(Animal animal) {
        this.animalToEdit = animal;

        animalNameField.setText(animal.getName());

        if (animal.getBirthDate() != null && !animal.getBirthDate().isEmpty()) {
            try {
                birthDatePicker.setValue(LocalDate.parse(animal.getBirthDate()));
            } catch (DateTimeParseException e) {
                System.out.println("Date of birth format error: " + e.getMessage());
            }
        }

        if (animal.getType() != null && !animal.getType().isEmpty()) {
            typeComboBox.setValue(animal.getType());
        }

        addOrUpdatePetButton.setText("Update Pet");
    }

    @FXML
    private void handleAddOrUpdatePet() {
        String name = animalNameField.getText();
        LocalDate birthDateValue = birthDatePicker.getValue();
        String type = typeComboBox.getValue();

        String birthDate = birthDateValue != null ? birthDateValue.toString() : "";


        System.out.println("name: " + name);
        System.out.println("birthDate: " + birthDate);
        System.out.println("type: " + type);
        System.out.println("owner: " + owner);


        if (name == null || name.isEmpty() ||
                birthDate == null || birthDate.isEmpty() ||
                type == null || type.isEmpty() ||
                owner == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please fill in all fields!");
            alert.showAndWait();
            return;
        }

        if (animalToEdit == null) {

            Animal animal = new Animal(0, name, birthDate, type, owner.getId());
            AnimalController.addAnimal(animal);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "The animal has been added successfully!");
            alert.showAndWait();
        } else {

            animalToEdit.setName(name);
            animalToEdit.setBirthDate(birthDate);
            animalToEdit.setType(type);
            AnimalController.updateAnimal(animalToEdit);
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Animal data updated successfully!");
            alert.showAndWait();
        }


        if (ownerDetailsController != null) {
            ownerDetailsController.reloadPets();
        }


        ((Stage) animalNameField.getScene().getWindow()).close();
    }
}
