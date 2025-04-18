export enum StatusPayment {
    PENDING = 'PENDING',
    ONSITE = 'ONSITE',
    ONLINE = 'ONLINE',
  }
  
  export enum StatusCommande {
    Processing = 'Processing',
    Pending = 'Pending',
    Confirmed = 'Confirmed',
    Cancelled = 'Cancelled',
  }
  
  export interface Commande {
    idCommande: number;
    prixTotalCommande: number;
    pointfidelityTotalCommande: number;
    referenceCommande: string | null;
    statusCommande: StatusCommande;
    payment: StatusPayment;
    dateCommande: string;
    user: {
      id: number;
      username: string;
      phone: string;
      address: string;
      pointFidelityClient: number;
    };
    detailsCommande: {
      idDetailsCommande: number;
      quantite: number;
      prixTotalArticle: number;
      pointfidelityTotalArticle: number;
      article: {
        idArticle: number;
        nom: string;
        reference: string;
        pointsFidelite: number;
        prix: number;
        statusAgri: string;
      };
    };
  }
  