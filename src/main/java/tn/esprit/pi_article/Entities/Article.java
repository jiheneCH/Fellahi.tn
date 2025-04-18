package tn.esprit.pi_article.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "article", uniqueConstraints = {@UniqueConstraint(columnNames = "nom")})


public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long idArticle;

    @NotBlank(message = "Le nom de l'article est obligatoire.")
    @Column(unique = true)
    String nom;


    String reference;
    private double reduction;
    private Integer pointsFidelite;
    private Integer quantiteInitiale;

    public Integer getQuantiteInitiale() {
        return quantiteInitiale;
    }

    public void setQuantiteInitiale(Integer quantiteInitiale) {
        this.quantiteInitiale = quantiteInitiale;
    }

    private boolean alerteEnvoyee;
    private LocalDate dateAjout;

    public LocalDate getDateAjout() {
        return dateAjout;
    }

    public void setDateAjout(LocalDate dateAjout) {
        this.dateAjout = dateAjout;
    }

    public boolean isAlerteEnvoyee() {
        return alerteEnvoyee;
    }
    @Transient
    private Double prixFinal; // Ce champ sera calculé en fonction des promotions et des périodes spécifiques

    @Transient
    private Double promotion; // Le pourcentage de réduction appliqué à l'article

    public Double getPrixFinal() {
        return prixFinal;
    }

    public void setPrixFinal(Double prixFinal) {
        this.prixFinal = prixFinal;
    }

    public Double getPromotion() {
        return promotion;
    }

    public void setPromotion(Double promotion) {
        this.promotion = promotion;
    }

    public void setAlerteEnvoyee(boolean alerteEnvoyee) {
        this.alerteEnvoyee = alerteEnvoyee;
    }

    public double getReduction() {
        return reduction;
    }

    public Integer getPointsFidelite() {
        return pointsFidelite;
    }

    public void setPointsFidelite(Integer pointsFidelite) {
        this.pointsFidelite = pointsFidelite;
    }

    public void setReduction(double reduction) {
        this.reduction = reduction;
    }

    @NotNull(message = "La quantité disponible ne peut pas être vide.")
    @Min(value = 0, message = "La quantité disponible ne peut pas être négative.")
    private Integer quantiteDisponible;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Integer getQuantiteDisponible() {
        return quantiteDisponible;
    }

    public void setQuantiteDisponible(Integer quantiteDisponible) {
        this.quantiteDisponible = quantiteDisponible;
    }

    public Integer getQuantiteVendue() {
        return quantiteVendue;
    }

    public void setQuantiteVendue(Integer quantiteVendue) {
        this.quantiteVendue = quantiteVendue;
    }
    @Column(name = "quantite_vendue")

    @NotNull(message = "La quantité vendue ne peut pas être vide.")
    @Min(value = 0, message = "La quantité vendue ne peut pas être négative.")
    private Integer quantiteVendue;


    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.1", message = "Le prix doit être supérieur à 0")
    Double prix;


    @Enumerated(EnumType.STRING)
StatusAgri statusAgri;

    public StatusAgri getStatusAgri() {
        return statusAgri;
    }

    public void setStatusAgri(StatusAgri statusAgri) {
        this.statusAgri = statusAgri;
    }





    TypeProduit typeProduit;
    private boolean archived = false; // Ajout du champ d'archivage

    @ManyToOne
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(long idArticle) {
        this.idArticle = idArticle;
    }



    public double getPrix() {
        return prix;
    }

    public void setPrix(double prix) {
        this.prix = prix;
    }



    public TypeProduit getTypeProduit() {
        return typeProduit;
    }

    public void setTypeProduit(TypeProduit typeProduit) {
        this.typeProduit = typeProduit;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void calculerPointsFidelite() {
        if (this.prix != null) {
            this.pointsFidelite = (int) (this.prix * 0.1); // 10% du prix
        } else {
            this.pointsFidelite = 0;
        }
    }

    private boolean isPack = false; // Indique si l'article est un pack

    @ElementCollection
    private List<Long> articlesPack = new ArrayList<>(); // Liste des articles dans le pack

    // Getters et Setters

    public boolean isPack() {
        return isPack;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }

    public void setPack(boolean pack) {
        isPack = pack;
    }

    public List<Long> getArticlesPack() {
        return articlesPack;
    }

    public void setArticlesPack(List<Long> articlesPack) {
        this.articlesPack = articlesPack;
    }
    @Lob  // Très important pour stocker une longue chaîne base64
    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
