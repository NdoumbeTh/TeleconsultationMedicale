package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.dao.RendezVousDAO;
import epf.csi.examen.teleconsultation.model.RendezVous;
import epf.csi.examen.teleconsultation.utils.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
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

    // Lister les rendez-vous du médecin
    public List<RendezVous> listerRendezVousMedecin(int medecinId) {
        try (Connection connection = DBConnection.getConnection()) {
            RendezVousDAO rdvDAO = new RendezVousDAO(connection);
            return rdvDAO.listerRendezVousMedecin(medecinId);
        } catch (SQLException e) {
            System.err.println("Erreur récupération rendez-vous : " + e.getMessage());
            return Collections.emptyList();
        }
    }
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
