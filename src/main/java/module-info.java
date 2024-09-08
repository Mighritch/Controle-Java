module com.example.controle {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires java.sql;
    requires java.desktop;

    opens tn.java.Tests to javafx.fxml;
    opens tn.java.entities to javafx.base;
    opens tn.java.services to javafx.base;
    opens tn.java.controllers to javafx.fxml;


    exports tn.java.Tests;
    exports tn.java.entities;
    exports tn.java.services;
}
