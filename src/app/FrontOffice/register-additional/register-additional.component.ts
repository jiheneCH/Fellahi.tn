import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-register-additional',
  templateUrl: './register-additional.component.html',
  styleUrls: ['./register-additional.component.css']
})
export class RegisterAdditionalComponent implements OnInit {
  stepTwoForm!: FormGroup;
  selectedFile?: File;
  selectedImageUrl: string | ArrayBuffer | null = null;
  userId?: number;

  isLoading = false;
  errorMessage: string | null = null;

  readonly governorates: string[] = [
    'Ariana', 'Béja', 'Ben Arous', 'Bizerte', 'Gabès', 'Gafsa',
    'Jendouba', 'Kairouan', 'Kasserine', 'Kebili', 'La Manouba',
    'Le Kef', 'Mahdia', 'Médenine', 'Monastir', 'Nabeul', 'Sfax',
    'Sidi Bouzid', 'Siliana', 'Sousse', 'Tataouine', 'Tozeur',
    'Tunis', 'Zaghouan'
  ];

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) {}
  username: string = '';

  ngOnInit(): void {
    this.stepTwoForm = this.fb.group({
      phone: ['', [Validators.required, Validators.pattern(/^\+?\d{8,15}$/)]],
      address: ['', Validators.required],
      governorate: ['', Validators.required],
      profilePicture: [null] // if you're handling image upload
    });
  
    this.route.queryParams.subscribe(params => {
      const username = params['username'];
      if (username) {
        this.username = username;
      } else {
        this.errorMessage = 'Username is missing.';
      }
    });
  }
  

  onFileSelected(event: Event): void {
    const input = event.target as HTMLInputElement;
    if (input.files && input.files.length > 0) {
      this.selectedFile = input.files[0];

      const reader = new FileReader();
      reader.onload = () => {
        this.selectedImageUrl = reader.result;
      };
      reader.readAsDataURL(this.selectedFile);
    }
  }

  onSubmit(): void {
    if (this.stepTwoForm.invalid || !this.username) {
      this.errorMessage = 'Please complete the form correctly.';
      return;
    }
  
    this.isLoading = true;
    this.errorMessage = null;
  
    const formData = new FormData();
    formData.append('phone', this.stepTwoForm.value.phone);
    formData.append('address', this.stepTwoForm.value.address);
    formData.append('governorate', this.stepTwoForm.value.governorate);
    if (this.selectedFile) {
      formData.append('profileImage', this.selectedFile); 

    }
  
    this.http.post('http://localhost:8080/Fallehi/auth/register-complete?username=' + this.username, formData).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/activate-account']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Registration completion failed. Please try again.';
        console.error('Registration error:', err);
      }
    });
  }
  
}
