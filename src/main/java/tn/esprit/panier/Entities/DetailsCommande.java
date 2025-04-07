package tn.esprit.panier.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DetailsCommande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idDetailsCommande;
    int quantite;
    int prixTotalArticle;
    @ManyToOne(cascade = CascadeType.ALL)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    @JsonBackReference
    private Commande commande;


    @PrePersist
    @PreUpdate
    public void calculerPrixTotal() {
        if (article != null) {
            this.prixTotalArticle = this.quantite * article.getPrix();
        }
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public int getIdDetailsCommande() {
        return idDetailsCommande;
    }

    public void setIdDetailsCommande(int idDetailsCommande) {
        this.idDetailsCommande = idDetailsCommande;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public int getPrixTotalArticle() {
        return prixTotalArticle;
    }

    public void setPrixTotalArticle(int prixTotalArticle) {
        this.prixTotalArticle = prixTotalArticle;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }


}
