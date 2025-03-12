package tn.esprit.pi_article.Entities;

import jakarta.persistence.*;

import java.util.List;
@Entity
public class utilisateur {
    @Id
    private Long id = 100L;

    private String nom;
    private String email;
    private String role; // "Agriculteur" ou "Client"

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<Article> articles;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Article> getArticle() {
        return articles;
    }

    public void setArticle(List<Article> article) {
        this.articles = article;
    }
}
