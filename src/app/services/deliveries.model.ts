// deliveries.model.ts

export interface Client {
  prenom: string;
  nom: string;
  delegation: string;
  adresse: string;
  numero: string;
}

export interface Transporteur {
  prenom: string;
  nom: string;
  delegation: string;
  adresse: string;
  numero: string;
 
}

export interface Delivery {
  id: number;
  client: Client;
  transporteur: Transporteur;
  prixTotal: number;
  dateLivraison: Date; // ou Date si tu veux utiliser un objet Date
  statut: string;
  dateIncident: Date,
  description: String,
  statutIncident: String,
  typeIncident: String,
}
