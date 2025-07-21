package epf.csi.examen.teleconsultation.utils;

import epf.csi.examen.teleconsultation.dao.UtilisateurDAO;
import epf.csi.examen.teleconsultation.model.Utilisateur;

public class DatabaseSeeder {
    public static void seedAdminIfNotExists() {
        UtilisateurDAO utilisateurDAO = new UtilisateurDAO(null);
        if (!utilisateurDAO.existsByEmail("admin@carelinker.com")) {
            Utilisateur admin = new Utilisateur("Admin", "admin@carelinker.com", "admin123", "admin");
            utilisateurDAO.save(admin);
            System.out.println("Admin par défaut créé.");
        }
    }
}

