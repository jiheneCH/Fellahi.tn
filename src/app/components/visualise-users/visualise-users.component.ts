import { Component, OnInit } from '@angular/core';
import { UserService, User } from 'src/app/service/user/user-service.service';

@Component({
  selector: 'app-visualise-users',
  templateUrl: './visualise-users.component.html',
  styleUrls: ['./visualise-users.component.css']
})
export class VisualiseUsersComponent implements OnInit {
  displayedColumns: string[] = ['username', 'email', 'role', 'status', 'createdDate'];
  users: User[] = [];
  searchTerm: string = '';
  selectedRole: string = '';
  availableRoles: string[] = ['ADMIN', 'FARMER', 'CLIENT', 'TRANSPORTER'];
  isLoading = false;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.Refresh(); // Load all users initially
  }

  Refresh(): void {
    this.isLoading = true;
    this.userService.getAllUsers().subscribe({
      next: (data) => {
        this.users = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('❌ Error fetching all users:', err);
        this.isLoading = false;
      }
    });
  }

  onFilterByRole(): void {
    if (!this.selectedRole) {
      this.Refresh(); // Reset to full list
      return;
    }

    this.isLoading = true;
    this.userService.getUsersByRole(this.selectedRole).subscribe({
      next: (data) => {
        if (typeof data === 'string') {
          // If API returns a message like "No users found"
          this.users = [];
          console.warn(data);
        } else {
          this.users = data;
        }
        this.isLoading = false;
      },
      error: (err) => {
        console.error(`❌ Error fetching users by role (${this.selectedRole}):`, err);
        this.users = [];
        this.isLoading = false;
      }
    });
  }

  getRoleClass(role: string): string {
    switch (role?.toUpperCase()) {
      case 'ADMIN':
        return 'role-admin';
      case 'FARMER':
        return 'role-farmer';
      case 'CLIENT':
        return 'role-client';
      case 'TRANSPORTER':
        return 'role-transporter';
      default:
        return 'role-default';
    }
  }

  getStatusClass(status: string): string {
    switch (status?.toUpperCase()) {
      case 'ACTIVE':
        return 'status-active';
      case 'PENDING':
        return 'status-pending';
      case 'SUSPENDED':
        return 'status-suspended';
      case 'INACTIVE':
        return 'status-inactive';
      default:
        return '';
    }
  }
  onRoleChange(role: string): void {
    this.selectedRole = role;
    this.onFilterByRole();
  }
  getRoleIcon(role: string): string {
    switch (role.toLowerCase()) {
      case 'admin':
        return 'admin_panel_settings';
      case 'farmer':
        return 'spa';
      case 'transporter':
        return 'local_shipping';
      case 'client':
        return 'person';
      default:
        return 'help_outline';
    }
  }
  onSearchUsername(): void {
    const trimmedSearch = this.searchTerm.trim();
  
    if (trimmedSearch.length === 0) {
      this.Refresh(); // If search is cleared, load all users
      return;
    }
  
    this.isLoading = true;
    this.userService.searchUsersByUsername(trimmedSearch).subscribe({
      next: (data) => {
        this.users = data;
        this.isLoading = false;
      },
      error: (err) => {
        console.error('❌ Error searching users by username:', err);
        this.users = [];
        this.isLoading = false;
      }
    });
  }
  
  
}
