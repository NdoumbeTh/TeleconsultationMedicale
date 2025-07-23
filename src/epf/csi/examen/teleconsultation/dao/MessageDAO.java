package epf.csi.examen.teleconsultation.dao;

import epf.csi.examen.teleconsultation.model.Message;
import epf.csi.examen.teleconsultation.model.Utilisateur;
import epf.csi.examen.teleconsultation.utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {

    public void envoyerMessage(Message message) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO messages (expediteur_id, destinataire_id, contenu, date_envoi, lu) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, message.getExpediteurId());
            ps.setInt(2, message.getDestinataireId());
            ps.setString(3, message.getContenu());
            ps.setTimestamp(4, Timestamp.valueOf(message.getDateEnvoi()));
            ps.setBoolean(5, message.isLu());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Message> getMessagesEntreUtilisateurs(int user1Id, int user2Id) {
        List<Message> messages = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection()) {
            String sql = """
                SELECT * FROM messages 
                WHERE (expediteur_id = ? AND destinataire_id = ?) 
                   OR (expediteur_id = ? AND destinataire_id = ?) 
                ORDER BY date_envoi
            """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, user1Id);
            ps.setInt(2, user2Id);
            ps.setInt(3, user2Id);
            ps.setInt(4, user1Id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                messages.add(mapResultSetToMessage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public List<Message> getMessagesRecus(int destinataireId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM messages WHERE destinataire_id = ? ORDER BY date_envoi DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, destinataireId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add(mapResultSetToMessage(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    public void marquerMessageCommeLu(int messageId) {
        String sql = "UPDATE messages SET lu = true WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, messageId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void marquerCommeLu(int expediteurId, int destinataireId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE messages SET lu = true WHERE expediteur_id = ? AND destinataire_id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, expediteurId);
            ps.setInt(2, destinataireId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int countMessagesNonLus(int expediteurId, int destinataireId) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM messages WHERE expediteur_id = ? AND destinataire_id = ? AND lu = false";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, expediteurId);
            ps.setInt(2, destinataireId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Utilisateur> getPatientsAyantCommuniqueAvec(int medecinId) {
        List<Utilisateur> patients = new ArrayList<>();
        String sql = """
            SELECT DISTINCT u.id, u.nom, u.email
            FROM utilisateurs u
            JOIN messages m ON (u.id = m.expediteur_id OR u.id = m.destinataire_id)
            WHERE (m.expediteur_id = ? OR m.destinataire_id = ?) AND u.role = 'patient' AND u.id != ?
        """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, medecinId);
            ps.setInt(2, medecinId);
            ps.setInt(3, medecinId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Utilisateur u = new Utilisateur();
                u.setId(rs.getInt("id"));
                u.setNom(rs.getString("nom"));
                u.setEmail(rs.getString("email"));
                patients.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patients;
    }

    private Message mapResultSetToMessage(ResultSet rs) throws SQLException {
        Message m = new Message();
        m.setId(rs.getInt("id"));
        m.setExpediteurId(rs.getInt("expediteur_id"));
        m.setDestinataireId(rs.getInt("destinataire_id"));
        m.setContenu(rs.getString("contenu"));
        m.setDateEnvoi(rs.getTimestamp("date_envoi").toLocalDateTime());
        m.setLu(rs.getBoolean("lu"));
        return m;
    }
}
