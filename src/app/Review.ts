

export class Review {
    id!: number;
    event!: {
      idEvent: number;
      // Ajoute d'autres propriétés si tu veux les afficher
    };
    user!: {
      id: number;
      username?: string; // optionnel si jamais tu renvoies le nom depuis ton backend
      // tu peux ajouter d'autres propriétés si tu les exposes dans le JSON
    };
    rating!: number;
    comment!: string;
    createdAt!: string; // Date sous forme ISO (format JSON standard), Angular la traite bien
  }
  