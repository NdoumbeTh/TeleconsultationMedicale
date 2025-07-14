package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.model.RendezVous;

import java.util.ArrayList;
import java.util.List;

public class RendezVousController {

    private List<RendezVous> rdvSimules = new ArrayList<>();

    public List<RendezVous> getRendezVousParPatient(int patientId) {
        // Simulation — à remplacer par une requête JDBC
        return rdvSimules.stream()
                .filter(rdv -> rdv.getPatientId() == patientId)
                .toList();
    }

    public void creerRendezVous(RendezVous rdv) {
        // Simulation d'ajout
        rdvSimules.add(rdv);
    }

    public void annulerRendezVous(int idRdv) {
        rdvSimules.removeIf(r -> r.getId() == idRdv);
    }
}
