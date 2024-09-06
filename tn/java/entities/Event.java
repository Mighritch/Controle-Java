package tn.java.entities;

import java.time.LocalDate;


public class Event {
    private int id;
    private String nom;
    private String description;
    private String emplacement;
    private int nombrePlaces;
    private LocalDate date;

    public Event() {
    }

    public Event(int id, String nom, String description, String emplacement, int nombrePlaces, LocalDate date) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.emplacement = emplacement;
        this.nombrePlaces = nombrePlaces;
        this.date = date;
    }

    public Event(String nom, String description, String emplacement, int nombrePlaces, LocalDate date) {
        this.nom = nom;
        this.description = description;
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

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", emplacement='" + emplacement + '\'' +
                ", nombrePlaces=" + nombrePlaces +
                ", date=" + date +
                '}';
    }
}
