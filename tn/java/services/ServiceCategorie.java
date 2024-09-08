package tn.java.services;

import java.sql.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;

import tn.java.entities.Categorie;
import tn.java.entities.Event;
import tn.java.utils.DataSource;
import java.time.LocalDate;


public class ServiceCategorie implements IService<Categorie> {

    Connection cnx = DataSource.getInstance().getCnx();

    @Override
    public void ajouter(Categorie categorie) throws SQLException {
        String query = "INSERT INTO categorie (nom) VALUES (?)";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, categorie.getNom());
            statement.executeUpdate();
        }
    }

    @Override
    public void modifier(Categorie categorie) throws SQLException {
        String query = "UPDATE categorie SET nom = ? WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setString(1, categorie.getNom());
            statement.setInt(2, categorie.getId());
            statement.executeUpdate();
        }
    }

    @Override
    public void supprimer(int id) throws SQLException {
        String query = "DELETE FROM categorie WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    @Override
    public Categorie getOneById(int id) throws SQLException {
        String query = "SELECT * FROM categorie WHERE id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String nom = resultSet.getString("nom");
                return new Categorie(id, nom);
            }
        }
        return null;
    }

    @Override
    public List<Categorie> getAll() throws SQLException {
        List<Categorie> categories = new ArrayList<>();
        String query = "SELECT * FROM categorie";
        try (Statement statement = cnx.createStatement()) {
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                categories.add(new Categorie(id, nom));
            }
        }
        return categories;
    }

    @Override
    public List<Categorie> afficher() throws SQLException {
        return getAll(); // Réutiliser getAll pour afficher les catégories
    }

    @Override
    public List<Categorie> rechercherParNom(String nom) throws SQLException {
        List<Categorie> filteredCategories = new ArrayList<>();
        String query = "SELECT * FROM categorie WHERE nom LIKE ?";
        try (PreparedStatement sp = cnx.prepareStatement(query)) {
            sp.setString(1, "%" + nom + "%"); // Recherche partielle
            ResultSet rs = sp.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String nomCategorie = rs.getString("nom");
                filteredCategories.add(new Categorie(id, nomCategorie));
            }
        }
        return filteredCategories;
    }

    public List<Event> getEventsByCategorie(int categorieId) throws SQLException {
        List<Event> events = new ArrayList<>();
        String query = "SELECT * FROM event WHERE categorie_id = ?";
        try (PreparedStatement statement = cnx.prepareStatement(query)) {
            statement.setInt(1, categorieId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String nom = resultSet.getString("nom");
                String description = resultSet.getString("description");
                String emplacement = resultSet.getString("emplacement");
                int nombrePlaces = resultSet.getInt("nombrePlaces");
                LocalDate date = resultSet.getDate("date").toLocalDate();
                events.add(new Event(id, nom, description, emplacement, nombrePlaces, date));
            }
        }
        return events;
    }
}
