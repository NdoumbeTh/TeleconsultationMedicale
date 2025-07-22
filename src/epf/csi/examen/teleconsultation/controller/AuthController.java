package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.dao.UtilisateurDAO;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import epf.csi.examen.teleconsultation.utils.DBConnection;
import epf.csi.examen.teleconsultation.view.DashboardAdminView;
import epf.csi.examen.teleconsultation.view.DashboardMedecinView;
import epf.csi.examen.teleconsultation.view.DashboardPatientView;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class AuthController {

    public AuthController() {
        // Constructeur vide, on créera les connexions à la demande
    }

    public boolean inscription(String nom, String email, String motDePasse, String role) {
        try (Connection connection = DBConnection.getConnection()) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connection);
            
            // Vérifier si l'utilisateur existe déjà
            if (utilisateurDAO.existsByEmail(email)) {
                System.err.println("Un utilisateur avec cet email existe déjà.");
                return false;
            }
            
            Utilisateur utilisateur = new Utilisateur(nom, email, motDePasse, role);
            utilisateurDAO.save(utilisateur);
            System.out.println("Inscription réussie pour : " + email);
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'inscription : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

public void connexion(String email, String motDePasse, Stage primaryStage) {
    try (Connection connection = DBConnection.getConnection()) {
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connection);
        
        Utilisateur utilisateur = utilisateurDAO.findByEmailAndPassword(email, motDePasse);
        
        if (utilisateur != null) {
            System.out.println("Connexion réussie pour : " + utilisateur.getNom() + " (" + utilisateur.getRole() + ")");
            
            switch (utilisateur.getRole().toLowerCase()) {
            case "admin":
                new DashboardAdminView(primaryStage);
                break;
            case "medecin":
                new DashboardMedecinView(primaryStage, utilisateur);  // <-- ici
                break;
            case "patient":
                new DashboardPatientView(utilisateur.getId()).start(primaryStage);
                break;
            default:
                System.err.println("Rôle non reconnu : " + utilisateur.getRole());
            }

        } else {
            System.err.println("Email ou mot de passe incorrect.");
        }
        
    } catch (SQLException e) {
        System.err.println("Erreur lors de la connexion : " + e.getMessage());
        e.printStackTrace();
    }
}

    // Méthode utilitaire pour vérifier si un utilisateur existe
    public boolean utilisateurExiste(String email) {
        try (Connection connection = DBConnection.getConnection()) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connection);
            return utilisateurDAO.existsByEmail(email);
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour obtenir un utilisateur par email (utile pour d'autres fonctionnalités)
    public Utilisateur getUtilisateurByEmail(String email) {
        try (Connection connection = DBConnection.getConnection()) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connection);
            // On ne peut pas directement chercher par email seul, donc on retourne null
            // Cette méthode pourrait être améliorée en ajoutant une méthode findByEmail dans le DAO
            return null;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'utilisateur : " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}