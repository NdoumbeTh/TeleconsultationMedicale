package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.dao.RendezVousDAO;
import epf.csi.examen.teleconsultation.model.RendezVous;
import epf.csi.examen.teleconsultation.utils.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RendezVousController {

    // Créer un rendez-vous
    public boolean creerRendezVous(int medecinId, int patientId, LocalDateTime dateHeure, String statut) {
        try (Connection connection = DBConnection.getConnection()) {
            RendezVousDAO rdvDAO = new RendezVousDAO(connection);
            RendezVous rdv = new RendezVous(patientId, medecinId, dateHeure, statut);
            rdvDAO.save(rdv);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur création rendez-vous : " + e.getMessage());
            return false;
        }
    }

    // Modifier un rendez-vous
    public boolean modifierRendezVous(RendezVous rdv) {
        try (Connection connection = DBConnection.getConnection()) {
            RendezVousDAO rdvDAO = new RendezVousDAO(connection);
            rdvDAO.update(rdv);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur modification rendez-vous : " + e.getMessage());
            return false;
        }
    }

    // Supprimer un rendez-vous
    public boolean supprimerRendezVous(int id) {
        try (Connection connection = DBConnection.getConnection()) {
            RendezVousDAO rdvDAO = new RendezVousDAO(connection);
            rdvDAO.delete(id);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur suppression rendez-vous : " + e.getMessage());
            return false;
        }
    }

    // Lister les rendez-vous du médecin avec nom patient
    public List<RendezVous> listerRendezVousMedecin(int medecinId) {
        List<RendezVous> list = new ArrayList<>();
        String sql = "SELECT rv.*, u.nom AS patient_nom FROM rendez_vous rv " +
                     "JOIN utilisateurs u ON rv.patient_id = u.id " +
                     "WHERE rv.medecin_id = ? ORDER BY rv.date_heure DESC";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setInt(1, medecinId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    int patientId = rs.getInt("patient_id");
                    int medecinIdResult = rs.getInt("medecin_id");
                    Timestamp ts = rs.getTimestamp("date_heure");
                    LocalDateTime dateHeure = ts != null ? ts.toLocalDateTime() : null;
                    String statut = rs.getString("statut");
                    String patientNom = rs.getString("patient_nom");

                    RendezVous rdv = new RendezVous(id, patientId, medecinIdResult, dateHeure, statut, patientNom);
                    list.add(rdv);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur récupération rendez-vous médecin : " + e.getMessage());
            return Collections.emptyList();
        }
        return list;
    }

    // Lister les rendez-vous d'un patient (sans nom car il est connu)
    public List<RendezVous> listerRendezVousPatient(int patientId) {
        try (Connection connection = DBConnection.getConnection()) {
            RendezVousDAO rdvDAO = new RendezVousDAO(connection);
            return rdvDAO.listerRendezVousPatient(patientId);
        } catch (SQLException e) {
            System.err.println("Erreur récupération rendez-vous patient : " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
