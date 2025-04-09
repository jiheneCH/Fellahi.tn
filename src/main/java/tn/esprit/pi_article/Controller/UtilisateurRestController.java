package tn.esprit.pi_article.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi_article.Entities.utilisateur;
import tn.esprit.pi_article.Services.UtilisateurServicelmpl;

import java.util.List;

@RestController
@RequestMapping("/utilisateur")
public class UtilisateurRestController {
    @Autowired
    private UtilisateurServicelmpl utilisateurService;


    @PostMapping("/add")
    public ResponseEntity<utilisateur> ajouterUtilisateur(@RequestBody utilisateur utilisateur) {
        utilisateur saved = utilisateurService.ajouterUtilisateur(utilisateur);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/all")
    public ResponseEntity<List<utilisateur>> getAll() {
        return ResponseEntity.ok(utilisateurService.getAllUtilisateurs());
    }
}
