package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.model.Patient;

import java.util.ArrayList;
import java.util.List;

public class PatientController {

    public List<Patient> getAllPatients() {
        // Simule des patients récupérés depuis la base
        List<Patient> patients = new ArrayList<>();
        patients.add(new Patient("Sow", "Fatou"));
        patients.add(new Patient("Diop", "Mamadou"));
        patients.add(new Patient("Fall", "Aïssatou"));
        return patients;
    }
}
