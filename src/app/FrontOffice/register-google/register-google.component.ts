import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';



@Component({
  selector: 'app-register-google',
  templateUrl: './register-google.component.html',
  styleUrls: ['./register-google.component.css']
})


export class RegisterGoogleComponent implements OnInit {
  stepTwoForm!: FormGroup;
  selectedFile?: File;
  selectedImageUrl: string | ArrayBuffer | null = null;
  username: string = '';

  isLoading = false;
  errorMessage: string | null = null;

  readonly governorates: string[] = [
    'Ariana', 'Béja', 'Ben Arous', 'Bizerte', 'Gabès', 'Gafsa',
    'Jendouba', 'Kairouan', 'Kasserine', 'Kebili', 'La Manouba',
    'Le Kef', 'Mahdia', 'Médenine', 'Monastir', 'Nabeul', 'Sfax',
    'Sidi Bouzid', 'Siliana', 'Sousse', 'Tataouine', 'Tozeur',
    'Tunis', 'Zaghouan'
  ];

  readonly roles: string[] = ['CLIENT', 'FARMER', 'TRANSPORTER']; // Optional role list for select

  showPassword = false;
  showRePassword = false;

  constructor(
    private fb: FormBuilder,
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.stepTwoForm = this.fb.group(
      {
        phone: ['', [Validators.required, Validators.pattern(/^\+?\d{8,15}$/)]],
        address: ['', Validators.required],
        governorate: ['', Validators.required],
        role: ['', Validators.required], // ✅ Add role field
        profilePicture: [null],
        password: ['', [Validators.required, Validators.minLength(6)]],
        rePassword: ['', Validators.required]
      },
      { validators: this.passwordMatchValidator }
    );

    this.route.queryParams.subscribe(params => {
      const usernameParam = params['username'];
      if (usernameParam) {
        this.username = usernameParam;
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
    formData.append('role', this.stepTwoForm.value.role); // ✅ Add role to payload
    formData.append('password', this.stepTwoForm.value.password);
    if (this.selectedFile) {
      formData.append('profilePicture', this.selectedFile);
    }

    this.http.put(`http://localhost:8080/Fallehi/auth/google-complete?username=${this.username}`, formData).subscribe({
      next: () => {
        this.isLoading = false;
        this.router.navigate(['/login']);
      },
      error: (err) => {
        this.isLoading = false;
        this.errorMessage = 'Registration completion failed. Please try again.';
        console.error('Registration error:', err);
      }
    });
  }

  passwordMatchValidator(group: FormGroup): { [key: string]: boolean } | null {
    const password = group.get('password')?.value;
    const rePassword = group.get('rePassword')?.value;
    return password && rePassword && password !== rePassword ? { mismatch: true } : null;
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  toggleRePasswordVisibility(): void {
    this.showRePassword = !this.showRePassword;
  }
}
