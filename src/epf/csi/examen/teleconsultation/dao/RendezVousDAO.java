package epf.csi.examen.teleconsultation.dao;

import epf.csi.examen.teleconsultation.model.RendezVous;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class RendezVousDAO {

    private final Connection connection;

    public RendezVousDAO(Connection connection) {
        this.connection = connection;
    }

    // Créer un rendez-vous
    public void save(RendezVous rdv) throws SQLException {
        String sql = "INSERT INTO rendez_vous (patient_id, medecin_id, date_heure, statut) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, rdv.getPatientId());
            stmt.setInt(2, rdv.getMedecinId());
            stmt.setTimestamp(3, Timestamp.valueOf(rdv.getDateHeure()));
            stmt.setString(4, rdv.getStatut());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Erreur création rendez-vous, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    rdv.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Modifier un rendez-vous
    public void update(RendezVous rdv) throws SQLException {
        String sql = "UPDATE rendez_vous SET patient_id = ?, medecin_id = ?, date_heure = ?, statut = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, rdv.getPatientId());
            stmt.setInt(2, rdv.getMedecinId());
            stmt.setTimestamp(3, Timestamp.valueOf(rdv.getDateHeure()));
            stmt.setString(4, rdv.getStatut());
            stmt.setInt(5, rdv.getId());
            stmt.executeUpdate();
        }
    }

    // Supprimer un rendez-vous par ID
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM rendez_vous WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Trouver un rendez-vous par ID
    public RendezVous findById(int id) throws SQLException {
        String sql = "SELECT * FROM rendez_vous WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToRendezVous(rs);
                } else {
                    return null;
                }
            }
        }
    }

    // Lister tous les rendez-vous d’un médecin
    public List<RendezVous> listerRendezVousMedecin(int medecinId) throws SQLException {
        List<RendezVous> list = new ArrayList<>();
        String sql = "SELECT * FROM rendez_vous WHERE medecin_id = ? ORDER BY date_heure DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, medecinId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToRendezVous(rs));
                }
            }
        }
        return list;
    }

    private RendezVous mapResultSetToRendezVous(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int patientId = rs.getInt("patient_id");
        int medecinId = rs.getInt("medecin_id");
        Timestamp ts = rs.getTimestamp("date_heure");
        LocalDateTime dateHeure = ts != null ? ts.toLocalDateTime() : null;
        String statut = rs.getString("statut");

        return new RendezVous(id, patientId, medecinId, dateHeure, statut);
    }
    public List<RendezVous> listerRendezVousPatient(int patientId) throws SQLException {
        List<RendezVous> list = new ArrayList<>();
        String sql = "SELECT * FROM rendez_vous WHERE patient_id = ? ORDER BY date_heure DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    list.add(mapResultSetToRendezVous(rs));
                }
            }
        }
        return list;
    }

}
