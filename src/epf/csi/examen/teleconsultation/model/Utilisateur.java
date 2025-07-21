package epf.csi.examen.teleconsultation.model;

import javafx.beans.property.*;

public class Utilisateur {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty motDePasse = new SimpleStringProperty();
    private final StringProperty role = new SimpleStringProperty();

    // Constructeur complet
    public Utilisateur(int id, String nom, String email, String motDePasse, String role) {
        this.id.set(id);
        this.nom.set(nom);
        this.email.set(email);
        this.motDePasse.set(motDePasse);
        this.role.set(role);
    }

    // Constructeur sans ID (utile pour les créations)
    public Utilisateur(String nom, String email, String motDePasse, String role) {
        this.nom.set(nom);
        this.email.set(email);
        this.motDePasse.set(motDePasse);
        this.role.set(role);
    }

    // --- Getters
    public int getId() { return id.get(); }
    public String getNom() { return nom.get(); }
    public String getEmail() { return email.get(); }
    public String getMotDePasse() { return motDePasse.get(); }
    public String getRole() { return role.get(); }

    // --- Setters
    public void setId(int id) { this.id.set(id); }
    public void setNom(String nom) { this.nom.set(nom); }
    public void setEmail(String email) { this.email.set(email); }
    public void setMotDePasse(String motDePasse) { this.motDePasse.set(motDePasse); }
    public void setRole(String role) { this.role.set(role); }

    // --- Propriétés JavaFX
    public IntegerProperty idProperty() { return id; }
    public StringProperty nomProperty() { return nom; }
    public StringProperty emailProperty() { return email; }
    public StringProperty motDePasseProperty() { return motDePasse; }
    public StringProperty roleProperty() { return role; }
}
