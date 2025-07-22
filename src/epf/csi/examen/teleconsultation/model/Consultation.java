package epf.csi.examen.teleconsultation.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class Consultation {

    private final IntegerProperty id;
    private final IntegerProperty idPatient;
    private final IntegerProperty idMedecin;
    private final ObjectProperty<LocalDateTime> dateHeure;
    private final StringProperty type;
    private final StringProperty statut;
    private final StringProperty motif;
    private final StringProperty nomPatient;  // Pour afficher dans la table, info jointe
    private final StringProperty nomMedecin;  // Optionnel

    // Constructeur complet
    public Consultation(int id, int idPatient, int idMedecin, LocalDateTime dateHeure,
                        String type, String statut, String motif,
                        String nomPatient, String nomMedecin) {
        this.id = new SimpleIntegerProperty(id);
        this.idPatient = new SimpleIntegerProperty(idPatient);
        this.idMedecin = new SimpleIntegerProperty(idMedecin);
        this.dateHeure = new SimpleObjectProperty<>(dateHeure);
        this.type = new SimpleStringProperty(type);
        this.statut = new SimpleStringProperty(statut);
        this.motif = new SimpleStringProperty(motif);
        this.nomPatient = new SimpleStringProperty(nomPatient);
        this.nomMedecin = new SimpleStringProperty(nomMedecin);
    }

    // Constructeur simplifié (sans noms)
    public Consultation(int id, int idPatient, int idMedecin, LocalDateTime dateHeure,
                        String type, String statut, String motif) {
        this(id, idPatient, idMedecin, dateHeure, type, statut, motif, "", "");
    }

    // Getters et setters JavaFX Properties

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getIdPatient() {
        return idPatient.get();
    }

    public IntegerProperty idPatientProperty() {
        return idPatient;
    }

    public void setIdPatient(int idPatient) {
        this.idPatient.set(idPatient);
    }

    public int getIdMedecin() {
        return idMedecin.get();
    }

    public IntegerProperty idMedecinProperty() {
        return idMedecin;
    }

    public void setIdMedecin(int idMedecin) {
        this.idMedecin.set(idMedecin);
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

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
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

    public String getMotif() {
        return motif.get();
    }

    public StringProperty motifProperty() {
        return motif;
    }

    public void setMotif(String motif) {
        this.motif.set(motif);
    }

    public String getNomPatient() {
        return nomPatient.get();
    }

    public StringProperty nomPatientProperty() {
        return nomPatient;
    }

    public void setNomPatient(String nomPatient) {
        this.nomPatient.set(nomPatient);
    }

    public String getNomMedecin() {
        return nomMedecin.get();
    }

    public StringProperty nomMedecinProperty() {
        return nomMedecin;
    }

    public void setNomMedecin(String nomMedecin) {
        this.nomMedecin.set(nomMedecin);
    }
}
