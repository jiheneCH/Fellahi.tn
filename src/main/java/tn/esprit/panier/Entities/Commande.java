package tn.esprit.panier.Entities;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Setter
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Commande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idCommande;
    private int quantite;
    private int prixTotalArticle;
    @OneToOne
    private DetailsCommande detailsCommande;
    @OneToMany(mappedBy = "commande")
    Set<Article> article;
}
