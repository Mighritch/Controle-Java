package tn.java.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import tn.java.entities.CalendarActivity;
import tn.java.entities.Event;
import tn.java.services.ServiceEvent;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import javafx.scene.Node;

public class Calendar implements Initializable {

    ZonedDateTime dateFocus;
    ZonedDateTime today;

    @FXML
    private Text year;

    @FXML
    private Text month;

    @FXML
    private FlowPane calendar;

    @FXML
    private TextArea eventDetailsTextArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateFocus = ZonedDateTime.now();
        today = ZonedDateTime.now();
        drawCalendar();
        loadData();
    }

    @FXML
    void backOneMonth(ActionEvent event) {
        dateFocus = dateFocus.minusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
        loadData();
    }

    @FXML
    void forwardOneMonth(ActionEvent event) {
        dateFocus = dateFocus.plusMonths(1);
        calendar.getChildren().clear();
        drawCalendar();
        loadData();
    }

    private void drawCalendar() {
        year.setText(String.valueOf(dateFocus.getYear()));
        month.setText(String.valueOf(dateFocus.getMonth()));

        double calendarWidth = calendar.getPrefWidth();
        double calendarHeight = calendar.getPrefHeight();
        double strokeWidth = 1;
        double spacingH = calendar.getHgap();
        double spacingV = calendar.getVgap();

        int monthMaxDate = dateFocus.getMonth().maxLength();
        if (dateFocus.getYear() % 4 != 0 && monthMaxDate == 29) {
            monthMaxDate = 28;
        }
        int dateOffset = ZonedDateTime.of(dateFocus.getYear(), dateFocus.getMonthValue(), 1, 0, 0, 0, 0, dateFocus.getZone()).getDayOfWeek().getValue();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 7; j++) {
                StackPane stackPane = new StackPane();

                Rectangle rectangle = new Rectangle();
                rectangle.setFill(Color.TRANSPARENT);
                rectangle.setStroke(Color.BLACK);
                rectangle.setStrokeWidth(strokeWidth);
                double rectangleWidth = (calendarWidth / 7) - strokeWidth - spacingH;
                rectangle.setWidth(rectangleWidth);
                double rectangleHeight = (calendarHeight / 6) - strokeWidth - spacingV;
                rectangle.setHeight(rectangleHeight);
                stackPane.getChildren().add(rectangle);

                int calculatedDate = (j + 1) + (7 * i);
                if (calculatedDate > dateOffset) {
                    int currentDate = calculatedDate - dateOffset;
                    if (currentDate <= monthMaxDate) {
                        Text date = new Text(String.valueOf(currentDate));
                        double textTranslationY = -(rectangleHeight / 2) * 0.75;
                        date.setTranslateY(textTranslationY);
                        stackPane.getChildren().add(date);

                        if (today.getYear() == dateFocus.getYear() && today.getMonth() == dateFocus.getMonth() && today.getDayOfMonth() == currentDate) {
                            rectangle.setStroke(Color.BLUE);
                        }
                    }
                }
                calendar.getChildren().add(stackPane);
            }
        }
    }

    private void createCalendarActivity(List<CalendarActivity> calendarActivities, double rectangleHeight, double rectangleWidth, StackPane stackPane) {
        for (int k = 0; k < calendarActivities.size(); k++) {
            if (k >= 2) {
                Text moreActivities = new Text("...");
                stackPane.getChildren().add(moreActivities);
                moreActivities.setOnMouseClicked(mouseEvent -> {
                    //On ... click print all activities for given date
                    System.out.println(calendarActivities);
                });
                break;
            }
            CalendarActivity activity = calendarActivities.get(k);
            Text text = new Text(activity.getNom() + ", " + activity.getDescription());
            stackPane.getChildren().add(text);
            text.setOnMouseClicked(mouseEvent -> {
                //On Text clicked
                System.out.println(text.getText());
            });

            stackPane.setOnMouseClicked(event -> {
                eventDetailsTextArea.setText("Détails de l'événement sélectionné : \n" +
                        "Nom : " + activity.getNom() + "\n" +
                        "Description : " + activity.getDescription() + "\n" +
                        "Lieu : " + activity.getLieu() + "\n" +
                        "Nombre de places disponibles : " + activity.getNombre_des_places() + "\n" +
                        "Date : " + activity.getDate());
            });
        }
    }

    private void loadData() {
        ServiceEvent serviceEvent = new ServiceEvent();
        List<Event> evenements = null;  // Changement de Set à List

        try {
            evenements = serviceEvent.getAll();  // Correction pour retourner une List
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (evenements != null) {
            Map<LocalDate, List<CalendarActivity>> calendarActivityMap = new HashMap<>();
            for (Event event : evenements) {
                LocalDate date = event.getDate();
                List<CalendarActivity> activities = calendarActivityMap.getOrDefault(date, new ArrayList<>());
                activities.add(new CalendarActivity(
                        event.getId(),
                        event.getNom(),
                        event.getDescription(),
                        event.getEmplacement(),
                        event.getNombrePlaces(),
                        event.getDate()
                ));
                calendarActivityMap.put(date, activities);
            }

            double rectangleHeight = 40;
            double rectangleWidth = 40;
            for (Map.Entry<LocalDate, List<CalendarActivity>> entry : calendarActivityMap.entrySet()) {
                LocalDate date = entry.getKey();
                List<CalendarActivity> activities = entry.getValue();
                StackPane stackPane = findStackPaneForDate(date);

                if (stackPane != null) {
                    createCalendarActivity(activities, rectangleHeight, rectangleWidth, stackPane);
                }
            }
        }
    }

    private StackPane findStackPaneForDate(LocalDate date) {
        for (Node node : calendar.getChildren()) {
            if (node instanceof StackPane) {
                StackPane stackPane = (StackPane) node;
                for (Node child : stackPane.getChildren()) {
                    if (child instanceof Text) {
                        Text text = (Text) child;
                        try {
                            int dayOfMonth = Integer.parseInt(text.getText());
                            LocalDate stackPaneDate = LocalDate.of(dateFocus.getYear(), dateFocus.getMonth(), dayOfMonth);
                            if (stackPaneDate.equals(date)) {
                                return stackPane;
                            }
                        } catch (NumberFormatException e) {
                            // Ignorer les Text qui ne sont pas des jours valides
                        }
                    }
                }
            }
        }
        return null;
    }
}
