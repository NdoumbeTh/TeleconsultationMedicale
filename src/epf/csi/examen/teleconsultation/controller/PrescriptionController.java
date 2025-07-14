package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.model.Prescription;

import java.util.ArrayList;
import java.util.List;

public class PrescriptionController {

    private List<Prescription> prescriptions = new ArrayList<>();

    public void creerPrescription(Prescription p) {
        prescriptions.add(p);
    }

    public List<Prescription> getPrescriptionsParPatient(int patientId) {
        return prescriptions.stream()
                .filter(p -> p.getPatientId() == patientId)
                .toList();
    }
}
