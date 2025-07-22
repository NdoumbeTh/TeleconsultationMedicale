package epf.csi.examen.teleconsultation.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import epf.csi.examen.teleconsultation.model.Utilisateur;

public class UtilisateurDAO {
    private Connection connection;

    public UtilisateurDAO(Connection connection) {
        this.connection = connection;
    }

    // Méthode pour vérifier si un utilisateur existe par email
    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM utilisateurs WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    // Méthode pour sauvegarder un utilisateur
    public void save(Utilisateur utilisateur) throws SQLException {
        String sql = "INSERT INTO utilisateurs (nom, email, mot_de_passe, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getEmail());
            stmt.setString(3, utilisateur.getMotDePasse());
            stmt.setString(4, utilisateur.getRole());
            stmt.executeUpdate();
        }
    }

    // NOUVELLE MÉTHODE : Méthode pour créer un utilisateur avec retour boolean
    public boolean creer(Utilisateur utilisateur) {
        try {
            save(utilisateur);
            return true;
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création de l'utilisateur: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // NOUVELLE MÉTHODE : Lister les utilisateurs par rôles
    public List<Utilisateur> listerUtilisateursParRoles(String role1, String role2) {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs WHERE role = ? OR role = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, role1);
            stmt.setString(2, role2);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    utilisateurs.add(mapResultSetToUtilisateur(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs par rôles: " + e.getMessage());
            e.printStackTrace();
        }
        
        return utilisateurs;
    }

    // NOUVELLE MÉTHODE : Trouver un utilisateur par email
    public Utilisateur trouverParEmail(String email) {
        String sql = "SELECT * FROM utilisateurs WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUtilisateur(rs);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la recherche par email: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // Méthode pour trouver un utilisateur par email et mot de passe (pour l'authentification)
    public Utilisateur findByEmailAndPassword(String email, String motDePasse) throws SQLException {
        System.out.println("Recherche utilisateur avec email: " + email);
        System.out.println("Connexion fermée ? " + connection.isClosed());
        
        String sql = "SELECT * FROM utilisateurs WHERE email = ? AND mot_de_passe = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            stmt.setString(2, motDePasse);
            System.out.println("Exécution de la requête SQL: " + sql);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    System.out.println("Utilisateur trouvé: " + rs.getString("nom"));
                    return mapResultSetToUtilisateur(rs);
                } else {
                    System.out.println("Aucun utilisateur trouvé avec ces identifiants");
                }
            }
        }
        return null;
    }

    // Méthode pour trouver un utilisateur par ID
    public Utilisateur findById(int id) throws SQLException {
        String sql = "SELECT * FROM utilisateurs WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUtilisateur(rs);
                }
            }
        }
        return null;
    }

    // Méthode pour obtenir tous les utilisateurs
    public List<Utilisateur> findAll() throws SQLException {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String sql = "SELECT * FROM utilisateurs";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                utilisateurs.add(mapResultSetToUtilisateur(rs));
            }
        }
        return utilisateurs;
    }

    // Méthode pour mettre à jour un utilisateur
    public void update(Utilisateur utilisateur) throws SQLException {
        String sql = "UPDATE utilisateurs SET nom = ?, email = ?, mot_de_passe = ?, role = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, utilisateur.getNom());
            stmt.setString(2, utilisateur.getEmail());
            stmt.setString(3, utilisateur.getMotDePasse());
            stmt.setString(4, utilisateur.getRole());
            stmt.setInt(5, utilisateur.getId());
            stmt.executeUpdate();
        }
    }

    // Méthode pour supprimer un utilisateur
    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM utilisateurs WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Méthode helper pour mapper ResultSet vers Utilisateur
    private Utilisateur mapResultSetToUtilisateur(ResultSet rs) throws SQLException {
        Utilisateur utilisateur = new Utilisateur(
            rs.getString("nom"),
            rs.getString("email"),
            rs.getString("mot_de_passe"),
            rs.getString("role")
        );
        utilisateur.setId(rs.getInt("id"));
        return utilisateur;
    }
    
 // Méthode pour afficher tous les utilisateurs (médecins, patients, administrateurs)
    public void afficherTousLesUtilisateurs() {
        try {
            List<Utilisateur> tousLesUtilisateurs = findAll();
            
            if (tousLesUtilisateurs.isEmpty()) {
                System.out.println("Aucun utilisateur trouvé dans la base de données.");
                return;
            }
            
            System.out.println("=== LISTE DE TOUS LES UTILISATEURS ===");
            System.out.println("Total: " + tousLesUtilisateurs.size() + " utilisateur(s)");
            System.out.println();
            
            // Grouper par rôle pour un affichage organisé
            List<Utilisateur> medecins = new ArrayList<>();
            List<Utilisateur> patients = new ArrayList<>();
            List<Utilisateur> admins = new ArrayList<>();
            List<Utilisateur> autres = new ArrayList<>();
            
            for (Utilisateur user : tousLesUtilisateurs) {
                switch (user.getRole().toLowerCase()) {
                    case "medecin":
                    case "médecin":
                        medecins.add(user);
                        break;
                    case "patient":
                        patients.add(user);
                        break;
                    case "admin":
                    case "administrateur":
                        admins.add(user);
                        break;
                    default:
                        autres.add(user);
                        break;
                }
            }
            
            // Afficher les médecins
            if (!medecins.isEmpty()) {
                System.out.println("--- MÉDECINS (" + medecins.size() + ") ---");
                for (Utilisateur medecin : medecins) {
                    afficherUtilisateur(medecin);
                }
                System.out.println();
            }
            
            // Afficher les patients
            if (!patients.isEmpty()) {
                System.out.println("--- PATIENTS (" + patients.size() + ") ---");
                for (Utilisateur patient : patients) {
                    afficherUtilisateur(patient);
                }
                System.out.println();
            }
            
            // Afficher les administrateurs
            if (!admins.isEmpty()) {
                System.out.println("--- ADMINISTRATEURS (" + admins.size() + ") ---");
                for (Utilisateur admin : admins) {
                    afficherUtilisateur(admin);
                }
                System.out.println();
            }
            
            // Afficher les autres rôles
            if (!autres.isEmpty()) {
                System.out.println("--- AUTRES RÔLES (" + autres.size() + ") ---");
                for (Utilisateur autre : autres) {
                    afficherUtilisateur(autre);
                }
                System.out.println();
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de tous les utilisateurs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthode helper pour afficher les informations d'un utilisateur
    private void afficherUtilisateur(Utilisateur utilisateur) {
        System.out.printf("ID: %-3d | Nom: %-20s | Email: %-30s | Rôle: %s%n",
            utilisateur.getId(),
            utilisateur.getNom(),
            utilisateur.getEmail(),
            utilisateur.getRole()
        );
    }

    // Alternative: Méthode pour afficher tous les utilisateurs avec un format simple
    public void listerTousLesUtilisateurs() {
        try {
            List<Utilisateur> utilisateurs = findAll();
            
            System.out.println("=== TOUS LES UTILISATEURS ===");
            System.out.println("Total: " + utilisateurs.size() + " utilisateur(s)");
            System.out.println("-".repeat(80));
            
            for (Utilisateur user : utilisateurs) {
                System.out.println("• " + user.getNom() + " (" + user.getEmail() + ") - " + user.getRole().toUpperCase());
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération des utilisateurs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthode pour afficher les utilisateurs avec filtrage par rôle (optionnel)
    public void afficherUtilisateursParRole(String role) {
        try {
            List<Utilisateur> tousLesUtilisateurs = findAll();
            List<Utilisateur> utilisateursFiltres = new ArrayList<>();
            
            for (Utilisateur user : tousLesUtilisateurs) {
                if (user.getRole().equalsIgnoreCase(role)) {
                    utilisateursFiltres.add(user);
                }
            }
            
            if (utilisateursFiltres.isEmpty()) {
                System.out.println("Aucun utilisateur trouvé avec le rôle: " + role);
                return;
            }
            
            System.out.println("=== UTILISATEURS AVEC LE RÔLE: " + role.toUpperCase() + " ===");
            System.out.println("Total: " + utilisateursFiltres.size() + " utilisateur(s)");
            System.out.println("-".repeat(80));
            
            for (Utilisateur user : utilisateursFiltres) {
                afficherUtilisateur(user);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du filtrage des utilisateurs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Méthode pour obtenir des statistiques sur les utilisateurs
    public void afficherStatistiquesUtilisateurs() {
        try {
            List<Utilisateur> utilisateurs = findAll();
            
            int totalUtilisateurs = utilisateurs.size();
            int nbMedecins = 0;
            int nbPatients = 0;
            int nbAdmins = 0;
            int nbAutres = 0;
            
            for (Utilisateur user : utilisateurs) {
                switch (user.getRole().toLowerCase()) {
                    case "medecin":
                    case "médecin":
                        nbMedecins++;
                        break;
                    case "patient":
                        nbPatients++;
                        break;
                    case "admin":
                    case "administrateur":
                        nbAdmins++;
                        break;
                    default:
                        nbAutres++;
                        break;
                }
            }
            
            System.out.println("=== STATISTIQUES DES UTILISATEURS ===");
            System.out.println("Total des utilisateurs: " + totalUtilisateurs);
            System.out.println("• Médecins: " + nbMedecins);
            System.out.println("• Patients: " + nbPatients);
            System.out.println("• Administrateurs: " + nbAdmins);
            if (nbAutres > 0) {
                System.out.println("• Autres rôles: " + nbAutres);
            }
            System.out.println("-".repeat(40));
            
            if (totalUtilisateurs > 0) {
                System.out.printf("Pourcentage de médecins: %.1f%%%n", (nbMedecins * 100.0) / totalUtilisateurs);
                System.out.printf("Pourcentage de patients: %.1f%%%n", (nbPatients * 100.0) / totalUtilisateurs);
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors du calcul des statistiques: " + e.getMessage());
            e.printStackTrace();
        }
    }
 // Lister les patients liés à un médecin via les consultations
    public List<Utilisateur> listerPatientsParMedecin(int medecinId) throws SQLException {
        List<Utilisateur> patients = new ArrayList<>();
        String sql = "SELECT DISTINCT u.* FROM utilisateurs u " +
                     "JOIN consultations c ON u.id = c.id_patient " +
                     "WHERE c.id_medecin = ? AND u.role = 'patient'";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, medecinId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    patients.add(mapResultSetToUtilisateur(rs));
                }
            }
        }
        return patients;
    }

}