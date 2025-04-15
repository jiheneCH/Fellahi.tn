import { User} from "./User";
import { Event } from "./Event";

export class WaitingListEntry {
  id!: number; 
  requestedPasses!: number;
  requestTime!: string; // ISO string pour LocalDateTime
  reference!: string
  event!: Event;
  user!: User;
}
