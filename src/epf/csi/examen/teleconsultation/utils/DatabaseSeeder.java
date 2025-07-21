package epf.csi.examen.teleconsultation.utils;

import java.sql.Connection;
import java.sql.SQLException;

import epf.csi.examen.teleconsultation.dao.UtilisateurDAO;
import epf.csi.examen.teleconsultation.model.Utilisateur;

public class DatabaseSeeder {
    
    public static void seedAdminIfNotExists() {
        try (Connection connection = DBConnection.getConnection()) {
            UtilisateurDAO utilisateurDAO = new UtilisateurDAO(connection);
            
            if (!utilisateurDAO.existsByEmail("admin@carelinker.com")) {
                Utilisateur admin = new Utilisateur("Admin", "admin@carelinker.com", "admin123", "admin");
                utilisateurDAO.save(admin);
                System.out.println("Admin par défaut créé.");
            } else {
                System.out.println("Admin existe déjà.");
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'admin : " + e.getMessage());
            e.printStackTrace();
        }
    }
}