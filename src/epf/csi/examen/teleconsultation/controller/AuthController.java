package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.dao.UtilisateurDAO;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import epf.csi.examen.teleconsultation.utils.DBConnection;
import epf.csi.examen.teleconsultation.view.DashboardAdminView;
import epf.csi.examen.teleconsultation.view.DashboardMedecinView;
import epf.csi.examen.teleconsultation.view.DashboardPatientView;
import javafx.stage.Stage;

import java.sql.Connection;

public class AuthController {
    private final Connection connection;
    private final UtilisateurDAO utilisateurDAO;

    public AuthController() {
        this.connection = DBConnection.getConnection();
        this.utilisateurDAO = new UtilisateurDAO(connection);
    }

    public boolean inscription(String nom, String email, String motDePasse, String role) {
        Utilisateur utilisateur = new Utilisateur(nom, email, motDePasse, role);
        return utilisateurDAO.inscrire(utilisateur);
    }

    public void connexion(String email, String motDePasse, Stage primaryStage) {
        Utilisateur utilisateur = utilisateurDAO.connexion(email, motDePasse);
        if (utilisateur != null) {
            switch (utilisateur.getRole().toLowerCase()) {
                case "admin":
                    // Appelle directement le constructeur qui affiche la vue admin
                    new DashboardAdminView(primaryStage);
                    break;
                case "medecin":
                    new DashboardMedecinView(primaryStage);
                    break;
                case "patient":
                    new DashboardPatientView(primaryStage);
                    break;
                default:
                    System.err.println("Rôle non reconnu : " + utilisateur.getRole());
            }
        } else {
            System.err.println("Email ou mot de passe incorrect.");
        }
    }
}
