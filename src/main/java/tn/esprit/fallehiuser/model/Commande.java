package tn.esprit.fallehiuser.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
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
    int pointfidelityTotalCommande;
    String referenceCommande;
    @Enumerated(EnumType.STRING)
    StatusCommande StatusCommande;
    @Enumerated(EnumType.STRING)
    StatusPayment payment;
    Date dateCommande;
    @ManyToOne
    //@JoinColumn(name = "user_id")
    User user;
    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("commande")
    @JsonManagedReference
    private Set<DetailsCommande> detailsCommande = new HashSet<>();

    public Commande() {

    }
    @OneToOne
    @JoinColumn(name = "livraison_id")
    @JsonIgnore
    private Livraison livraison;

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

    public Date getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(Date dateCommande) {
        this.dateCommande = dateCommande;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public int getPointfidelityTotalCommande() {
        return pointfidelityTotalCommande;
    }

    public void setPointfidelityTotalCommande(int pointfidelityTotalCommande) {
        this.pointfidelityTotalCommande = pointfidelityTotalCommande;
    }

    public String getReferenceCommande() {
        return referenceCommande;
    }

    public void setReferenceCommande(String referenceCommande) {
        this.referenceCommande = referenceCommande;
    }

    public StatusPayment getPayment() {
        return payment;
    }

    public void setPayment(StatusPayment payment) {
        this.payment = payment;
    }

    public StatusCommande getStatusCommande() {
        return StatusCommande;
    }

    public void setStatusCommande(StatusCommande statusCommande) {
        StatusCommande = statusCommande;
    }
}