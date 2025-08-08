package epf.csi.examen.teleconsultation.dao;

import epf.csi.examen.teleconsultation.model.Utilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PatientDAO {

    private final Connection connection;

    public PatientDAO(Connection connection) {
        this.connection = connection;
    }

public ObservableList<Utilisateur> getAllPatients() {
    ObservableList<Utilisateur> patients = FXCollections.observableArrayList();
    String query = "SELECT * FROM utilisateurs WHERE role = 'patient'";

    try (PreparedStatement stmt = connection.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Utilisateur patient = new Utilisateur(
                rs.getInt("id"),
                rs.getString("nom"),    // utilisé comme nom complet
                rs.getString("email"),
                null,                   // pas de champ `nom` séparé si `prenom` est utilisé pour cela
                rs.getString("role")
            );
            patients.add(patient);
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return patients;
}
}
