// Fichier modifié : RendezVous.java
package epf.csi.examen.teleconsultation.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class RendezVous {

    private final IntegerProperty id;
    private final IntegerProperty patientId;
    private final IntegerProperty medecinId;
    private final ObjectProperty<LocalDateTime> dateHeure;
    private final StringProperty statut;
    private final StringProperty patientNom; // Ajout pour nom du patient

    // Constructeur complet
    public RendezVous(int id, int patientId, int medecinId, LocalDateTime dateHeure, String statut, String patientNom) {
        this.id = new SimpleIntegerProperty(id);
        this.patientId = new SimpleIntegerProperty(patientId);
        this.medecinId = new SimpleIntegerProperty(medecinId);
        this.dateHeure = new SimpleObjectProperty<>(dateHeure);
        this.statut = new SimpleStringProperty(statut);
        this.patientNom = new SimpleStringProperty(patientNom);
    }

    // Constructeur sans nom patient (fallback)
    public RendezVous(int id, int patientId, int medecinId, LocalDateTime dateHeure, String statut) {
        this(id, patientId, medecinId, dateHeure, statut, "");
    }

    public RendezVous(int patientId, int medecinId, LocalDateTime dateHeure, String statut) {
        this(0, patientId, medecinId, dateHeure, statut, "");
    }

    public int getId() { return id.get(); }
    public IntegerProperty idProperty() { return id; }
    public void setId(int id) { this.id.set(id); }

    public int getPatientId() { return patientId.get(); }
    public IntegerProperty patientIdProperty() { return patientId; }
    public void setPatientId(int patientId) { this.patientId.set(patientId); }

    public int getMedecinId() { return medecinId.get(); }
    public IntegerProperty medecinIdProperty() { return medecinId; }
    public void setMedecinId(int medecinId) { this.medecinId.set(medecinId); }

    public LocalDateTime getDateHeure() { return dateHeure.get(); }
    public ObjectProperty<LocalDateTime> dateHeureProperty() { return dateHeure; }
    public void setDateHeure(LocalDateTime dateHeure) { this.dateHeure.set(dateHeure); }

    public String getStatut() { return statut.get(); }
    public StringProperty statutProperty() { return statut; }
    public void setStatut(String statut) { this.statut.set(statut); }

    public String getPatientNom() { return patientNom.get(); }
    public StringProperty patientNomProperty() { return patientNom; }
    public void setPatientNom(String patientNom) { this.patientNom.set(patientNom); }
}