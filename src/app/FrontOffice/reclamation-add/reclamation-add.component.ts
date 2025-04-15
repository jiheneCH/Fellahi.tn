import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReclamationService } from 'src/app/service/reclamation/reclamation.service';
import { Router } from '@angular/router'; // 

@Component({
  selector: 'app-reclamation-add',
  templateUrl: './reclamation-add.component.html',
  styleUrls: ['./reclamation-add.component.css']
})
export class ReclamationAddComponent implements OnInit {
  reclamationForm: FormGroup;
  isLoading = false;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  role: 'FARMER' | 'TRANSPORTER' | 'CLIENT' = 'CLIENT'; // fallback
  subjects: string[] = [];
  username: string | null = '';

  private subjectMap: Record<string, string[]> = {
    FARMER: [
      'DELIVERY_DELAY_BY_TRANSPORTER',
      'ORDER_CANCELLATION_BY_CLIENT',
      'INCORRECT_PRICE_OR_QUANTITY',
      'UNAVAILABILITY_OF_GOODS'
    ],
    TRANSPORTER: [
      'DELIVERY_DELAY_BY_CLIENT',
      'CLIENT_REFUSES_TO_ACCEPT_GOODS',
      'WRONG_PRODUCT_INFORMATION',
      'UNAVAILABILITY_OF_CLIENT'
    ],
    CLIENT: [
      'POOR_QUALITY_OF_GOODS',
      'DELAYED_DELIVERY',
      'WRONG_PRODUCT_DELIVERED',
      'GOODS_NOT_AS_DESCRIBED',
      'PRICE_DISCREPANCY'
    ]
  };

  constructor(
    private fb: FormBuilder,
    private reclamationService: ReclamationService,
    private router: Router 
  ) {
    this.reclamationForm = this.fb.group({
      subject: ['', Validators.required],
      description: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.getRoleFromToken();
    this.loadSubjectsForRole();
    this.username = localStorage.getItem('username');
  }

  getRoleFromToken(): void {
    const storedRole = localStorage.getItem('userRole')?.toUpperCase();
    if (storedRole && ['FARMER', 'TRANSPORTER', 'CLIENT'].includes(storedRole)) {
      this.role = storedRole as 'FARMER' | 'TRANSPORTER' | 'CLIENT';
    }
  }

  loadSubjectsForRole(): void {
    this.subjects = this.subjectMap[this.role] || [];
  }

  onAddReport(): void {
    if (this.reclamationForm.invalid) {
      this.reclamationForm.markAllAsTouched();
      return;
    }
  
    this.isLoading = true;
    this.errorMessage = null;
    this.successMessage = null;
  
    const reclamationData = {
      ...this.reclamationForm.value,
      role: this.role
    };
  
    this.reclamationService.addReclamation(reclamationData).subscribe({
      next: (res: string) => {
        this.successMessage = res || 'Complaint submitted successfully!';
        this.reclamationForm.reset();
        setTimeout(() => {
          this.router.navigate(['/client/reclamation']);
        }, 1000);
        this.isLoading = false;
      },
      error: (err) => {
        this.errorMessage = typeof err.error === 'string'
          ? err.error
          : (err.error?.message || 'Failed to submit complaint.');
        this.isLoading = false;
      }
    });
  }

  goBack(): void {
    this.router.navigate(['/client/reclamation']);
  }
  
  
}
