package epf.csi.examen.teleconsultation.model;

public class CarnetSante {
    private int patientId;
    private String contenu;

    public CarnetSante(int patientId, String contenu) {
        this.patientId = patientId;
        this.contenu = contenu;
    }

    // Getters et Setters
    public int getPatientId() { return patientId; }
    public String getContenu() { return contenu; }

    public void setPatientId(int patientId) { this.patientId = patientId; }
    public void setContenu(String contenu) { this.contenu = contenu; }
}
