package tn.java.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;
import tn.java.entities.Categorie;
import tn.java.services.ServiceCategorie;
import tn.java.utils.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AjoutCategorie {

    @FXML
    private Button btnAfficher;

    @FXML
    private Button btnAjouter;

    @FXML
    private Button btnModifier;

    @FXML
    private Button btnSupprimer;

    @FXML
    private Button btnRecherche;

    @FXML
    private Button btnTri;

    @FXML
    private TableColumn<Categorie, String> colNom;

    @FXML
    private TableView<Categorie> table;

    @FXML
    private TextField Nom;

    @FXML
    private TextField searchField;

    @FXML
    void Afficher(ActionEvent event) {
        ServiceCategorie service = new ServiceCategorie();
        try {
            List<Categorie> categories = service.afficher();
            ObservableList<Categorie> observableCategories = FXCollections.observableArrayList(categories);
            table.setItems(observableCategories);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        showCategorie();
    }

    public ObservableList<Categorie> getCategories() {
        ObservableList<Categorie> categories = FXCollections.observableArrayList();
        String query = "SELECT * FROM categorie";
        Connection con = DataSource.getInstance().getCnx();
        try {
            PreparedStatement sp = con.prepareStatement(query);
            ResultSet rs = sp.executeQuery();
            while (rs.next()) {
                Categorie cat = new Categorie();
                cat.setNom(rs.getString("nom"));
                cat.setId(rs.getInt("id"));
                categories.add(cat);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories;
    }

    public void showCategorie() {
        ObservableList<Categorie> list = getCategories();
        table.setItems(list);
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        table.refresh();
    }

    @FXML
    void Ajouter(ActionEvent event) {
        String nom = Nom.getText();

        if (nom == null || nom.trim().isEmpty()) {
            showAlert("Erreur", "Le nom ne peut pas être vide.");
            return;
        }

        Categorie newCategorie = new Categorie(nom);

        ServiceCategorie serviceCategorie = new ServiceCategorie();
        try {
            serviceCategorie.ajouter(newCategorie);
            Afficher(null); // Rafraîchir le tableau
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Modifier(ActionEvent event) {
        Categorie selectedCategorie = table.getSelectionModel().getSelectedItem();
        if (selectedCategorie == null) {
            showAlert("Erreur", "Aucune catégorie sélectionnée.");
            return;
        }

        String nomValue = Nom.getText();

        if (nomValue == null || nomValue.trim().isEmpty()) {
            showAlert("Erreur", "Le nom ne peut pas être vide.");
            return;
        }

        selectedCategorie.setNom(nomValue);

        ServiceCategorie serviceCategorie = new ServiceCategorie();
        try {
            serviceCategorie.modifier(selectedCategorie);
            Afficher(null); // Actualiser le TableView pour refléter les changements
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Supprimer(ActionEvent event) {
        Categorie selectedCategorie = table.getSelectionModel().getSelectedItem();
        if (selectedCategorie == null) {
            showAlert("Erreur", "Aucune catégorie sélectionnée.");
            return;
        }

        boolean confirmation = showConfirmationDialog();
        if (!confirmation) {
            return;
        }

        ServiceCategorie serviceCategorie = new ServiceCategorie();
        try {
            serviceCategorie.supprimer(selectedCategorie.getId());
            Afficher(null); // Actualiser le TableView pour refléter les changements
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean showConfirmationDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de Suppression");
        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer cette catégorie ?");
        alert.setContentText("Cette action est irréversible.");

        ButtonType buttonTypeYes = new ButtonType("Oui");
        ButtonType buttonTypeNo = new ButtonType("Non");

        alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);

        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == buttonTypeYes;
    }

    @FXML
    void Recherche(ActionEvent event) {
        String searchText = searchField.getText();
        ServiceCategorie serviceCategorie = new ServiceCategorie();
        try {
            List<Categorie> filteredCategories = serviceCategorie.rechercherParNom(searchText);
            ObservableList<Categorie> observableCategories = FXCollections.observableArrayList(filteredCategories);
            table.setItems(observableCategories);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Tri(ActionEvent event) {
        ObservableList<Categorie> sortedList = FXCollections.observableArrayList(table.getItems());
        sortedList.sort((cat1, cat2) -> cat1.getNom().compareToIgnoreCase(cat2.getNom()));
        table.setItems(sortedList);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}


