package epf.csi.examen.teleconsultation.controller;

import epf.csi.examen.teleconsultation.model.Utilisateur;
import epf.csi.examen.teleconsultation.utils.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UtilisateurController {

    public ObservableList<Utilisateur> getAllMedecins() {
        ObservableList<Utilisateur> medecins = FXCollections.observableArrayList();
        String sql = "SELECT id, nom, email FROM utilisateurs WHERE role = 'medecin'";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setId(rs.getInt("id"));
                u.setNom(rs.getString("nom"));
                u.setEmail(rs.getString("email"));
                medecins.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return medecins;
    }
    public static List<Utilisateur> obtenirTousLesUtilisateurs() {
        List<Utilisateur> liste = new ArrayList<>();
        String sql = "SELECT id, nom, email, role FROM utilisateurs";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setId(rs.getInt("id"));
                u.setNom(rs.getString("nom"));
                u.setEmail(rs.getString("email"));
                u.setRole(rs.getString("role"));
                liste.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return liste;
    }
    public static boolean creerUtilisateur(String nom, String email, String mot_de_passe, String role) {
        String sql = "INSERT INTO utilisateurs (nom, email, mot_de_passe, role) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nom);
            ps.setString(2, email);
            ps.setString(3, mot_de_passe);
            ps.setString(4, role);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean supprimerUtilisateur(int id) {
        String sql = "DELETE FROM utilisateurs WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static boolean mettreAJourUtilisateur(Utilisateur user) {
        String sql = "UPDATE utilisateurs SET nom = ?, email = ?, role = ? WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getNom());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getRole());
            ps.setInt(4, user.getId());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
