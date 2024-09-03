package tn.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import tn.java.entities.Categorie;
import tn.java.entities.Event;
import tn.java.services.ServiceCategorie;
import tn.java.services.ServiceEvent;


public class AjoutEvent {

    @FXML
    private DatePicker Date;

    @FXML
    private TextField Description;

    @FXML
    private TextField Emplacement;

    @FXML
    private TextField Nom;

    @FXML
    private TextField Nombreplace;

    @FXML
    private TableView<Event> Table;

    @FXML
    private Button btnAffiche;

    @FXML
    private Button btnAjout;

    @FXML
    private Button btnChoisirImage;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private TableColumn<Event, LocalDate> colDate;

    @FXML
    private TableColumn<Event, String> colDescription;

    @FXML
    private TableColumn<Event, Integer> colEmplacement;

    @FXML
    private TableColumn<Event, String> colImage;

    @FXML
    private TableColumn<Event, String> colNom;

    @FXML
    private TableColumn<?, ?> colNombreplace;

    @FXML
    void Afficher(ActionEvent event) {
        ServiceEvent service = new ServiceEvent();
        List<Event> events = service.afficher(); // Récupérer les événements depuis la base de données
        ObservableList<Event> observableEvents = FXCollections.observableArrayList(events);
        Table.setItems(observableEvents); // Mettre à jour le TableView avec les événements
    }


    @FXML
    void Ajouter(ActionEvent event) {
        String nom = Nom.getText();
        String description = Description.getText();
        String emplacement = Emplacement.getText();
        int nombrePlaces = Integer.parseInt(Nombreplace.getText());
        LocalDate date = Date.getValue();
        String image = "default.jpg"; // Vous pouvez remplacer par le chemin réel de l'image

        Event newEvent = new Event(nom, description, image, emplacement, nombrePlaces, date);

        ServiceEvent serviceEvent = new ServiceEvent();
        try {
            serviceEvent.ajouter(newEvent);
            Afficher(null); // Refresh the table
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void ChoisirImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            // Assignez le chemin de l'image à l'événement
            // Par exemple : eventImagePath = selectedFile.getAbsolutePath();
        }
    }


    @FXML
    void Modifier(ActionEvent event) {
        Event selectedEvent = Table.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            selectedEvent.setNom(Nom.getText());
            selectedEvent.setDescription(Description.getText());
            selectedEvent.setEmplacement(Emplacement.getText());
            selectedEvent.setNombrePlaces(Integer.parseInt(Nombreplace.getText()));
            selectedEvent.setDate(Date.getValue());

            ServiceEvent serviceEvent = new ServiceEvent();
            try {
                serviceEvent.modifier(selectedEvent);
                Afficher(null); // Refresh the table
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void Supprimer(ActionEvent event) {
        Event selectedEvent = Table.getSelectionModel().getSelectedItem();

        if (selectedEvent != null) {
            ServiceEvent serviceEvent = new ServiceEvent();
            try {
                serviceEvent.supprimer(selectedEvent.getId()); // Suppression par ID
                Afficher(null); // Refresh the table
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

