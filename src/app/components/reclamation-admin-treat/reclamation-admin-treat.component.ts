import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReclamationAdminService } from 'src/app/service/reclamation-admin/reclamation-admin.service';

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
  selector: 'app-reclamation-admin-treat',
  templateUrl: './reclamation-admin-treat.component.html',
  styleUrls: ['./reclamation-admin-treat.component.css']
})
export class ReclamationAdminTreatComponent implements OnInit {
  selectedReclamation: Reclamation | null = null;
  responseMessage: string = '';
  selectedStatus: string = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private reclamationService: ReclamationAdminService
  ) {}

  ngOnInit(): void {
    const reclamationData = history.state.reclamation as Reclamation | undefined;
    if (reclamationData) {
      this.selectedReclamation = reclamationData;
      this.selectedStatus = reclamationData.status; // Initialize status dropdown with current status
    } else {
      this.router.navigate(['/admin/reclamation']);
    }
  }

  submitResponse(): void {
    if (!this.selectedReclamation || !this.responseMessage || !this.selectedStatus) return;
  
    const username = localStorage.getItem('username');
    if (!username) {
      alert('Admin username not found in local storage. Please log in again.');
      this.router.navigate(['/login']);
      return;
    }
  
    const responsePayload = {
      reclamationId: this.selectedReclamation.id,
      responseMessage: this.responseMessage,
      updatedStatus: this.selectedStatus as 'IN_PROGRESS' | 'RESOLVED',
      adminUsername: username
    };
  
    this.reclamationService.respondToReclamation(this.selectedReclamation.id, responsePayload).subscribe({
      next: () => {
        alert('Response submitted successfully.');
        this.router.navigate(['/admin/reclamation']);
      },
      error: (err) => {
        console.error('Error submitting response:', err);
        alert('Something went wrong while submitting the response.');
      }
    });
  }
  

  goBack(): void {
    this.router.navigate(['/admin/reclamation']);
  }
}
