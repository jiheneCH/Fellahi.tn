export enum ReactionType {
    LIKE, // Add other types as per your backend
    DISLIKE
  }

  export interface Reaction {
    id?: number;
    reactionType: ReactionType;
    postId?: number;
    userId: number;
  }
