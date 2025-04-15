import { Role } from "./Role";

export class User {
    id!: number;
    username!: string;
    email!: string;
    password!: string;
    roleName!: string;
    role!: Role;
    badge!: string;
    reduction!: number;
    createdDate!: Date;
    lastModifiedDate!: Date;
    resetToken!: string;
    resetTokenExpiry!: Date;
}