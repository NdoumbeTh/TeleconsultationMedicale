package epf.csi.examen.teleconsultation.model;

public class Patient {
    private int id;
    private String nom;
    private String prenom;
    private String email;
    private String numeroTel;

    public Patient(int id, String nom, String prenom, String email, String numeroTel) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.numeroTel = numeroTel;
    }

    // Getters et Setters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getEmail() { return email; }
    public String getNumeroTel() { return numeroTel; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setEmail(String email) { this.email = email; }
    public void setNumeroTel(String numeroTel) { this.numeroTel = numeroTel; }
}
