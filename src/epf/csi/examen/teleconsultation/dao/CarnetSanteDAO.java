package epf.csi.examen.teleconsultation.dao;

import epf.csi.examen.teleconsultation.model.CarnetSante;
import epf.csi.examen.teleconsultation.utils.DBConnection;

import java.sql.*;

public class CarnetSanteDAO {

    private final Connection conn;

    public CarnetSanteDAO(Connection conn) {
        this.conn = conn;
    }

    public CarnetSante getCarnetByPatientId(int patientId) {
        String sql = "SELECT * FROM carnets_sante WHERE patient_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, patientId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CarnetSante carnet = new CarnetSante();
                carnet.setId(rs.getInt("id"));
                carnet.setGroupeSanguin(rs.getString("groupe_sanguin"));
                carnet.setAllergies(rs.getString("allergies"));
                carnet.setAntecedents(rs.getString("antecedents"));
                carnet.setPatientId(rs.getInt("patient_id"));
                return carnet;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Pas trouvé
    }

    public boolean creerCarnet(CarnetSante carnet) throws SQLException {
        // Vérifie si le patient existe dans la table utilisateurs avec rôle patient
        String verifSQL = "SELECT COUNT(*) FROM utilisateurs WHERE id = ? AND role = 'patient'";
        try (PreparedStatement verifStmt = conn.prepareStatement(verifSQL)) {
            verifStmt.setInt(1, carnet.getPatientId());
            try (ResultSet rs = verifStmt.executeQuery()) {
                if (rs.next() && rs.getInt(1) == 0) {
                    throw new SQLException("Erreur : le patient avec id " + carnet.getPatientId() + " n'existe pas ou n'est pas un patient.");
                }
            }
        }

        // Insertion dans la table carnets_sante
        String sql = "INSERT INTO carnets_sante (groupe_sanguin, allergies, antecedents, patient_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, carnet.getGroupeSanguin());
            stmt.setString(2, carnet.getAllergies());
            stmt.setString(3, carnet.getAntecedents());
            stmt.setInt(4, carnet.getPatientId());
            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;
        }
    }


    public boolean modifierCarnet(CarnetSante carnet) {
        String sql = "UPDATE carnets_sante SET groupe_sanguin = ?, allergies = ?, antecedents = ? WHERE patient_id = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, carnet.getGroupeSanguin());
            ps.setString(2, carnet.getAllergies());
            ps.setString(3, carnet.getAntecedents());
            ps.setInt(4, carnet.getPatientId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public CarnetSante getCarnetParPatientId(int patientId) throws SQLException {
        String sql = "SELECT * FROM carnets_sante WHERE patient_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    CarnetSante carnet = new CarnetSante();
                    carnet.setId(rs.getInt("id"));
                    carnet.setGroupeSanguin(rs.getString("groupe_sanguin"));
                    carnet.setAllergies(rs.getString("allergies"));
                    carnet.setAntecedents(rs.getString("antecedents"));
                    carnet.setPatientId(patientId);
                    return carnet;
                } else {
                    return null; // Aucun carnet pour ce patient
                }
            }
        }
    }

	public void updateCarnet(CarnetSante carnet) {
		// TODO Auto-generated method stub
		
	}

}
