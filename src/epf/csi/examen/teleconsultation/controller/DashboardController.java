package epf.csi.examen.teleconsultation.controller;

public class DashboardController {

    public String getMessageBienvenue(String nomUtilisateur) {
        return "Bienvenue, " + nomUtilisateur + " 👋";
    }

    public int getNombreRendezVousAVenir(int patientId) {
        // Simulé — à remplacer par un SELECT COUNT
        return 2;
    }

    public int getNombreConsultationsEffectuees(int patientId) {
        return 4;
    }

    public String getDerniereConsultation(int patientId) {
        return "Le 25 juin 2025";
    }
}
