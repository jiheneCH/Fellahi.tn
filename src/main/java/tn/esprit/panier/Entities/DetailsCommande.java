package tn.esprit.panier.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

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
    int prixTotalCommande;
    @Enumerated(EnumType.STRING)
    Status status;
    Date dateCommande;
    @JsonIgnore
    @OneToOne(mappedBy = "detailsCommande")
    private Commande commande;
    @ManyToOne
    Client client;

}
