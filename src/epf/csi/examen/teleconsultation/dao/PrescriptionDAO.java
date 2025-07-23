package epf.csi.examen.teleconsultation.dao;

import epf.csi.examen.teleconsultation.model.Prescription;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO {
    private final Connection connection;

    public PrescriptionDAO(Connection connection) {
        this.connection = connection;
    }

    public void save(Prescription prescription) throws SQLException {
        String sql = "INSERT INTO prescriptions (date, medicaments, remarques, patient_id, medecin_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(prescription.getDate()));
            stmt.setString(2, prescription.getMedicaments());
            stmt.setString(3, prescription.getRemarques());
            stmt.setInt(4, prescription.getPatientId());
            stmt.setInt(5, prescription.getMedecinId());
            stmt.executeUpdate();
        }
    }

    public List<Prescription> getPrescriptionsByMedecin(int medecinId) throws SQLException {
        List<Prescription> prescriptions = new ArrayList<>();
        String sql = "SELECT * FROM prescriptions WHERE medecin_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, medecinId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Prescription p = new Prescription();
                p.setId(rs.getInt("id"));
                p.setDate(rs.getDate("date").toLocalDate());
                p.setMedicaments(rs.getString("medicaments"));
                p.setRemarques(rs.getString("remarques"));
                p.setPatientId(rs.getInt("patient_id"));
                p.setMedecinId(rs.getInt("medecin_id"));
                prescriptions.add(p);
            }
        }
        return prescriptions;
    }
    public List<Prescription> getPrescriptionsByPatient(int patientId) throws SQLException {
        List<Prescription> prescriptions = new ArrayList<>();
        String sql = "SELECT * FROM prescriptions WHERE patient_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Prescription p = new Prescription();
                p.setId(rs.getInt("id"));
                p.setDate(rs.getDate("date").toLocalDate());
                p.setMedicaments(rs.getString("medicaments"));
                p.setRemarques(rs.getString("remarques"));
                p.setPatientId(rs.getInt("patient_id"));
                p.setMedecinId(rs.getInt("medecin_id"));
                prescriptions.add(p);
            }
        }
        return prescriptions;
    }

}
