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
import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

import tn.java.entities.Event;
import tn.java.services.ServiceEvent;
import tn.java.utils.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

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
    private TextField SearchField;

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
    private Button btnRechecherche;

    @FXML
    private Button btnTri;

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
    private TableColumn<Event, Integer> colNombreplace;

    @FXML
    void Afficher(ActionEvent event) {
        ServiceEvent service = new ServiceEvent();
        List<Event> events = service.afficher(); // Récupérer les événements depuis la base de données
        ObservableList<Event> observableEvents = FXCollections.observableArrayList(events);
        Table.setItems(observableEvents); // Mettre à jour le TableView avec les événements

        showEvent();
    }

    public ObservableList<Event> getEvents() {
        ObservableList<Event> events = FXCollections.observableArrayList();
        String query = "SELECT * FROM event";
        Connection con = DataSource.getInstance().getCnx();
        try {
            PreparedStatement sp = con.prepareStatement(query);
            ResultSet rs = sp.executeQuery();
            while (rs.next()) {
                Event event = new Event();
                event.setId(rs.getInt("id"));
                event.setNom(rs.getString("nom"));
                event.setDescription(rs.getString("description"));
                event.setImage(rs.getString("image"));
                event.setEmplacement(rs.getString("emplacement"));
                event.setNombrePlaces(rs.getInt("nombre_places"));
                event.setDate(rs.getDate("date").toLocalDate());
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public void showEvent() {
        ObservableList<Event> list = getEvents(); // Récupère la liste des événements
        Table.setItems(list); // Définit les éléments du TableView
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colImage.setCellValueFactory(new PropertyValueFactory<>("image"));
        colEmplacement.setCellValueFactory(new PropertyValueFactory<>("emplacement"));
        colNombreplace.setCellValueFactory(new PropertyValueFactory<>("nombrePlaces"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        Table.refresh(); // Rafraîchit le TableView pour afficher les données
    }

    @FXML
    void Ajouter(ActionEvent event) {
        // Vérification que tous les champs sont remplis
        if (Nom.getText().isEmpty() || Description.getText().isEmpty() || Emplacement.getText().isEmpty() ||
                Nombreplace.getText().isEmpty() || Date.getValue() == null) {
            // Afficher un message d'erreur
            System.out.println("Tous les champs doivent être remplis");
            return;
        }

        // Vérification que les champs nom, description, emplacement ne dépassent pas 50 caractères
        if (Nom.getText().length() > 50 || Description.getText().length() > 50 || Emplacement.getText().length() > 50) {
            // Afficher un message d'erreur
            System.out.println("Nom, Description et Emplacement ne doivent pas dépasser 50 caractères");
            return;
        }

        // Vérification que le nombre de places est supérieur à 0
        int nombrePlaces;
        try {
            nombrePlaces = Integer.parseInt(Nombreplace.getText());
            if (nombrePlaces <= 0) {
                System.out.println("Le nombre de places doit être supérieur à 0");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Le nombre de places doit être un nombre valide");
            return;
        }

        // Vérification que la date est au moins la date du système
        LocalDate date = Date.getValue();
        if (date.isBefore(LocalDate.now())) {
            System.out.println("La date ne peut pas être antérieure à la date actuelle");
            return;
        }

        // Si toutes les vérifications sont passées, créer l'événement
        String nom = Nom.getText();
        String description = Description.getText();
        String emplacement = Emplacement.getText();
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

    @FXML
    void Recherche(ActionEvent event) {
        String rechercheNom = SearchField.getText();
        ServiceEvent service = new ServiceEvent();

        try {
            List<Event> events = service.rechercherParNom(rechercheNom); // Rechercher les événements par nom
            ObservableList<Event> observableEvents = FXCollections.observableArrayList(events);
            Table.setItems(observableEvents); // Mettre à jour le TableView avec les résultats de la recherche
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Tri(ActionEvent event) {
        // Obtenez la liste des événements actuels de la TableView
        ObservableList<Event> sortedList = FXCollections.observableArrayList(Table.getItems());

        // Triez la liste selon le nom (ordre alphabétique)
        sortedList.sort((event1, event2) -> event1.getNom().compareToIgnoreCase(event2.getNom()));

        // Mettez à jour le TableView avec la liste triée
        Table.setItems(sortedList);
    }
}
