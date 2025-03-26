package tn.esprit.pi.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Map;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonBackReference
    private Client client;

    @OneToOne
    @JoinColumn(name = "livraison_id")
    private Livraison livraison;
    @ElementCollection
    private Map<Long, Integer> produits;
    // Getter pour obtenir l'ID du client
    public Long getIdClient() {
        return this.client != null ? this.client.getId() : null;
    }

    public Livraison getLivraison() {
        return livraison;
    }

    public void setLivraison(Livraison livraison) {
        this.livraison = livraison;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    private double prixTotal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public Map<Long, Integer> getProduits() {
        return produits;
    }

    public void setProduits(Map<Long, Integer> produits) {
        this.produits = produits;
    }

    public double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(double prixTotal) {
        this.prixTotal = prixTotal;
    }
}
