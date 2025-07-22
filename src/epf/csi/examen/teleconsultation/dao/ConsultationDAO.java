package epf.csi.examen.teleconsultation.dao;

import epf.csi.examen.teleconsultation.model.Consultation;
import epf.csi.examen.teleconsultation.utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class ConsultationDAO {

    private final Connection connection;

    public ConsultationDAO(Connection connection) {
        this.connection = connection;
    }

    // Créer une nouvelle consultation
    public void save(Consultation consultation) throws SQLException {
        String sql = "INSERT INTO consultations (id_patient, id_medecin, date_heure, type, statut, motif) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, consultation.getIdPatient());
            stmt.setInt(2, consultation.getIdMedecin());
            stmt.setTimestamp(3, Timestamp.valueOf(consultation.getDateHeure()));
            stmt.setString(4, consultation.getType());
            stmt.setString(5, consultation.getStatut());
            stmt.setString(6, consultation.getMotif());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Échec de la création de la consultation, aucune ligne affectée.");
            }
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    consultation.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Lire une consultation par son ID
    public Consultation findById(int id) throws SQLException {
        String sql = "SELECT c.*, p.nom AS nom_patient, m.nom AS nom_medecin " +
                     "FROM consultations c " +
                     "JOIN utilisateurs p ON c.id_patient = p.id " +
                     "JOIN utilisateurs m ON c.id_medecin = m.id " +
                     "WHERE c.id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToConsultation(rs);
                } else {
                    return null;
                }
            }
        }
    }

    // Mettre à jour une consultation
    public void update(Consultation consultation) throws SQLException {
        String sql = "UPDATE consultations SET id_patient = ?, id_medecin = ?, date_heure = ?, type = ?, statut = ?, motif = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, consultation.getIdPatient());
            stmt.setInt(2, consultation.getIdMedecin());
            stmt.setTimestamp(3, Timestamp.valueOf(consultation.getDateHeure()));
            stmt.setString(4, consultation.getType());
            stmt.setString(5, consultation.getStatut());
            stmt.setString(6, consultation.getMotif());
            stmt.setInt(7, consultation.getId());
            stmt.executeUpdate();
        }
    }

    // Supprimer une consultation par ID
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM consultations WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Lister toutes les consultations d’un médecin, avec nom patient pour affichage
    public List<Consultation> listerConsultationsMedecin(int idMedecin) throws SQLException {
        List<Consultation> consultations = new ArrayList<>();
        String sql = "SELECT c.*, p.nom AS nom_patient, m.nom AS nom_medecin " +
                     "FROM consultations c " +
                     "JOIN utilisateurs p ON c.id_patient = p.id " +
                     "JOIN utilisateurs m ON c.id_medecin = m.id " +
                     "WHERE c.id_medecin = ? " +
                     "ORDER BY c.date_heure DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, idMedecin);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultations.add(mapResultSetToConsultation(rs));
                }
            }
        }
        return consultations;
    }

    // Méthode utilitaire pour mapper un ResultSet en Consultation
    private Consultation mapResultSetToConsultation(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int idPatient = rs.getInt("id_patient");
        int idMedecin = rs.getInt("id_medecin");
        Timestamp ts = rs.getTimestamp("date_heure");
        LocalDateTime dateHeure = ts.toLocalDateTime();
        String type = rs.getString("type");
        String statut = rs.getString("statut");
        String motif = rs.getString("motif");
        String nomPatient = rs.getString("nom_patient");
        String nomMedecin = rs.getString("nom_medecin");

        return new Consultation(id, idPatient, idMedecin, dateHeure, type, statut, motif, nomPatient, nomMedecin);
    }
}
