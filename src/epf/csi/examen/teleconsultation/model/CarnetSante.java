package epf.csi.examen.teleconsultation.model;

import javafx.beans.property.*;

public class CarnetSante {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty groupeSanguin = new SimpleStringProperty();
    private final StringProperty allergies = new SimpleStringProperty();
    private final StringProperty antecedents = new SimpleStringProperty();
    private final IntegerProperty patientId = new SimpleIntegerProperty();

    // Getters/Setters
    public int getId() { return id.get(); }
    public void setId(int value) { id.set(value); }
    public IntegerProperty idProperty() { return id; }

    public String getGroupeSanguin() { return groupeSanguin.get(); }
    public void setGroupeSanguin(String value) { groupeSanguin.set(value); }
    public StringProperty groupeSanguinProperty() { return groupeSanguin; }

    public String getAllergies() { return allergies.get(); }
    public void setAllergies(String value) { allergies.set(value); }
    public StringProperty allergiesProperty() { return allergies; }

    public String getAntecedents() { return antecedents.get(); }
    public void setAntecedents(String value) { antecedents.set(value); }
    public StringProperty antecedentsProperty() { return antecedents; }

    public int getPatientId() { return patientId.get(); }
    public void setPatientId(int value) { patientId.set(value); }
    public IntegerProperty patientIdProperty() { return patientId; }
}
