package tn.java.services;

import tn.java.entities.Event;
import tn.java.utils.DataSource;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ServiceEvent implements IService<Event> {

    Connection cnx = DataSource.getInstance().getCnx(); // Utiliser 'cnx' au lieu de 'connection'

    public void ajouter(Event event) throws SQLException {
        // Requête SQL d'insertion avec les attributs de l'événement
        String query = "INSERT INTO event (nom, description, image, emplacement, nombre_places, date) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            // Définir les valeurs pour les paramètres de la requête
            ps.setString(1, event.getNom());
            ps.setString(2, event.getDescription());
            ps.setString(3, event.getImage());
            ps.setString(4, event.getEmplacement());
            ps.setInt(5, event.getNombrePlaces());
            ps.setDate(6, Date.valueOf(event.getDate()));

            // Exécuter la requête
            ps.executeUpdate();
            System.out.println("Event ajouté avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de l'ajout de l'événement : " + e.getMessage());
        }
    }

    public void modifier(Event event) throws SQLException {
        // Requête SQL de mise à jour avec les attributs de l'événement
        String query = "UPDATE events SET nom = ?, description = ?, image = ?, emplacement = ?, nombre_places = ?, date = ? WHERE id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            // Définir les valeurs pour les paramètres de la requête
            ps.setString(1, event.getNom());
            ps.setString(2, event.getDescription());
            ps.setString(3, event.getImage());
            ps.setString(4, event.getEmplacement());
            ps.setInt(5, event.getNombrePlaces());
            ps.setDate(6, Date.valueOf(event.getDate()));

            // Exécuter la requête
            int rowsUpdated = ps.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Event modifié avec succès !");
            } else {
                System.out.println("Aucun événement trouvé avec l'ID spécifié.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la modification de l'événement : " + e.getMessage());
        }
    }

    public void supprimer(int id) throws SQLException {
        // Requête SQL pour supprimer un événement en fonction de son ID
        String query = "DELETE FROM events WHERE id = ?";

        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            // Définir l'ID de l'événement à supprimer
            ps.setInt(1, id);

            // Exécuter la requête
            int rowsDeleted = ps.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Event supprimé avec succès !");
            } else {
                System.out.println("Aucun événement trouvé avec l'ID spécifié.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la suppression de l'événement : " + e.getMessage());
        }
    }

    public Event getOneById(int id) throws SQLException {
        String query = "SELECT * FROM events WHERE id = ?";
        Event event = null;

        try (PreparedStatement ps = cnx.prepareStatement(query)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                event = new Event(
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getString("emplacement"),
                        rs.getInt("nombre_places"),
                        rs.getDate("date").toLocalDate()
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la récupération de l'événement par ID : " + e.getMessage());
        }

        return event;
    }

    public Set<Event> getAll() throws SQLException {
        String query = "SELECT * FROM events";
        Set<Event> events = new HashSet<>();

        try (Statement stmt = cnx.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Event event = new Event(
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getString("emplacement"),
                        rs.getInt("nombre_places"),
                        rs.getDate("date").toLocalDate()
                );
                events.add(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException("Erreur lors de la récupération de tous les événements : " + e.getMessage());
        }

        return events;
    }

    public List<Event> afficher() {
        List<Event> events = new ArrayList<>();

        try {
            String query = "SELECT * FROM events";
            Statement stmt = cnx.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                Event event = new Event(
                        rs.getString("nom"),
                        rs.getString("description"),
                        rs.getString("image"),
                        rs.getString("emplacement"),
                        rs.getInt("nombre_places"),
                        rs.getDate("date").toLocalDate()
                );
                events.add(event);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return events;
    }

}
