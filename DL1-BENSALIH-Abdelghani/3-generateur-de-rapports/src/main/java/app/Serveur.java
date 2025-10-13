package app;

public class Serveur {
  private int id;
  private String nom;
  private String prenom;
  private String email;
  private String telephone;

  public Serveur() {}

  public Serveur(int id, String nom, String prenom, String email, String telephone) {
    this.id = id;
    this.nom = nom;
    this.prenom = prenom;
    this.email = email;
    this.telephone = telephone;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public String getPrenom() {
    return prenom;
  }

  public void setPrenom(String prenom) {
    this.prenom = prenom;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getTelephone() {
    return telephone;
  }

  public void setTelephone(String telephone) {
    this.telephone = telephone;
  }

  public String getNomComplet() {
    return prenom + " " + nom;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Serveur serveur = (Serveur) o;
    return id == serveur.id;
  }

  public int hashCode() {
    return id;
  }

  public String toString() {
    return "Serveur{"
        + "id="
        + id
        + ", nom='"
        + nom
        + '\''
        + ", prenom='"
        + prenom
        + '\''
        + ", email='"
        + email
        + '\''
        + ", telephone='"
        + telephone
        + '\''
        + '}';
  }
}
