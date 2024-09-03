package tn.java.entities;

import java.time.LocalDate;

public class Event {
    private int id; // Ajoutez cet attribut pour l'identifiant
    private String nom;
    private String description;
    private String image;
    private String emplacement;
    private int nombrePlaces;
    private LocalDate date;

    // Constructeur avec ID
    public Event(int id, String nom, String description, String image, String emplacement, int nombrePlaces, LocalDate date) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.emplacement = emplacement;
        this.nombrePlaces = nombrePlaces;
        this.date = date;
    }

    // Constructeur sans ID (pour la création de nouveaux événements)
    public Event(String nom, String description, String image, String emplacement, int nombrePlaces, LocalDate date) {
        this.nom = nom;
        this.description = description;
        this.image = image;
        this.emplacement = emplacement;
        this.nombrePlaces = nombrePlaces;
        this.date = date;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    public int getNombrePlaces() {
        return nombrePlaces;
    }

    public void setNombrePlaces(int nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    // Méthode toString pour afficher les informations de l'événement
    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", image='" + image + '\'' +
                ", emplacement='" + emplacement + '\'' +
                ", nombrePlaces=" + nombrePlaces +
                ", date=" + date +
                '}';
    }
}
