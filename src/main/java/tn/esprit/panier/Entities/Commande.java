package tn.esprit.panier.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.Set;

@Setter
@Entity
@Getter
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idCommande;
    int prixTotalCommande;
    @Enumerated(EnumType.STRING)
    Status status;
    Date dateCommande;
    @ManyToOne
    @JoinColumn(name = "client_id")
    Client client;
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("commande")
    @JsonManagedReference
    private Set<DetailsCommande> detailsCommande ;

    public Set<DetailsCommande> getDetailsCommande() {
        return detailsCommande;
    }

    public void setDetailsCommande(Set<DetailsCommande> detailsCommande) {
        this.detailsCommande = detailsCommande;
    }

    public int getIdCommande() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande = idCommande;
    }


    public int getPrixTotalCommande() {
        return prixTotalCommande;
    }

    public void setPrixTotalCommande(int prixTotalCommande) {
        this.prixTotalCommande = prixTotalCommande;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }


}

