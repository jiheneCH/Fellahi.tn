export interface Article {
  promotion: any;
promo: any;
bio: any;
categorie: any;
description: any;
unite: any;
origine: any;
  ventes: number;
    archived: any;
  idArticle: number;
      nom: string;
      prix: number;
      quantite: number;
      seuilCritique: number;
      reference?: string;
      pointsFidelite?: number;
      quantiteInitiale?: number;
      quantiteDisponible: number;
      quantiteVendue: number;
      typeProduit: TypeProduit;
      status: Status;
      dateAjout: Date;
      image?: string;
      isPack?: boolean; 

    }
    
    export enum TypeProduit {
      ALIMENTAIRE = 'legumes_et_fruits',
      equipements = 'equipements',
      AUTRE = 'insec',
      fruit = 'fruit',
      legume = 'legume',
      cereale = 'cereale',
      produitLaitier = 'produitLaitier',
      viande = 'viande',
      insec = 'insec'
    }
    
    export enum Status {
      InStock = 'InStock',
      OutOfStock = 'OutOfStock',
      LowStock = 'LowStock'
    }
    