package tn.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.Map;

public class Stat {

    @FXML
    private BarChart<String, Number> StatLieux;  // Utiliser Number au lieu d'Integer pour l'axe des valeurs

    @FXML
    private CategoryAxis emplacement;

    @FXML
    private NumberAxis nombreEmplacement;

    // Méthode pour afficher les statistiques
    public void displayStats(Map<String, Integer> stats) {
        // Créer une nouvelle série pour le BarChart
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Parcourir les données et les ajouter à la série
        for (Map.Entry<String, Integer> entry : stats.entrySet()) {
            series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
        }

        // Ajouter la série au graphique
        StatLieux.getData().add(series);
    }
}
