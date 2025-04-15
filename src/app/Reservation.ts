


import { User } from "./User";
import { Event } from "./Event";

export class Reservation {
  idReservation!: number;
  event!: Event;
  user!: User;
  reservationDate!: string;
  numberOfPass!: number;
  prixTotal!: number;
  reference!: string;
}

