package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.dao.ConsultationDAO;
import epf.csi.examen.teleconsultation.dao.UtilisateurDAO;
import epf.csi.examen.teleconsultation.model.Consultation;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import epf.csi.examen.teleconsultation.utils.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class ConsultationController {

    // Créer une nouvelle consultation, retourne true si succès
public boolean creerConsultation(int medecinId, int patientId, 
                                 java.time.LocalDateTime dateConsultation, String motif) {
    try (Connection connection = DBConnection.getConnection()) {
        ConsultationDAO consultationDAO = new ConsultationDAO(connection);

        Consultation consultation = new Consultation(patientId, patientId, patientId, dateConsultation, motif, motif, motif);
        consultation.setIdMedecin(medecinId);
        consultation.setIdPatient(patientId);
        consultation.setDateHeure(dateConsultation);
        consultation.setMotif(motif);  // tu peux adapter selon usage

        // Tu peux aussi définir d'autres propriétés si besoin, par ex :
        consultation.setType("Consultation"); 
        consultation.setStatut("En attente");

        consultationDAO.save(consultation);
        return true;
    } catch (SQLException e) {
        System.err.println("Erreur création consultation : " + e.getMessage());
        return false;
    }
}

    // Modifier une consultation existante
    public boolean modifierConsultation(Consultation consultation) {
        try (Connection connection = DBConnection.getConnection()) {
            ConsultationDAO consultationDAO = new ConsultationDAO(connection);
            consultationDAO.update(consultation);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la modification de la consultation : " + e.getMessage());
            return false;
        }
    }

    // Lister toutes les consultations du médecin connecté
    public List<Consultation> listerConsultationsMedecin(int medecinId) {
        try (Connection connection = DBConnection.getConnection()) {
            ConsultationDAO consultationDAO = new ConsultationDAO(connection);
            return consultationDAO.listerConsultationsMedecin(medecinId);
        } catch (SQLException e) {
            System.err.println("Erreur récupération consultations : " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Lister les patients liés à un médecin (à implémenter dans UtilisateurDAO)
    public List<Utilisateur> listerPatientsDuMedecin(int medecinId) {
        try (Connection connection = DBConnection.getConnection()) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connection);
            return utilisateurDAO.listerPatientsParMedecin(medecinId);
        } catch (SQLException e) {
            System.err.println("Erreur récupération patients : " + e.getMessage());
            return Collections.emptyList();
        }
    }
    public List<Consultation> listerConsultationsPatient(int patientId) {
        try (Connection connection = DBConnection.getConnection()) {
            ConsultationDAO consultationDAO = new ConsultationDAO(connection);
            return consultationDAO.listerConsultationsPatient(patientId);
        } catch (SQLException e) {
            System.err.println("Erreur récupération consultations patient : " + e.getMessage());
            return Collections.emptyList();
        }
    }

}
