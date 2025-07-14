package epf.csi.examen.teleconsultation.model;

public class Medecin {
    private int id;
    private String nom;
    private String specialite;

    public Medecin(int id, String nom, String specialite) {
        this.id = id;
        this.nom = nom;
        this.specialite = specialite;
    }

    // Getters et Setters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getSpecialite() { return specialite; }

    public void setId(int id) { this.id = id; }
    public void setNom(String nom) { this.nom = nom; }
    public void setSpecialite(String specialite) { this.specialite = specialite; }
}
