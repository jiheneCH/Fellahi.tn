package tn.esprit.pi_article.Entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

    @NotNull(message = "La quantité vendue ne peut pas être vide.")
    @Min(value = 0, message = "La quantité vendue ne peut pas être négative.")
    private Integer quantiteVendue;


    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.1", message = "Le prix doit être supérieur à 0")
    double prix;


    @AssertTrue(message = "La quantité vendue ne peut pas dépasser la quantité disponible.")
    public boolean isQuantiteVendueValide() {
        return quantiteVendue != null && quantiteDisponible != null && quantiteVendue <= quantiteDisponible;
    }


    @NotNull(message = "Le statut est obligatoire")
    @Enumerated(EnumType.STRING)
    Status status;

    TypeProduit typeProduit;
    private boolean archived = false; // Ajout du champ d'archivage

    @ManyToOne
    @JoinColumn(name = "user_id") // Clé étrangère vers Utilisateur
    private utilisateur utilisateur;





    public tn.esprit.pi_article.Entities.utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(tn.esprit.pi_article.Entities.utilisateur utilisateur) {
        this.utilisateur = utilisateur;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
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
}
