package epf.csi.examen.teleconsultation.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class RendezVous {
    private int id;
    private int patientId;
    private int medecinId;
    private LocalDate date;
    private LocalTime heure;
    private String statut;

    public RendezVous(int id, int patientId, int medecinId, LocalDate date, LocalTime heure, String statut) {
        this.id = id;
        this.patientId = patientId;
        this.medecinId = medecinId;
        this.date = date;
        this.heure = heure;
        this.statut = statut;
    }

    // Getters et Setters
    public int getId() { return id; }
    public int getPatientId() { return patientId; }
    public int getMedecinId() { return medecinId; }
    public LocalDate getDate() { return date; }
    public LocalTime getHeure() { return heure; }
    public String getStatut() { return statut; }

    public void setId(int id) { this.id = id; }
    public void setPatientId(int patientId) { this.patientId = patientId; }
    public void setMedecinId(int medecinId) { this.medecinId = medecinId; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setHeure(LocalTime heure) { this.heure = heure; }
    public void setStatut(String statut) { this.statut = statut; }
}
