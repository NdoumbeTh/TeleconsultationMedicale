package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.dao.*;
import epf.csi.examen.teleconsultation.model.Utilisateur;

import java.util.List;

public class UserController {
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO(null);

    public boolean creerUtilisateur(String nom, String email, String mdp, String role) {
        if (!role.equals("admin") && !role.equals("medecin")) {
            return false; // empêcher d’autres rôles
        }
        Utilisateur u = new Utilisateur(nom, email, mdp, role);
        return utilisateurDAO.save(u);
    }

    public List<Utilisateur> listerAdminsEtMedecins() {
        return utilisateurDAO.listerUtilisateursParRoles("admin", "medecin");
    }

    public boolean inscrirePatient(String nom, String email, String mdp) {
        Utilisateur patient = new Utilisateur(nom, email, mdp, "patient");
        return utilisateurDAO.inscrire(patient);
    }
}
