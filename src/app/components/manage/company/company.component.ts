import { Component, ViewChild, OnInit } from '@angular/core';
import Swal from 'sweetalert2';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalComponent } from 'angular-custom-modal';
import { CompanyService } from 'src/app/service/manage/company.service';


@Component({
  selector: 'app-company',
  templateUrl: './company.component.html',
})
export class CompanyComponent implements OnInit {
  displayType : string = 'list';
  companyForm: FormGroup;
  companyList: any[] = [];
  filteredCompanies: any[] = [];
  searchQuery = '';

  @ViewChild('companyModal') companyModal!: ModalComponent;

  constructor(
    private fb: FormBuilder,
    private companyService: CompanyService // Injecting the CompanyService
  ) {
    this.companyForm = this.fb.group({
      id: [null],
      idCompany: [''],
      name: ['', Validators.required],
      image: [''],
      email: ['', [Validators.required, Validators.email]],
      phone: ['', Validators.required],
      address: [''],
      PfeBook: [''],
    });
  }

  ngOnInit(): void {
    this.loadCompanies();
  }

  loadCompanies(): void {
    this.companyService.getCompanies().subscribe(
      (data) => {
        this.companyList = data;
        this.filteredCompanies = this.companyList;
      },
      (error) => {
        console.error('Error fetching companies', error);
        this.showMessage('Failed to load companies', 'error');
      }
    );
  }


  generateIdCompany(): string {
    const yearPrefix = new Date().getFullYear().toString().slice(-2); // Last two digits of the year
    const yearCode = parseInt(yearPrefix) - 1 + '4'; // Assuming '23' for last year and '4' for this year logic
    const nameInitials = this.companyForm.value.name ? this.companyForm.value.name.toUpperCase().substring(0, 2) : 'XX';
    const randomNumbers = Math.floor(1000 + Math.random() * 9000); // Generate 4 random digits
  
    return `${yearCode}C${nameInitials}${randomNumbers}`;
  }


  searchCompanies(): void {
    this.filteredCompanies = this.companyList.filter((company) =>
      company.name.toLowerCase().includes(this.searchQuery.toLowerCase())
    );
  }

  editCompany(company: any = null): void {
    this.companyModal.open();
    if (company) {
      this.companyForm.patchValue(company);
    } else {
      this.companyForm.reset();
    }
  }

  saveCompany(): void {
    if (this.companyForm.invalid) {
      Swal.fire('Error', 'Please fill out the form correctly.', 'error');
      return;
    }

    const companyData = this.companyForm.value;
    if (companyData.id) {
      this.companyService.updateCompany(companyData).subscribe(
        () => {
          this.loadCompanies(); // Reload the list
          this.companyModal.close();
          this.showMessage('Company updated successfully');
        },
        (error) => {
          console.error('Error updating company', error);
          this.showMessage('Failed to update company', 'error');
        }
      );
    } else {
      companyData.idCompany = this.generateIdCompany();
      this.companyService.addCompany(companyData).subscribe(
        () => {
          this.loadCompanies(); // Reload the list
          this.companyModal.close();
          this.showMessage('Company added successfully');
        },
        (error) => {
          console.error('Error adding company', error);
          this.showMessage('Failed to add company', 'error');
        }
      );
    }
  }

  deleteCompany(companyId: number): void {
    this.companyService.deleteCompany(companyId).subscribe(
      () => {
        this.loadCompanies(); // Reload the list
        this.showMessage('Company deleted successfully');
      },
      (error) => {
        console.error('Error deleting company', error);
        this.showMessage('Failed to delete company', 'error');
      }
    );
  }

  // Helper method for displaying messages
  showMessage(message: string, type: 'success' | 'error' = 'success'): void {
    Swal.fire({
      icon: type,
      title: message,
      showConfirmButton: false,
      timer: 3000,
    });
  }
}
