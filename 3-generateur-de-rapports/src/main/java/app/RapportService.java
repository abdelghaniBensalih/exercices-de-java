package app;

import data.Categorie;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class RapportService {
  private List<Ticket> tickets;

  public RapportService() {
    this.tickets = new ArrayList<>();
  }

  public RapportService(List<Ticket> tickets) {
    this.tickets = tickets != null ? new ArrayList<>(tickets) : new ArrayList<>();
  }

  public void ajouterTicket(Ticket ticket) {
    if (ticket != null) {
      tickets.add(ticket);
    }
  }

  public void setTickets(List<Ticket> tickets) {
    this.tickets = tickets != null ? new ArrayList<>(tickets) : new ArrayList<>();
  }

  public RapportQuotidienServeur genererRapportQuotidienParServeur(LocalDate date) {
    List<Ticket> ticketsJour =
        tickets.stream()
            .filter(t -> t.getDateHeure().toLocalDate().equals(date))
            .filter(t -> "FERME".equals(t.getStatut()))
            .collect(Collectors.toList());
    Map<Serveur, List<Ticket>> ticketsParServeur =
        ticketsJour.stream().collect(Collectors.groupingBy(Ticket::getServeur));
    List<DetailServeur> detailsServeurs = new ArrayList<>();
    for (Map.Entry<Serveur, List<Ticket>> entry : ticketsParServeur.entrySet()) {
      Serveur serveur = entry.getKey();
      List<Ticket> ticketsServeur = entry.getValue();
      int nombreTickets = ticketsServeur.size();
      BigDecimal chiffreAffaires =
          ticketsServeur.stream()
              .map(Ticket::getMontantTTC)
              .reduce(BigDecimal.ZERO, BigDecimal::add);
      BigDecimal ticketMoyen =
          nombreTickets > 0
              ? chiffreAffaires.divide(
                  BigDecimal.valueOf(nombreTickets), 2, BigDecimal.ROUND_HALF_UP)
              : BigDecimal.ZERO;
      detailsServeurs.add(new DetailServeur(serveur, nombreTickets, chiffreAffaires, ticketMoyen));
    }
    return new RapportQuotidienServeur(date, detailsServeurs);
  }

  public List<PerformanceServeur> getServeursLePlusPerformants(
      LocalDate dateDebut, LocalDate dateFin, int limite) {
    List<Ticket> ticketsPeriode =
        tickets.stream()
            .filter(
                t -> {
                  LocalDate dateTicket = t.getDateHeure().toLocalDate();
                  return !dateTicket.isBefore(dateDebut) && !dateTicket.isAfter(dateFin);
                })
            .filter(t -> "FERME".equals(t.getStatut()))
            .collect(Collectors.toList());
    Map<Serveur, List<Ticket>> ticketsParServeur =
        ticketsPeriode.stream().collect(Collectors.groupingBy(Ticket::getServeur));
    return ticketsParServeur.entrySet().stream()
        .map(
            entry -> {
              Serveur serveur = entry.getKey();
              List<Ticket> ticketsServeur = entry.getValue();
              int nombreTickets = ticketsServeur.size();
              BigDecimal chiffreAffaires =
                  ticketsServeur.stream()
                      .map(Ticket::getMontantTTC)
                      .reduce(BigDecimal.ZERO, BigDecimal::add);
              BigDecimal ticketMoyen =
                  nombreTickets > 0
                      ? chiffreAffaires.divide(
                          BigDecimal.valueOf(nombreTickets), 2, BigDecimal.ROUND_HALF_UP)
                      : BigDecimal.ZERO;
              return new PerformanceServeur(serveur, nombreTickets, chiffreAffaires, ticketMoyen);
            })
        .sorted((p1, p2) -> p2.getChiffreAffaires().compareTo(p1.getChiffreAffaires()))
        .limit(limite)
        .collect(Collectors.toList());
  }

  public Map<DayOfWeek, List<VenteCategorie>> getCategoriesLePlusVenduesParJourSemaine(
      LocalDate dateDebut, LocalDate dateFin) {
    List<Ticket> ticketsPeriode =
        tickets.stream()
            .filter(
                t -> {
                  LocalDate dateTicket = t.getDateHeure().toLocalDate();
                  return !dateTicket.isBefore(dateDebut) && !dateTicket.isAfter(dateFin);
                })
            .filter(t -> "FERME".equals(t.getStatut()))
            .collect(Collectors.toList());
    Map<DayOfWeek, List<VenteCategorie>> resultats = new HashMap<>();
    for (DayOfWeek jourSemaine : DayOfWeek.values()) {
      Map<Categorie, VenteCategorie> ventesParCategorie = new HashMap<>();
      ticketsPeriode.stream()
          .filter(t -> t.getDateHeure().getDayOfWeek() == jourSemaine)
          .forEach(
              ticket -> {
                ticket
                    .getLignes()
                    .forEach(
                        ligne -> {
                          Categorie categorie = ligne.getProduit().getCategorie();
                          ventesParCategorie.computeIfAbsent(
                              categorie, c -> new VenteCategorie(c, 0, BigDecimal.ZERO));
                          VenteCategorie venteCategorie = ventesParCategorie.get(categorie);
                          venteCategorie.ajouterVente(ligne.getQuantite(), ligne.getMontantLigne());
                        });
              });
      List<VenteCategorie> ventesTriees =
          ventesParCategorie.values().stream()
              .sorted((v1, v2) -> Integer.compare(v2.getQuantiteVendue(), v1.getQuantiteVendue()))
              .collect(Collectors.toList());
      resultats.put(jourSemaine, ventesTriees);
    }
    return resultats;
  }

  public static class RapportQuotidienServeur {
    private LocalDate date;
    private List<DetailServeur> detailsServeurs;
    private BigDecimal chiffreAffairesTotal;
    private int nombreTicketsTotal;

    public RapportQuotidienServeur(LocalDate date, List<DetailServeur> detailsServeurs) {
      this.date = date;
      this.detailsServeurs = detailsServeurs;
      this.chiffreAffairesTotal =
          detailsServeurs.stream()
              .map(DetailServeur::getChiffreAffaires)
              .reduce(BigDecimal.ZERO, BigDecimal::add);
      this.nombreTicketsTotal =
          detailsServeurs.stream().mapToInt(DetailServeur::getNombreTickets).sum();
    }

    public LocalDate getDate() {
      return date;
    }

    public List<DetailServeur> getDetailsServeurs() {
      return detailsServeurs;
    }

    public BigDecimal getChiffreAffairesTotal() {
      return chiffreAffairesTotal;
    }

    public int getNombreTicketsTotal() {
      return nombreTicketsTotal;
    }

    public String toString() {
      StringBuilder sb = new StringBuilder();
      sb.append("Rapport du ").append(date).append("\n");
      sb.append("Chiffre total: ").append(chiffreAffairesTotal).append("\n");
      sb.append("Tickets total: ").append(nombreTicketsTotal).append("\n");
      sb.append("Serveurs:\n");
      for (DetailServeur detail : detailsServeurs) {
        sb.append("- ")
            .append(detail.getServeur().getNomComplet())
            .append(": ")
            .append(detail.getNombreTickets())
            .append(" tickets, ")
            .append(detail.getChiffreAffaires())
            .append(", ticket moyen: ")
            .append(detail.getTicketMoyen())
            .append("\n");
      }
      return sb.toString();
    }
  }

  public static class DetailServeur {
    private Serveur serveur;
    private int nombreTickets;
    private BigDecimal chiffreAffaires;
    private BigDecimal ticketMoyen;

    public DetailServeur(
        Serveur serveur, int nombreTickets, BigDecimal chiffreAffaires, BigDecimal ticketMoyen) {
      this.serveur = serveur;
      this.nombreTickets = nombreTickets;
      this.chiffreAffaires = chiffreAffaires;
      this.ticketMoyen = ticketMoyen;
    }

    public Serveur getServeur() {
      return serveur;
    }

    public int getNombreTickets() {
      return nombreTickets;
    }

    public BigDecimal getChiffreAffaires() {
      return chiffreAffaires;
    }

    public BigDecimal getTicketMoyen() {
      return ticketMoyen;
    }
  }

  public static class PerformanceServeur {
    private Serveur serveur;
    private int nombreTickets;
    private BigDecimal chiffreAffaires;
    private BigDecimal ticketMoyen;

    public PerformanceServeur(
        Serveur serveur, int nombreTickets, BigDecimal chiffreAffaires, BigDecimal ticketMoyen) {
      this.serveur = serveur;
      this.nombreTickets = nombreTickets;
      this.chiffreAffaires = chiffreAffaires;
      this.ticketMoyen = ticketMoyen;
    }

    public Serveur getServeur() {
      return serveur;
    }

    public int getNombreTickets() {
      return nombreTickets;
    }

    public BigDecimal getChiffreAffaires() {
      return chiffreAffaires;
    }

    public BigDecimal getTicketMoyen() {
      return ticketMoyen;
    }

    public String toString() {
      return serveur.getNomComplet()
          + " - CA: "
          + chiffreAffaires
          + " ("
          + nombreTickets
          + " tickets, moy: "
          + ticketMoyen
          + ")";
    }
  }

  public static class VenteCategorie {
    private Categorie categorie;
    private int quantiteVendue;
    private BigDecimal montantVentes;

    public VenteCategorie(Categorie categorie, int quantiteVendue, BigDecimal montantVentes) {
      this.categorie = categorie;
      this.quantiteVendue = quantiteVendue;
      this.montantVentes = montantVentes;
    }

    public void ajouterVente(int quantite, BigDecimal montant) {
      this.quantiteVendue += quantite;
      this.montantVentes = this.montantVentes.add(montant);
    }

    public Categorie getCategorie() {
      return categorie;
    }

    public int getQuantiteVendue() {
      return quantiteVendue;
    }

    public BigDecimal getMontantVentes() {
      return montantVentes;
    }

    public String toString() {
      return categorie.getNom() + " - Qte: " + quantiteVendue + ", Montant: " + montantVentes;
    }
  }
}
