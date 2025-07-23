package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.dao.PatientDAO;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import javafx.collections.ObservableList;

import java.sql.Connection;

public class PatientController {

    private final PatientDAO patientDAO;

    public PatientController(Connection connection) {
        this.patientDAO = new PatientDAO(connection);
    }

    public ObservableList<Utilisateur> getAllPatients() {
        return patientDAO.getAllPatients();
    }
}
