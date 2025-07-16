package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.dao.UtilisateurDAO;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import epf.csi.examen.teleconsultation.utils.DBConnection;
import epf.csi.examen.teleconsultation.view.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;

public class AuthController {
    private Connection connection;
    private UtilisateurDAO utilisateurDAO;

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
        Parent view = null;
        switch (utilisateur.getRole()) {
            case "admin":
                view = new DashboardAdminView().getView();
                break;
            case "medecin":
                view = new DashboardMedecinView().getView();
                break;
            case "patient":
                view = new DashboardPatientView().getView();
                break;
            default:
                System.out.println("Rôle non reconnu.");
                return;
        }
        primaryStage.setScene(new Scene(view, 800, 600));
    } else {
        System.out.println("Email ou mot de passe incorrect.");
    }
}
}
