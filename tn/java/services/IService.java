package tn.java.services;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public interface IService <T> {
    public void ajouter(T t) throws SQLException;
    public void modifier(T t) throws SQLException;
    public void supprimer(int id) throws SQLException;
    public T getOneById(int id) throws SQLException;
    public Set<T> getAll() throws SQLException;
    public List<T> afficher() throws SQLException;
}
