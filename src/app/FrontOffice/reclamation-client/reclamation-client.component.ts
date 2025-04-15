import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ReclamationService } from 'src/app/service/reclamation/reclamation.service';

@Component({
  selector: 'app-reclamation-client',
  templateUrl: './reclamation-client.component.html',
  styleUrls: ['./reclamation-client.component.css']
})
export class ReclamationClientComponent implements OnInit {
  reports: any[] = [];
  selectedReport: any = null;
  showModal = false;

  constructor(
    private router: Router,
    private reclamationService: ReclamationService
  ) {}

  ngOnInit(): void {
    this.fetchReports();
  }

  fetchReports() {
    this.reclamationService.getAllReclamations().subscribe({
      next: (data) => {
        console.log('Fetched reclamations:', data); // 👈 Log the full response
        this.reports = data;
      },
      error: (err) => {
        console.error('Failed to load reports:', err);
      }
    });
  }
  

  onAddReport() {
    this.router.navigate(['/reclamation/add']);
  }

  onViewReport(report: any) {
    this.selectedReport = report;
    this.showModal = true;
  }

  closeModal() {
    this.showModal = false;
    this.selectedReport = null;
  }
}
