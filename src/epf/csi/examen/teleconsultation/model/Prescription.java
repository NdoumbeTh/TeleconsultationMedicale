package epf.csi.examen.teleconsultation.model;

import java.time.LocalDate;

public class Prescription {
    private int id;
    private int patientId;
    private int medecinId;
    private String contenu;
    private LocalDate date;

    public Prescription(int id, int patientId, int medecinId, String contenu, LocalDate date) {
        this.id = id;
        this.patientId = patientId;
        this.medecinId = medecinId;
        this.contenu = contenu;
        this.date = date;
    }

    // Getters et Setters
    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getMedecinId() { return medecinId; }
    public String getContenu() { return contenu; }
    public LocalDate getDate() { return date; }

    public void setId(int id) { this.id = id; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public void setMedecinId(int medecinId) { this.medecinId = medecinId; }
    public void setContenu(String contenu) { this.contenu = contenu; }
    public void setDate(LocalDate date) { this.date = date; }
}
