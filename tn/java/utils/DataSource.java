package tn.java.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataSource {
    private final String URL="jdbc:mysql://localhost:3306/controle";
    private final String USER="root";
    private final String PASSWD="";
    private Connection cnx;
    private static DataSource instance;

    private DataSource()
    {


        try {
            cnx = DriverManager.getConnection(URL, USER, PASSWD);
            System.out.println("Connexion établie avec succès à la base de données.");
        } catch (SQLException e) {
            System.err.println("Erreur lors de la connexion à la base de données : " + e.getMessage());

        }

    }

    public static  DataSource getInstance() {
        if(instance==null)
        {
            instance=new DataSource();
        }
        return  instance;

    }

    public Connection getCnx() {
        return this.cnx;
    }
}
