package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.dao.UtilisateurDAO;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import epf.csi.examen.teleconsultation.utils.DBConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserController {

    // Méthode pour créer un utilisateur - CORRIGÉE pour retourner boolean
    public boolean creerUtilisateur(String nom, String email, String motDePasse, String role) {
        try (Connection connection = DBConnection.getConnection()) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connection);
            
            // Vérifier si l'utilisateur existe déjà
            if (utilisateurDAO.existsByEmail(email)) {
                System.err.println("Un utilisateur avec cet email existe déjà.");
                return false;
            }
            
            // Créer le nouvel utilisateur
            Utilisateur nouvelUtilisateur = new Utilisateur(nom, email, motDePasse, role);
            return utilisateurDAO.creer(nouvelUtilisateur);
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'utilisateur: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour lister les admins et médecins - CORRIGÉE
    public List<Utilisateur> listerAdminsEtMedecins() {
        try (Connection connection = DBConnection.getConnection()) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connection);
            return utilisateurDAO.listerUtilisateursParRoles("admin", "medecin");
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>(); // Retourner une liste vide en cas d'erreur
        }
    }

    // Méthode pour authentifier un utilisateur
    public Utilisateur authentifier(String email, String motDePasse) {
        try (Connection connection = DBConnection.getConnection()) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connection);
            return utilisateurDAO.findByEmailAndPassword(email, motDePasse);
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'authentification: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Méthode pour obtenir tous les utilisateurs
    public List<Utilisateur> obtenirTousLesUtilisateurs() {
        try (Connection connection = DBConnection.getConnection()) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connection);
            return utilisateurDAO.findAll();
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les utilisateurs: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    // Méthode pour mettre à jour un utilisateur
    public boolean mettreAJourUtilisateur(Utilisateur utilisateur) {
        try (Connection connection = DBConnection.getConnection()) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connection);
            utilisateurDAO.update(utilisateur);
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour de l'utilisateur: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour supprimer un utilisateur
    public boolean supprimerUtilisateur(int id) {
        try (Connection connection = DBConnection.getConnection()) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connection);
            utilisateurDAO.delete(id);
            return true;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la suppression de l'utilisateur: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Méthode pour trouver un utilisateur par ID
    public Utilisateur trouverUtilisateurParId(int id) {
        try (Connection connection = DBConnection.getConnection()) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connection);
            return utilisateurDAO.findById(id);
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche de l'utilisateur par ID: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}