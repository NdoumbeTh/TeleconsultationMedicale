package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.dao.PrescriptionDAO;
import epf.csi.examen.teleconsultation.model.Prescription;
import epf.csi.examen.teleconsultation.utils.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PrescriptionController {

    public void creerPrescription(Prescription prescription) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            PrescriptionDAO dao = new PrescriptionDAO(connection);
            dao.save(prescription);
        }
    }

    public List<Prescription> listerPrescriptionsMedecin(int medecinId) throws SQLException {
        try (Connection connection = DBConnection.getConnection()) {
            PrescriptionDAO dao = new PrescriptionDAO(connection);
            return dao.getPrescriptionsByMedecin(medecinId);
        }
    }
}
