package tn.esprit.fallehiuser.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    double prixTotalArticle;
    int pointfidelityTotalArticle;
    @ManyToOne
    @JoinColumn(name = "article_id", referencedColumnName = "idArticle")
    private Article article;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "commande_id")
    @JsonBackReference
    private Commande commande;


    @PrePersist
    @PreUpdate
    public void calculerPrixTotal() {
        if (article != null) {
            this.prixTotalArticle = (double) this.quantite * article.getPrix();  // Convertir quantite en double
        }
    }


    public void calculerPointfidelityTotal() {
        if (article != null) {
            this.pointfidelityTotalArticle = this.quantite * article.getPointsFidelite();
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

    public double getPrixTotalArticle() {
        return prixTotalArticle;
    }

    public void setPrixTotalArticle(double prixTotalArticle) {
        this.prixTotalArticle = prixTotalArticle;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }
    public int getPointfidelityTotalArticle() {
        return pointfidelityTotalArticle;
    }

    public void setPointfidelityTotalArticle(int pointfidelityTotalArticle) {
        this.pointfidelityTotalArticle = pointfidelityTotalArticle;
    }


}
