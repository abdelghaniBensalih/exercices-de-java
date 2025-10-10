
package data;
import java.math.BigDecimal;
public class Produit {
    private int id;
    private String nom;
    private String description;
    private BigDecimal prix;
    private Categorie categorie;
    private int stock;

    
    public Produit() {}
    public Produit(int id, String nom, String description, BigDecimal prix, Categorie categorie, int stock) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.prix = prix;
        this.categorie = categorie;
        this.stock = stock;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPrix() { return prix; }
    public void setPrix(BigDecimal prix) { this.prix = prix; }
    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produit produit = (Produit) o;
        return id == produit.id;
    }
    public int hashCode() { return id; }
    public String toString() {
        return "Produit{" + "id=" + id + ", nom='" + nom + '\'' + ", description='" + description + '\'' + ", prix=" + prix + ", categorie=" + categorie + ", stock=" + stock + '}';
    }
}
