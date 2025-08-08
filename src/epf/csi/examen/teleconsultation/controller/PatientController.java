package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.dao.PatientDAO;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import epf.csi.examen.teleconsultation.utils.DBConnection;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class PatientController {

    private final PatientDAO patientDAO;

    public PatientController(Connection connection) {
        this.patientDAO = new PatientDAO(connection);
    }

    public ObservableList<Utilisateur> getAllPatients() {
        return patientDAO.getAllPatients();
    }
    public Map<Integer, Integer> getUnreadMessageCounts(int medecinId) {
        Map<Integer, Integer> unreadCounts = new HashMap<>();

        String sql = "SELECT expediteur_id, COUNT(*) as count FROM messages " +
                     "WHERE destinataire_id = ? AND lu = 0 GROUP BY expediteur_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, medecinId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                int expediteurId = rs.getInt("expediteur_id");
                int count = rs.getInt("count");
                unreadCounts.put(expediteurId, count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return unreadCounts;
    }

}
