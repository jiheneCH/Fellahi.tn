// deliveries.model.ts

export interface Client {
  prenom: string;
  nom: string;
  delegation: string;
  adresse: string;
  numero: string;
}

export interface Transporteur {
  nom: string;
  fname: string;
  delegation: string;
  adresse: string;
  num: string;
 
}

export interface Delivery {
  id: number;
  refLivraison: String;
  client: Client;
  transporteur: Transporteur;
  prixTotal: number;
  dateLivraison: Date;
  statut: string;
  dateIncident: Date,
  description: String,
  statutIncident: String,
  typeIncident: String,
}
