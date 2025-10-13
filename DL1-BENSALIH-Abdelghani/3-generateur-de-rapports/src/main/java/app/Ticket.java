package app;

import data.Produit;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Ticket {
  private int id;
  private LocalDateTime dateHeure;
  private Serveur serveur;
  private List<LigneTicket> lignes;
  private BigDecimal montantTotal;
  private BigDecimal montantTTC;
  private BigDecimal tva;
  private Promo promo;
  private String statut;

  public Ticket() {
    this.lignes = new ArrayList<>();
    this.statut = "OUVERT";
    this.tva = new BigDecimal("0.20");
  }

  public Ticket(int id, LocalDateTime dateHeure, Serveur serveur) {
    this();
    this.id = id;
    this.dateHeure = dateHeure;
    this.serveur = serveur;
  }

  public static class LigneTicket {
    private Produit produit;
    private int quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal montantLigne;

    public LigneTicket(Produit produit, int quantite, BigDecimal prixUnitaire) {
      this.produit = produit;
      this.quantite = quantite;
      this.prixUnitaire = prixUnitaire;
      this.montantLigne = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
    }

    public Produit getProduit() {
      return produit;
    }

    public void setProduit(Produit produit) {
      this.produit = produit;
    }

    public int getQuantite() {
      return quantite;
    }

    public void setQuantite(int quantite) {
      this.quantite = quantite;
      this.montantLigne = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
    }

    public BigDecimal getPrixUnitaire() {
      return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
      this.prixUnitaire = prixUnitaire;
      this.montantLigne = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
    }

    public BigDecimal getMontantLigne() {
      return montantLigne;
    }
  }

  public void ajouterLigne(Produit produit, int quantite) {
    LigneTicket ligne = new LigneTicket(produit, quantite, produit.getPrix());
    lignes.add(ligne);
    calculerMontants();
  }

  public void supprimerLigne(int index) {
    if (index >= 0 && index < lignes.size()) {
      lignes.remove(index);
      calculerMontants();
    }
  }

  public void appliquerPromo(Promo promo) {
    if (promo != null && promo.isValide(dateHeure.toLocalDate())) {
      this.promo = promo;
      calculerMontants();
    }
  }

  private void calculerMontants() {
    montantTotal =
        lignes.stream().map(LigneTicket::getMontantLigne).reduce(BigDecimal.ZERO, BigDecimal::add);
    if (promo != null && promo.isValide(dateHeure.toLocalDate())) {
      BigDecimal reduction =
          montantTotal.multiply(promo.getPourcentageReduction()).divide(BigDecimal.valueOf(100));
      montantTotal = montantTotal.subtract(reduction);
    }
    BigDecimal montantTVA = montantTotal.multiply(tva);
    montantTTC = montantTotal.add(montantTVA);
  }

  public void fermerTicket() {
    this.statut = "FERME";
    calculerMontants();
  }

  public void annulerTicket() {
    this.statut = "ANNULE";
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public LocalDateTime getDateHeure() {
    return dateHeure;
  }

  public void setDateHeure(LocalDateTime dateHeure) {
    this.dateHeure = dateHeure;
  }

  public Serveur getServeur() {
    return serveur;
  }

  public void setServeur(Serveur serveur) {
    this.serveur = serveur;
  }

  public List<LigneTicket> getLignes() {
    return new ArrayList<>(lignes);
  }

  public BigDecimal getMontantTotal() {
    return montantTotal;
  }

  public BigDecimal getMontantTTC() {
    return montantTTC;
  }

  public BigDecimal getTva() {
    return tva;
  }

  public void setTva(BigDecimal tva) {
    this.tva = tva;
    calculerMontants();
  }

  public Promo getPromo() {
    return promo;
  }

  public String getStatut() {
    return statut;
  }

  public void setStatut(String statut) {
    this.statut = statut;
  }

  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Ticket ticket = (Ticket) o;
    return id == ticket.id;
  }

  public int hashCode() {
    return id;
  }

  public String toString() {
    return "Ticket{"
        + "id="
        + id
        + ", dateHeure="
        + dateHeure
        + ", serveur="
        + serveur
        + ", montantTotal="
        + montantTotal
        + ", montantTTC="
        + montantTTC
        + ", statut='"
        + statut
        + '\''
        + '}';
  }
}
