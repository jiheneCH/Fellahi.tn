import { Status } from "./Status";
import { TypeEvent } from "./TypeEvent";

export class Event {
    idEvent!: number;
    title!: string;
    dateEvent!: string; // ISO string format: 'yyyy-MM-dd'
    heureEvent!: string; // format 'HH:mm:ss'
    location!: string;
    description!: string;
    nombrePlaces!: number;
    prix!: number;
    image!: string;
    pdf!: string; 
    archived!: boolean;
    typeEvent!: TypeEvent;
    status!: Status;
    reference!: string;
    
  }