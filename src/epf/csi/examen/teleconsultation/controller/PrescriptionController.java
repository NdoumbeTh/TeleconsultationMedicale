package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.dao.PrescriptionDAO;
import epf.csi.examen.teleconsultation.model.Prescription;
import epf.csi.examen.teleconsultation.utils.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class PrescriptionController {

    public void creerPrescription(Prescription prescription) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            PrescriptionDAO dao = new PrescriptionDAO(connection);
            dao.save(prescription);
        }
    }

    public List<Prescription> listerPrescriptionsMedecin(int medecinId) {
        try (Connection connection = DBConnection.getConnection()) {
            PrescriptionDAO dao = new PrescriptionDAO(connection);
            return dao.getPrescriptionsByMedecin(medecinId);
        } catch (SQLException e) {
            System.err.println("Erreur récupération prescriptions médecin : " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<Prescription> listerPrescriptionsPatient(int patientId) {
        try (Connection connection = DBConnection.getConnection()) {
            PrescriptionDAO dao = new PrescriptionDAO(connection);
            return dao.getPrescriptionsByPatient(patientId);
        } catch (SQLException e) {
            System.err.println("Erreur récupération prescriptions patient : " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public int countPrescriptionsByMedecin(int medecinId) {
        try (Connection connection = DBConnection.getConnection()) {
            PrescriptionDAO dao = new PrescriptionDAO(connection);
            return dao.countPrescriptionsByMedecin(medecinId);
        } catch (SQLException e) {
            System.err.println("Erreur comptage prescriptions médecin : " + e.getMessage());
            return 0;
        }
    }

    public int countPrescriptionsByPatient(int patientId) {
        try (Connection connection = DBConnection.getConnection()) {
            PrescriptionDAO dao = new PrescriptionDAO(connection);
            return dao.countPrescriptionsByPatient(patientId);
        } catch (SQLException e) {
            System.err.println("Erreur comptage prescriptions patient : " + e.getMessage());
            return 0;
        }
    }
}
