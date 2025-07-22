package epf.csi.examen.teleconsultation.model;

import javafx.beans.property.*;

import java.time.LocalDate;

public class Prescription {
    private IntegerProperty id = new SimpleIntegerProperty();
    private ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private StringProperty medicaments = new SimpleStringProperty();
    private StringProperty remarques = new SimpleStringProperty();
    private IntegerProperty patientId = new SimpleIntegerProperty();
    private IntegerProperty medecinId = new SimpleIntegerProperty();

    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public LocalDate getDate() { return date.get(); }
    public void setDate(LocalDate date) { this.date.set(date); }
    public ObjectProperty<LocalDate> dateProperty() { return date; }

    public String getMedicaments() { return medicaments.get(); }
    public void setMedicaments(String medicaments) { this.medicaments.set(medicaments); }
    public StringProperty medicamentsProperty() { return medicaments; }

    public String getRemarques() { return remarques.get(); }
    public void setRemarques(String remarques) { this.remarques.set(remarques); }
    public StringProperty remarquesProperty() { return remarques; }

    public int getPatientId() { return patientId.get(); }
    public void setPatientId(int patientId) { this.patientId.set(patientId); }
    public IntegerProperty patientIdProperty() { return patientId; }

    public int getMedecinId() { return medecinId.get(); }
    public void setMedecinId(int medecinId) { this.medecinId.set(medecinId); }
    public IntegerProperty medecinIdProperty() { return medecinId; }
}
