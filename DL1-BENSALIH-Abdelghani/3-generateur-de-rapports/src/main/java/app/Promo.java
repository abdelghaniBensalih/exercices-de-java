package app;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Promo {
  private int id;
  private String nom;
  private String description;
  private BigDecimal pourcentageReduction;
  private LocalDate dateDebut;
  private LocalDate dateFin;
  private boolean active;

  public Promo() {}

  public Promo(
      int id,
      String nom,
      String description,
      BigDecimal pourcentageReduction,
      LocalDate dateDebut,
      LocalDate dateFin,
      boolean active) {
    this.id = id;
    this.nom = nom;
    this.description = description;
    this.pourcentageReduction = pourcentageReduction;
    this.dateDebut = dateDebut;
    this.dateFin = dateFin;
    this.active = active;
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

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public BigDecimal getPourcentageReduction() {
    return pourcentageReduction;
  }

  public void setPourcentageReduction(BigDecimal pourcentageReduction) {
    this.pourcentageReduction = pourcentageReduction;
  }

  public LocalDate getDateDebut() {
    return dateDebut;
  }

  public void setDateDebut(LocalDate dateDebut) {
    this.dateDebut = dateDebut;
  }

  public LocalDate getDateFin() {
    return dateFin;
  }

  public void setDateFin(LocalDate dateFin) {
    this.dateFin = dateFin;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public boolean isValide(LocalDate date) {
    return active
        && (dateDebut == null || !date.isBefore(dateDebut))
        && (dateFin == null || !date.isAfter(dateFin));
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Promo promo = (Promo) o;
    return id == promo.id;
  }

  public int hashCode() {
    return id;
  }

  public String toString() {
    return "Promo{"
        + "id="
        + id
        + ", nom='"
        + nom
        + '\''
        + ", description='"
        + description
        + '\''
        + ", pourcentageReduction="
        + pourcentageReduction
        + ", dateDebut="
        + dateDebut
        + ", dateFin="
        + dateFin
        + ", active="
        + active
        + '}';
  }
}
