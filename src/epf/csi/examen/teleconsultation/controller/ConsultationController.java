package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.dao.ConsultationDAO;
import epf.csi.examen.teleconsultation.dao.MessageDAO;
import epf.csi.examen.teleconsultation.dao.UtilisateurDAO;
import epf.csi.examen.teleconsultation.model.Consultation;
import epf.csi.examen.teleconsultation.model.Medecin;
import epf.csi.examen.teleconsultation.model.Message;
import epf.csi.examen.teleconsultation.model.Patient;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import epf.csi.examen.teleconsultation.utils.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ConsultationController {

    // Créer une nouvelle consultation, retourne true si succès
public boolean creerConsultation(Utilisateur patient, Utilisateur medecin, LocalDateTime dateHeure, String motif) {
    String roomName = "carelinker_" + UUID.randomUUID();
    String jitsiLink = "https://meet.jit.si/" + roomName;

    Consultation consultation = new Consultation();
    consultation.setNomPatient(patient.getNom());
    consultation.setIdMedecin(medecin.getId());
    consultation.setIdPatient(patient.getId());
    consultation.setDateHeure(dateHeure);
    consultation.setMotif(motif);
    consultation.setType("téléconsultation");
    consultation.setStatut("planifiée");
    consultation.setLienTeleconsultation(jitsiLink);

    try (Connection conn = DBConnection.getConnection()) {
        ConsultationDAO consultationDAO = new ConsultationDAO(conn);
        consultationDAO.save(consultation);

        // Envoi du message
        Message message = new Message();
        message.setExpediteurId(medecin.getId());
        message.setDestinataireId(patient.getId());
        message.setObjet("Lien de téléconsultation");
        message.setContenu("Bonjour " + patient.getNom() + ",\n\nVoici le lien pour votre téléconsultation : " + jitsiLink);
        message.setDateEnvoi(LocalDateTime.now());
        message.setLu(false);

        MessageDAO dao = new MessageDAO();
        dao.envoyerMessage(message);

        return true;
    } catch (SQLException e) {
        System.err.println("Erreur lors de la création de la consultation : " + e.getMessage());
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

    // Lister les consultations d'un patient
    public List<Consultation> listerConsultationsPatient(int patientId) {
        try (Connection connection = DBConnection.getConnection()) {
            ConsultationDAO consultationDAO = new ConsultationDAO(connection);
            return consultationDAO.listerConsultationsPatient(patientId);
        } catch (SQLException e) {
            System.err.println("Erreur récupération consultations patient : " + e.getMessage());
            return Collections.emptyList();
        }
    }

    // Compter le nombre de consultations d’un patient
    public int countConsultationsByPatient(int patientId) {
        try (Connection connection = DBConnection.getConnection()) {
            ConsultationDAO consultationDAO = new ConsultationDAO(connection);
            return consultationDAO.countConsultationsByPatient(patientId);
        } catch (SQLException e) {
            System.err.println("Erreur comptage consultations patient : " + e.getMessage());
            return 0;
        }
    }

    public List<Utilisateur> listerPatientsDuMedecin(int medecinId) {
        try (Connection connection = DBConnection.getConnection()) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connection);
            return utilisateurDAO.listerPatientsParMedecin(medecinId);
        } catch (SQLException e) {
            System.err.println("Erreur récupération patients : " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
