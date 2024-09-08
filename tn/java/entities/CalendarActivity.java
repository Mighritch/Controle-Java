package tn.java.entities;

import java.time.LocalDate;

public class CalendarActivity {
    private String nom;
    private String description;
    private String lieu;
    private int nombre_des_places;
    private LocalDate date;
    private int id;

    public CalendarActivity(){

    }
    public CalendarActivity(int id, String nom, String description, String lieu, int nombre_des_places, LocalDate date) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.lieu = lieu;
        this.nombre_des_places = nombre_des_places;
        this.date = date;
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

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public int getNombre_des_places() {
        return nombre_des_places;
    }

    public void setNombre_des_places(int nombre_des_places) {
        this.nombre_des_places = nombre_des_places;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalendarActivity calendarActivity = (CalendarActivity) o;
        return id == calendarActivity.id;
    }

    @Override
    public String toString() {
        return "CalendarActivity{" +
                "nom='" + nom + '\'' +
                ", description='" + description + '\'' +
                ", lieu='" + lieu + '\'' +
                ", nombre_des_places=" + nombre_des_places +
                ", date=" + date +
                ", id=" + id +
                '}';
    }
}
