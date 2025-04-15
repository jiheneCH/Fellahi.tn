import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ReclamationService } from 'src/app/service/reclamation/reclamation.service';
import { Router } from '@angular/router';



@Component({
  selector: 'app-reclamation-client',
  templateUrl: './reclamation-client.component.html',
  styleUrls: ['./reclamation-client.component.css']
})
export class ReclamationClientComponent implements OnInit {
  forgotForm: FormGroup;
  reclamationForm: FormGroup;
  isLoading = false;
  errorMessage: string | null = null;
  successMessage: string | null = null;

  reports: any[] = [];

  constructor(private router: Router, 
    private fb: FormBuilder,
    private reclamationService: ReclamationService
  ) {
    this.forgotForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });

    this.reclamationForm = this.fb.group({
      subject: ['', Validators.required],
      description: ['', Validators.required]
    });
  }

  ngOnInit(): void {
    this.fetchReports();
  }

  fetchReports() {
    this.reclamationService.getAllReclamations().subscribe({
      next: (data) => {
        this.reports = data;
      },
      error: (err) => {
        console.error('Failed to load reports:', err);
        this.errorMessage = 'Failed to load reports.';
      }
    });
  }

  onAddReport() {
    this.router.navigate(['/reclamation/add']);
    
  }
}
