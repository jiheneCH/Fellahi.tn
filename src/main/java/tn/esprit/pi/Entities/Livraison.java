package tn.esprit.pi.Entities;

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
    private Client client;
    public void setClientId(Long clientId) {
        this.client = new Client();
        this.client.setId(clientId);
    }
    @ManyToOne
    @JoinColumn(name = "transporteur_id", nullable = false)
    private Transporteur transporteur;
    // Getter et setter pour transporteurId
    public Long getTransporteurId() {
        return transporteur != null ? transporteur.getId() : null;
    }

    public void setTransporteurId(Long transporteurId) {
        // Si tu veux juste garder l'ID du transporteur, tu affectes ici l'ID du transporteur à la relation
        this.transporteur = new Transporteur();
        this.transporteur.setId(transporteurId);
    }
    private LocalDate dateLivraison;

    @Enumerated(EnumType.STRING)
    private StatutLivraison statut;
    @Column(name = "archived")
    private boolean archived;

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

    public Transporteur getTransporteur() {
        return transporteur;
    }

    public void setTransporteur(Transporteur transporteur) {
        this.transporteur = transporteur;
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
