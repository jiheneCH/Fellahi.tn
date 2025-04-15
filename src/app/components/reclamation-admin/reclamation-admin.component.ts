import { Component, OnInit } from '@angular/core';
import { ReclamationAdminService } from 'src/app/service/reclamation-admin/reclamation-admin.service';
import { Router } from '@angular/router';


export interface Reclamation {
  id: number;
  description: string;
  subject: string;
  createdAt: string;
  status: 'PENDING' | 'IN_PROGRESS' | 'RESOLVED' | 'REJECTED';
  user: {
    id: number;
    username: string;
    email: string;
    roleName: string;
  } | null;
}

@Component({
  selector: 'app-reclamation-admin',
  templateUrl: './reclamation-admin.component.html',
  styleUrls: ['./reclamation-admin.component.css']
})
export class ReclamationAdminComponent implements OnInit {
  reclamations: any[] = []; // Changed to any[] to handle mapped data
  // Added 'userId' to displayedColumns
  displayedColumns: string[] = ['id', 'userId', 'username', 'subject', 'description', 'status', 'createdDate', 'actions'];
  selectedStatus: string = '';

  constructor(private reclamationService: ReclamationAdminService, private router: Router) {}

  ngOnInit(): void {
    this.loadReclamations();
  }

  loadReclamations(): void {
    this.reclamationService.getAllReclamations().subscribe({
      next: (data: Reclamation[]) => {
        this.reclamations = data.map(reclamation => ({
          ...reclamation,
          // Flatten user data for template
          username: reclamation.user?.username || 'Unknown',
          userId: reclamation.user?.id || 'N/A',
          createdDate: reclamation.createdAt // Map createdAt to createdDate
        }));
      },
      error: (err) => console.error('Error loading reclamations:', err)
    });
  }

  onFilterByStatus(): void {
    if (this.selectedStatus) {
      this.reclamationService.getReclamationsByStatus(this.selectedStatus).subscribe({
        next: (data: Reclamation[]) => {
          this.reclamations = data.map(reclamation => ({
            ...reclamation,
            username: reclamation.user?.username || 'Unknown',
            userId: reclamation.user?.id || 'N/A',
            createdDate: reclamation.createdAt
          }));
        },
        error: (err) => console.error('Error filtering reclamations:', err)
      });
    } else {
      this.loadReclamations();
    }
  }

  getStatusClass(status: string): string {
    switch (status) {
      case 'IN_PROGRESS': return 'status-progress';
      case 'RESOLVED': return 'status-resolved';
      default: return '';
    }
  }

  openResponseDialog(reclamation: Reclamation) {
    this.router.navigate(['/admin/reclamation/treat'], {
      state: { reclamation: reclamation }
    })}

  markAsResolved(reclamation: any): void {
    console.log('Marking as resolved:', reclamation);
    // Implement status update logic here
  }
}