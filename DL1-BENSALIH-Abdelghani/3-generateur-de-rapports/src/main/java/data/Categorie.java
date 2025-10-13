
package data;
public class Categorie {
    private int id;
    private String nom;
    private String description;
    public Categorie() {}
    public Categorie(int id, String nom, String description) {
        this.id = id;
        this.nom = nom;
        this.description = description;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categorie categorie = (Categorie) o;
        return id == categorie.id;
    }
    public int hashCode() { return id; }
    public String toString() {
        return "Categorie{" + "id=" + id + ", nom='" + nom + '\'' + ", description='" + description + '\'' + '}';
    }
}
