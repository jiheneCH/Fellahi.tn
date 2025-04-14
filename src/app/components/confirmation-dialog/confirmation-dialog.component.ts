import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-confirmation-dialog',
  template: `
    <div class="dialog-container">
      <div class="dialog-header">
        <h2>{{ data.title }}</h2>
      </div>
      
      <div class="dialog-content">
        <p>{{ data.message }}</p>
        
        <div class="product-details" *ngIf="data.productDetails">
          <div class="detail-item" *ngFor="let detail of getDetailsArray()">
            <strong>{{ detail.label }}:</strong> {{ detail.value }}
          </div>
        </div>
      </div>
      
      <div class="dialog-actions">
        <button class="cancel-btn" (click)="onCancel()">Annuler</button>
        <button class="confirm-btn" (click)="onConfirm()">Confirmer</button>
      </div>
    </div>
  `,
  styles: [`
    .dialog-container {
      padding: 24px;
      max-width: 400px;
      font-family: 'Segoe UI', sans-serif;
    }
    
    .dialog-header {
      margin-bottom: 16px;
    }
    
    .dialog-header h2 {
      margin: 0;
      color: #333;
      font-size: 1.5rem;
    }
    
    .dialog-content {
      margin-bottom: 24px;
      line-height: 1.5;
    }
    
    .product-details {
      margin-top: 16px;
      padding: 12px;
      background: #f8f9fa;
      border-radius: 8px;
    }
    
    .detail-item {
      margin: 8px 0;
    }
    
    .detail-item strong {
      color: #555;
    }
    
    .dialog-actions {
      display: flex;
      justify-content: flex-end;
      gap: 12px;
    }
    
    button {
      padding: 10px 20px;
      border-radius: 6px;
      font-weight: 500;
      cursor: pointer;
      transition: all 0.2s ease;
      border: none;
    }
    
    .cancel-btn {
      background: #f1f3f5;
      color: #333;
    }
    
    .cancel-btn:hover {
      background: #e9ecef;
    }
    
    .confirm-btn {
      background: #2b8a3e;
      color: white;
    }
    
    .confirm-btn:hover {
      background: #2f9e44;
    }
  `]
})
export class ConfirmationDialogComponent {
  constructor(
    public dialogRef: MatDialogRef<ConfirmationDialogComponent>,
    @Inject(MAT_DIALOG_DATA) public data: {
      title: string,
      message: string,
      productDetails?: string
    }
  ) {}

  getDetailsArray(): {label: string, value: string}[] {
    if (!this.data.productDetails) return [];
    
    return this.data.productDetails.split('\n')
      .filter(line => line.includes(':'))
      .map(line => {
        const [label, value] = line.split(':').map(part => part.trim());
        return {label, value};
      });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  onConfirm(): void {
    this.dialogRef.close('confirm');
  }
}