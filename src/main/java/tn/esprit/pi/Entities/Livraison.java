package tn.esprit.pi.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Livraison {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "id_commande", referencedColumnName = "id")
    private Commande commande;
    public void setCommandeId(Long commandeId) {
        // Si tu veux juste garder l'ID du transporteur, tu affectes ici l'ID du transporteur à la relation
        this.commande = new Commande();
        this.commande.setId(commandeId);
    }
    @ManyToOne
    @JoinColumn(name = "client_id",  referencedColumnName = "id")
    private User client;

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    // Nouvelle association vers User (transporteur)
    @ManyToOne
    @JoinColumn(name = "transporteur_id", referencedColumnName = "id")

    private User transporteur;

    public User getTransporteur() {
        return transporteur;
    }

    public void setTransporteur(User transporteur) {
        this.transporteur = transporteur;
    }





    private LocalDate dateLivraison;

    @Enumerated(EnumType.STRING)
    private StatutLivraison statut;
    @Column(name = "archived")
    private boolean archived;
    private String typeIncident;         // Ex: "Client injoignable", "Route bloquée"
    private String descriptionIncident;  // Description du problème
    private LocalDateTime dateIncident;

    public String getTypeIncident() {
        return typeIncident;
    }

    public void setTypeIncident(String typeIncident) {
        this.typeIncident = typeIncident;
    }

    public String getDescriptionIncident() {
        return descriptionIncident;
    }

    public void setDescriptionIncident(String descriptionIncident) {
        this.descriptionIncident = descriptionIncident;
    }

    public LocalDateTime getDateIncident() {
        return dateIncident;
    }

    public void setDateIncident(LocalDateTime dateIncident) {
        this.dateIncident = dateIncident;
    }

    public StatutIncident getStatutIncident() {
        return statutIncident;
    }

    public void setStatutIncident(StatutIncident statutIncident) {
        this.statutIncident = statutIncident;
    }

    @Enumerated(EnumType.STRING)
    private StatutIncident statutIncident; // NOUVEAU, EN_COURS, RESOLU
    // Getters and setters
    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
    private double prixTotal; // Prix total de la commande + 7 DT (frais de livraison)

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }


    public LocalDate  getDateLivraison() {
        return dateLivraison;
    }

    public void setDateLivraison(LocalDate  dateLivraison) {
        this.dateLivraison = dateLivraison;
    }

    public StatutLivraison getStatut() {
        return statut;
    }

    public void setStatut(StatutLivraison statut) {
        this.statut = statut;
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }



}
