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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.IOException;  // Assurez-vous que cela est importé
import java.util.Map;
import java.util.HashMap;

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
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private Button btnRechecherche;

    @FXML
    private Button btnTri;

    @FXML
    private Button btnStat;

    @FXML
    private TableColumn<Event, LocalDate> colDate;

    @FXML
    private TableColumn<Event, String> colDescription;

    @FXML
    private TableColumn<Event, Integer> colEmplacement;


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
        colEmplacement.setCellValueFactory(new PropertyValueFactory<>("emplacement")); // Correctement lié à 'emplacement'
        colNombreplace.setCellValueFactory(new PropertyValueFactory<>("nombrePlaces")); // Correctement lié à 'nombrePlaces'
        colDate.setCellValueFactory(new PropertyValueFactory<>("date")); // Correctement lié à 'date'

        Table.refresh(); // Rafraîchit le TableView pour afficher les données
    }

    @FXML
    void Ajouter(ActionEvent event) {
        // Vérification que tous les champs sont remplis
        if (Nom.getText().isEmpty() || Description.getText().isEmpty() || Emplacement.getText().isEmpty() ||
                Nombreplace.getText().isEmpty() || Date.getValue() == null) {
            showAlert(AlertType.ERROR, "Erreur de validation", "Tous les champs doivent être remplis.");
            return;
        }

        // Vérification que les champs nom, description, emplacement ne dépassent pas 50 caractères
        if (Nom.getText().length() > 50 || Description.getText().length() > 50 || Emplacement.getText().length() > 50) {
            showAlert(AlertType.ERROR, "Erreur de validation", "Nom, Description et Emplacement ne doivent pas dépasser 50 caractères.");
            return;
        }

        // Vérification que le nombre de places est supérieur à 0
        int nombrePlaces;
        try {
            nombrePlaces = Integer.parseInt(Nombreplace.getText());
            if (nombrePlaces <= 0) {
                showAlert(AlertType.ERROR, "Erreur de validation", "Le nombre de places doit être supérieur à 0.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(AlertType.ERROR, "Erreur de validation", "Le nombre de places doit être un nombre valide.");
            return;
        }

        // Vérification que la date est au moins la date du système
        LocalDate date = Date.getValue();
        if (date.isBefore(LocalDate.now())) {
            showAlert(AlertType.ERROR, "Erreur de validation", "La date ne peut pas être antérieure à la date actuelle.");
            return;
        }

        // Si toutes les vérifications sont passées, créer l'événement
        String nom = Nom.getText();
        String description = Description.getText();
        String emplacement = Emplacement.getText();

        Event newEvent = new Event(nom, description, emplacement, nombrePlaces, date);

        ServiceEvent serviceEvent = new ServiceEvent();
        try {
            serviceEvent.ajouter(newEvent);
            Afficher(null); // Rafraîchit la table après ajout
            clearFields();   // Vider les champs après ajout
            showAlert(AlertType.INFORMATION, "Succès", "L'événement a été ajouté avec succès.");
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Erreur d'ajout", "Erreur lors de l'ajout de l'événement : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
                Afficher(null); // Rafraîchir le tableau après modification
                clearFields();   // Vider les champs après modification
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
                Afficher(null); // Rafraîchir le tableau après suppression
                clearFields();   // Vider les champs après suppression
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

        @FXML
        void Recherche (ActionEvent event){
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
        void Tri (ActionEvent event){
            // Obtenez la liste des événements actuels de la TableView
            ObservableList<Event> sortedList = FXCollections.observableArrayList(Table.getItems());

            // Triez la liste selon le nom (ordre alphabétique)
            sortedList.sort((event1, event2) -> event1.getNom().compareToIgnoreCase(event2.getNom()));

            // Mettez à jour le TableView avec la liste triée
            Table.setItems(sortedList);
        }

        @FXML
        void Stat (ActionEvent event){
            try {
                // Requête SQL pour récupérer le nombre d'événements par emplacement
                String query = "SELECT emplacement, COUNT(*) AS nombre_events FROM event GROUP BY emplacement";

                Connection con = DataSource.getInstance().getCnx();
                PreparedStatement sp = con.prepareStatement(query);
                ResultSet rs = sp.executeQuery();

                // Stocker les résultats dans une Map ou une liste pour les passer au graphique
                Map<String, Integer> stats = new HashMap<>();

                while (rs.next()) {
                    String emplacement = rs.getString("emplacement");
                    int nombreEvents = rs.getInt("nombre_events");
                    stats.put(emplacement, nombreEvents); // Ajouter chaque emplacement et son nombre d'événements
                }

                // Création d'un FXMLLoader
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/tn/java/resources/stat.fxml"));

                // Chargement du fichier FXML
                Parent root = loader.load();

                // Récupérer le contrôleur du fichier FXML pour y injecter les données
                Stat statController = loader.getController();
                statController.displayStats(stats); // Passer les données récupérées au contrôleur des statistiques

                // Création de la scène et du stage
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Statistiques des lieux");
                stage.show();
            } catch (IOException | SQLException e) {
                // Gestion des erreurs
                e.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Erreur", "Erreur lors du chargement des statistiques.");
            }
        }

        @FXML
        public void initialize () {
            // Lier les colonnes du TableView avec les propriétés de l'Event
            colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
            colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            colEmplacement.setCellValueFactory(new PropertyValueFactory<>("emplacement"));
            colNombreplace.setCellValueFactory(new PropertyValueFactory<>("nombrePlaces"));
            colDate.setCellValueFactory(new PropertyValueFactory<>("date"));

            // Charger les événements dans le TableView
            Table.setItems(getAllEvents());  // Méthode qui récupère tous les événements
        }

        @FXML
        public void onEventSelected () {
            Event selectedEvent = Table.getSelectionModel().getSelectedItem();

            if (selectedEvent != null) {
                // Remplir les champs avec les données de l'événement sélectionné
                Nom.setText(selectedEvent.getNom());
                Description.setText(selectedEvent.getDescription());
                Emplacement.setText(selectedEvent.getEmplacement());
                Nombreplace.setText(String.valueOf(selectedEvent.getNombrePlaces()));
                Date.setValue(selectedEvent.getDate());
            }
        }

        private ObservableList<Event> getAllEvents () {
            ObservableList<Event> events = FXCollections.observableArrayList();
            // Ajoutez ici la logique pour récupérer les événements depuis la base de données
            return events;
        }

        private void clearFields () {
            Nom.clear();
            Description.clear();
            Emplacement.clear();
            Nombreplace.clear();
            Date.setValue(null);
        }

    }
