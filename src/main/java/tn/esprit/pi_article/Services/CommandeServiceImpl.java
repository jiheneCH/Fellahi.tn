package tn.esprit.pi_article.Services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi_article.Entities.Article;
import tn.esprit.pi_article.Entities.Commande;
import tn.esprit.pi_article.Entities.StatusCommande;
import tn.esprit.pi_article.Repositories.ArticleRepository;
import tn.esprit.pi_article.Repositories.CommandeRepository;
@AllArgsConstructor

@Service

public class CommandeServiceImpl implements ICommandeServices {
    @Autowired
    private   CommandeRepository commandeRepository;

    @Autowired
    private ArticleRepository articleRepository;

    @Transactional
    public void traiterCommande(Long idCommande) {
        // Récupérer la commande par son ID
        Commande commande = commandeRepository.findById(idCommande)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        // Vérifier si la commande est validée
        if (commande.getStatus() == StatusCommande.valider) {
            Article article = commande.getArticle();

            // Vérifier la disponibilité de l'article
            if (commande.getQuantite() <= article.getQuantiteDisponible()) {
                // Mettre à jour l'article : diminuer la quantité disponible et augmenter la quantité vendue
                article.setQuantiteDisponible(article.getQuantiteDisponible() - commande.getQuantite());
                article.setQuantiteVendue(article.getQuantiteVendue() + commande.getQuantite());

                // Sauvegarder l'article mis à jour
                articleRepository.save(article);

                // Sauvegarder la commande (même si elle est déjà sauvegardée, cela permet de gérer les changements d'état si nécessaire)
                commandeRepository.save(commande);

                // Logique supplémentaire si besoin (envoi de notifications, mise à jour des statistiques, etc.)
            } else {
                throw new RuntimeException("Quantité insuffisante pour l'article " + article.getNom());
            }
        } else {
            throw new RuntimeException("La commande n'est pas validée");
        }
    }
    public void creerCommande(Commande commande) {
        // Sauvegarder la commande dans la base de données
        commandeRepository.save(commande);
    }
}
