package tn.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import tn.java.entities.Event;
import tn.java.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import java.io.IOException;
import javafx.scene.Parent;

public class AfficheEvent {

    @FXML
    private TextField SearchField;

    @FXML
    private TextField reservationfield;

    @FXML
    private Button btnRecherche;

    @FXML
    private Button btnCalendrier;

    @FXML
    private Button btnReserver; // Assurez-vous d'avoir un bouton pour la réservation

    @FXML
    private ListView<String> listViewEvents; // ListView pour afficher les événements

    private Event selectedEvent; // Événement actuellement sélectionné

    @FXML
    void initialize() {
        // Appel de la méthode pour afficher les événements lors de l'initialisation
        afficherEvenements();

        // Ajouter un listener pour détecter les changements dans le champ de recherche
        SearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Mettre à jour la liste en fonction de la nouvelle valeur du champ de recherche
            if (newValue.trim().isEmpty()) {
                afficherEvenements();
            } else {
                afficherEvenementsParNom(newValue.trim());
            }
        });

        // Ajouter un listener pour détecter la sélection d'un événement dans la ListView
        listViewEvents.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            // Si un événement est sélectionné, mettre à jour la variable selectedEvent
            if (newValue != null) {
                selectedEvent = findEventByDetails(newValue);
            }
        });
    }

    @FXML
    void Recherche(ActionEvent event) {
        String searchTerm = SearchField.getText().trim();
        if (searchTerm.isEmpty()) {
            // Si le champ de recherche est vide, afficher tous les événements
            afficherEvenements();
        } else {
            // Sinon, rechercher les événements par nom
            afficherEvenementsParNom(searchTerm);
        }
    }

    @FXML
    void Reserver(ActionEvent event) {
        if (selectedEvent == null) {
            // Si aucun événement n'est sélectionné, afficher un message d'erreur ou avertir l'utilisateur
            System.out.println("Aucun événement sélectionné");
            return;
        }

        String reservationText = reservationfield.getText().trim();
        if (reservationText.isEmpty()) {
            // Si le champ de réservation est vide, afficher un message d'erreur ou avertir l'utilisateur
            System.out.println("Le champ de réservation est vide");
            return;
        }

        int nombrePlacesReservées;
        try {
            nombrePlacesReservées = Integer.parseInt(reservationText);
        } catch (NumberFormatException e) {
            // Si la conversion échoue, afficher un message d'erreur ou avertir l'utilisateur
            System.out.println("Le nombre de places réservées n'est pas valide");
            return;
        }

        // Vérifier la disponibilité
        if (selectedEvent.getNombrePlaces() < nombrePlacesReservées) {
            // Afficher un message d'erreur si le nombre de places réservées est supérieur au nombre disponible
            System.out.println("Pas assez de places disponibles");
            return;
        }

        // Effectuer la réservation
        reserverPlaces(selectedEvent, nombrePlacesReservées);

        // Mettre à jour l'affichage
        afficherEvenements();
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

    public ObservableList<Event> getEventsByName(String name) {
        ObservableList<Event> events = FXCollections.observableArrayList();
        String query = "SELECT * FROM event WHERE nom LIKE ?";
        Connection con = DataSource.getInstance().getCnx();
        try {
            PreparedStatement sp = con.prepareStatement(query);
            sp.setString(1, "%" + name + "%"); // Utilisation du LIKE pour la recherche
            ResultSet rs = sp.executeQuery();
            while (rs.next()) {
                Event event = new Event();
                event.setId(rs.getInt("id"));
                event.setNom(rs.getString("nom"));
                event.setDescription(rs.getString("description"));
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

    public void afficherEvenements() {
        ObservableList<Event> events = getEvents(); // Récupère la liste des événements

        // Crée une ObservableList pour stocker les chaînes représentant les événements sans ID
        ObservableList<String> eventDetails = FXCollections.observableArrayList();

        // Ajoute les informations des événements sans afficher l'ID
        for (Event event : events) {
            String eventDetail = "Nom: " + event.getNom() + "\n"
                    + "Description: " + event.getDescription() + "\n"
                    + "Emplacement: " + event.getEmplacement() + "\n"
                    + "Nombre de places: " + event.getNombrePlaces() + "\n"
                    + "Date: " + event.getDate();
            eventDetails.add(eventDetail); // Ajoute les informations dans la liste sans l'ID
        }

        // Met à jour la ListView avec les informations complètes des événements sans ID
        listViewEvents.setItems(eventDetails);
    }

    public void afficherEvenementsParNom(String nom) {
        ObservableList<Event> events = getEventsByName(nom); // Récupère la liste des événements par nom

        // Crée une ObservableList pour stocker les chaînes représentant les événements sans ID
        ObservableList<String> eventDetails = FXCollections.observableArrayList();

        // Ajoute les informations des événements sans afficher l'ID
        for (Event event : events) {
            String eventDetail = "Nom: " + event.getNom() + "\n"
                    + "Description: " + event.getDescription() + "\n"
                    + "Emplacement: " + event.getEmplacement() + "\n"
                    + "Nombre de places: " + event.getNombrePlaces() + "\n"
                    + "Date: " + event.getDate();
            eventDetails.add(eventDetail); // Ajoute les informations dans la liste sans l'ID
        }

        // Met à jour la ListView avec les informations complètes des événements sans ID
        listViewEvents.setItems(eventDetails);
    }

    // Méthode pour trouver un événement par ses détails dans la ListView
    private Event findEventByDetails(String details) {
        ObservableList<Event> events = getEvents(); // Récupère la liste des événements
        for (Event event : events) {
            String eventDetail = "Nom: " + event.getNom() + "\n"
                    + "Description: " + event.getDescription() + "\n"
                    + "Emplacement: " + event.getEmplacement() + "\n"
                    + "Nombre de places: " + event.getNombrePlaces() + "\n"
                    + "Date: " + event.getDate();
            if (eventDetail.equals(details)) {
                return event;
            }
        }
        return null; // Retourne null si aucun événement correspondant n'est trouvé
    }

    // Méthode pour effectuer la réservation
    private void reserverPlaces(Event event, int nombrePlaces) {
        String query = "UPDATE event SET nombre_places = nombre_places - ? WHERE id = ?";
        Connection con = DataSource.getInstance().getCnx();
        try {
            PreparedStatement ps = con.prepareStatement(query);
            ps.setInt(1, nombrePlaces);
            ps.setInt(2, event.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Calendrier(ActionEvent event) {
        try {
            System.out.println("Chargement du calendrier...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/java/resources/Calendar.fxml"));
            Parent root = loader.load();

            // Ouvrir une nouvelle fenêtre pour afficher le calendrier
            Stage stage = new Stage();
            stage.setTitle("Calendrier des événements");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


