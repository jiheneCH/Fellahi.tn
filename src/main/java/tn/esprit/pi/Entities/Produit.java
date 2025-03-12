package tn.esprit.pi.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Produit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private Double prix;
    private String nomAgr;
    private String addAgr;
    private Long numTelAgri;

    public String getNomAgr() {
        return nomAgr;
    }

    public void setNomAgr(String nomAgr) {
        this.nomAgr = nomAgr;
    }

    public Long getNumTelAgri() {
        return numTelAgri;
    }

    public void setNumTelAgri(Long numTelAgri) {
        this.numTelAgri = numTelAgri;
    }

    public String getAddAgr() {
        return addAgr;
    }

    public void setAddAgr(String addAgr) {
        this.addAgr = addAgr;
    }

    @ManyToOne
    private Livraison livraison;

    public Livraison getLivraison() {
        return livraison;
    }

    public void setLivraison(Livraison livraison) {
        this.livraison = livraison;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public Double getPrix() {
        return prix;
    }

    public void setPrix(Double prix) {
        this.prix = prix;
    }
}
