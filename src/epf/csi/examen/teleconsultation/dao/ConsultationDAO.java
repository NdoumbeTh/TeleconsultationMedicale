package epf.csi.examen.teleconsultation.dao;

import epf.csi.examen.teleconsultation.model.Consultation;
import epf.csi.examen.teleconsultation.utils.DBConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConsultationDAO {

    private final Connection connection;

    public ConsultationDAO(Connection connection) {
        this.connection = connection;
    }


    // Créer une nouvelle consultation
public void save(Consultation consultation) throws SQLException {
    String sql = "INSERT INTO consultations (id_medecin, id_patient, date_heure, motif, type, statut, lien_teleconsultation) VALUES ( ?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        stmt.setInt(1, consultation.getIdMedecin());
        stmt.setInt(2, consultation.getIdPatient());
        stmt.setTimestamp(3, Timestamp.valueOf(consultation.getDateHeure()));
        stmt.setString(4, consultation.getMotif());
        stmt.setString(5, consultation.getType());
        stmt.setString(6, consultation.getStatut());
        stmt.setString(7, consultation.getLienTeleconsultation());

        int affectedRows = stmt.executeUpdate(); // ✅ UNE SEULE FOIS
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

    // Trouver consultation par ID
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
            stmt.setString(7, consultation.getLienTeleconsultation());
            stmt.setInt(8, consultation.getId());
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

    // Lister consultations d’un médecin (ordre décroissant)
    public List<Consultation> listerConsultationsMedecin(int medecinId) throws SQLException {
        List<Consultation> consultations = new ArrayList<>();
        String sql = "SELECT c.*, p.nom AS nom_patient, m.nom AS nom_medecin " +
                     "FROM consultations c " +
                     "JOIN utilisateurs p ON c.id_patient = p.id " +
                     "JOIN utilisateurs m ON c.id_medecin = m.id " +
                     "WHERE c.id_medecin = ? " +
                     "ORDER BY c.date_heure DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, medecinId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultations.add(mapResultSetToConsultation(rs));
                }
            }
        }
        return consultations;
    }

    // Lister consultations d’un patient (ordre décroissant)
    public List<Consultation> listerConsultationsPatient(int patientId) throws SQLException {
        List<Consultation> consultations = new ArrayList<>();
        String sql = "SELECT c.*, p.nom AS nom_patient, m.nom AS nom_medecin " +
                     "FROM consultations c " +
                     "JOIN utilisateurs p ON c.id_patient = p.id " +
                     "JOIN utilisateurs m ON c.id_medecin = m.id " +
                     "WHERE c.id_patient = ? " +
                     "ORDER BY c.date_heure DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    consultations.add(mapResultSetToConsultation(rs));
                }
            }
        }
        return consultations;
    }

    // Compter consultations par médecin
    public int countConsultationsByMedecin(int medecinId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM consultations WHERE id_medecin = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, medecinId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    // Compter consultations par patient
    public int countConsultationsByPatient(int patientId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM consultations WHERE id_patient = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    // Mapper ResultSet -> Consultation
    private Consultation mapResultSetToConsultation(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        int idPatient = rs.getInt("id_patient");
        int idMedecin = rs.getInt("id_medecin");
        LocalDateTime dateHeure = rs.getTimestamp("date_heure").toLocalDateTime();
        String type = rs.getString("type");
        String statut = rs.getString("statut");
        String motif = rs.getString("motif");
        String nomPatient = rs.getString("nom_patient");
        String nomMedecin = rs.getString("nom_medecin");
        String lien = rs.getString("lien_teleconsultation");


        Consultation consultation = new Consultation(id, idPatient, idMedecin, dateHeure, type, statut, motif, nomPatient, nomMedecin);
        consultation.setLienTeleconsultation(lien);
        return consultation;



    }
}
