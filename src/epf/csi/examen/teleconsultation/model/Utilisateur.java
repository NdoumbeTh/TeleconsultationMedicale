package epf.csi.examen.teleconsultation.model;

import javafx.beans.property.*;

public class Utilisateur {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nom = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();
    private final StringProperty motDePasse = new SimpleStringProperty();
    private final StringProperty role = new SimpleStringProperty();

    // Constructeur par défaut (nécessaire pour le DAO)
    public Utilisateur() {
        // Constructeur vide pour permettre la création d'objets vides
        // puis les remplir avec les setters (utilisé dans le DAO)
    }

    // Constructeur complet avec ID
    public Utilisateur(int id, String nom, String email, String motDePasse, String role) {
        this.id.set(id);
        this.nom.set(nom);
        this.email.set(email);
        this.motDePasse.set(motDePasse);
        this.role.set(role);
    }

    // Constructeur sans ID (utile pour les créations/inscriptions)
    public Utilisateur(String nom, String email, String motDePasse, String role) {
        this.nom.set(nom);
        this.email.set(email);
        this.motDePasse.set(motDePasse);
        this.role.set(role);
    }

    // --- Getters
    public int getId() { 
        return id.get(); 
    }
    
    public String getNom() { 
        return nom.get(); 
    }
    
    public String getEmail() { 
        return email.get(); 
    }
    
    public String getMotDePasse() { 
        return motDePasse.get(); 
    }
    
    public String getRole() { 
        return role.get(); 
    }

    // --- Setters
    public void setId(int id) { 
        this.id.set(id); 
    }
    
    public void setNom(String nom) { 
        this.nom.set(nom != null ? nom : ""); 
    }
    
    public void setEmail(String email) { 
        this.email.set(email != null ? email : ""); 
    }
    
    public void setMotDePasse(String motDePasse) { 
        this.motDePasse.set(motDePasse != null ? motDePasse : ""); 
    }
    
    public void setRole(String role) { 
        this.role.set(role != null ? role : ""); 
    }

    // --- Propriétés JavaFX (pour les bindings dans les interfaces)
    public IntegerProperty idProperty() { 
        return id; 
    }
    
    public StringProperty nomProperty() { 
        return nom; 
    }
    
    public StringProperty emailProperty() { 
        return email; 
    }
    
    public StringProperty motDePasseProperty() { 
        return motDePasse; 
    }
    
    public StringProperty roleProperty() { 
        return role; 
    }

    // --- Méthodes utilitaires
    
    /**
     * Vérifie si l'utilisateur a un rôle spécifique
     * @param roleToCheck Le rôle à vérifier
     * @return true si l'utilisateur a ce rôle
     */
    public boolean hasRole(String roleToCheck) {
        return roleToCheck != null && roleToCheck.equalsIgnoreCase(getRole());
    }
    
    /**
     * Vérifie si l'utilisateur est un admin
     * @return true si l'utilisateur est admin
     */
    public boolean isAdmin() {
        return hasRole("admin");
    }
    
    /**
     * Vérifie si l'utilisateur est un médecin
     * @return true si l'utilisateur est médecin
     */
    public boolean isMedecin() {
        return hasRole("medecin");
    }
    
    /**
     * Vérifie si l'utilisateur est un patient
     * @return true si l'utilisateur est patient
     */
    public boolean isPatient() {
        return hasRole("patient");
    }
    
    /**
     * Vérifie si tous les champs obligatoires sont remplis
     * @return true si l'objet est valide pour l'insertion en base
     */
    public boolean isValid() {
        return getNom() != null && !getNom().trim().isEmpty() &&
               getEmail() != null && !getEmail().trim().isEmpty() &&
               getMotDePasse() != null && !getMotDePasse().trim().isEmpty() &&
               getRole() != null && !getRole().trim().isEmpty();
    }
    
    /**
     * Représentation textuelle de l'utilisateur
     */
    @Override
    public String toString() {
        return String.format("Utilisateur{id=%d, nom='%s', email='%s', role='%s'}", 
                           getId(), getNom(), getEmail(), getRole());
    }
    
    /**
     * Comparaison d'égalité basée sur l'email (unique)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Utilisateur that = (Utilisateur) obj;
        return getEmail() != null ? getEmail().equals(that.getEmail()) : that.getEmail() == null;
    }
    
    @Override
    public int hashCode() {
        return getEmail() != null ? getEmail().hashCode() : 0;
    }
}