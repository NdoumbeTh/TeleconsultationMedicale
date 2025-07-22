package epf.csi.examen.teleconsultation.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class RendezVous {

    private final IntegerProperty id;
    private final IntegerProperty patientId;
    private final IntegerProperty medecinId;
    private final ObjectProperty<LocalDateTime> dateHeure;
    private final StringProperty statut;

    // Constructeur complet
    public RendezVous(int id, int patientId, int medecinId, LocalDateTime dateHeure, String statut) {
        this.id = new SimpleIntegerProperty(id);
        this.patientId = new SimpleIntegerProperty(patientId);
        this.medecinId = new SimpleIntegerProperty(medecinId);
        this.dateHeure = new SimpleObjectProperty<>(dateHeure);
        this.statut = new SimpleStringProperty(statut);
    }

    // Constructeur simplifié (sans id)
    public RendezVous(int patientId, int medecinId, LocalDateTime dateHeure, String statut) {
        this(0, patientId, medecinId, dateHeure, statut);
    }

    // Getters & setters JavaFX properties

    public int getId() {
        return id.get();
    }
    public IntegerProperty idProperty() {
        return id;
    }
    public void setId(int id) {
        this.id.set(id);
    }

    public int getPatientId() {
        return patientId.get();
    }
    public IntegerProperty patientIdProperty() {
        return patientId;
    }
    public void setPatientId(int patientId) {
        this.patientId.set(patientId);
    }

    public int getMedecinId() {
        return medecinId.get();
    }
    public IntegerProperty medecinIdProperty() {
        return medecinId;
    }
    public void setMedecinId(int medecinId) {
        this.medecinId.set(medecinId);
    }

    public LocalDateTime getDateHeure() {
        return dateHeure.get();
    }
    public ObjectProperty<LocalDateTime> dateHeureProperty() {
        return dateHeure;
    }
    public void setDateHeure(LocalDateTime dateHeure) {
        this.dateHeure.set(dateHeure);
    }

    public String getStatut() {
        return statut.get();
    }
    public StringProperty statutProperty() {
        return statut;
    }
    public void setStatut(String statut) {
        this.statut.set(statut);
    }
}
