import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EventsService } from 'src/app/events.service';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'app-event-add',
  templateUrl: './event-add.component.html',
  styleUrls: ['./event-add.component.css']
})

export class EventAddComponent  {
  eventForm!: FormGroup;
  selectedFile!: File;
  imageError: boolean | undefined;
  message: string = '';
  isError: boolean = false;
  minDate: string;
  recurrenceOptions: string[] = ['NONE', 'DAILY', 'WEEKLY', 'MONTHLY', 'CUSTOM'];
  daysOfWeek: string[] = ['SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];
  showRecurrenceEndDate: boolean = false;
  showRecurrenceDays: boolean = false;


  

  constructor(
    private fb: FormBuilder,
    private eventsService: EventsService,
    private http: HttpClient ,
    private router: Router,
           
  ) {

    // Date minimum = aujourd'hui au format YYYY-MM-DD
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    this.minDate = today.toISOString().split('T')[0];

    this.eventForm = this.fb.group({
      title: ['', [
        Validators.required, 
        Validators.minLength(3),
        Validators.maxLength(100),
        Validators.pattern(/^[^\s].*[^\s]$/)
      ]],
      description: ['', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(100)
      ]],
      dateEvent: ['', [
        Validators.required,
        this.futureOrPresentDateValidator
      ]],
      heureEvent: ['', Validators.required],
      prix: [0, [
        Validators.required,
        Validators.min(0.01),
        Validators.pattern(/^\d+(\.\d{1,2})?$/)
      ]],
      nombrePlaces: [0, [
        Validators.required,
        Validators.min(0),
        Validators.max(10000)
      ]],
      location: ['', [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(255),
        Validators.pattern(/^[a-zA-Z0-9,\s]+$/)
      ]],
      typeEvent: ['', Validators.required],
      status: ['', Validators.required],
      recurrence: ['NONE'],
      recurrenceEndDate: [''],
      recurrenceDaysOfWeek: [[]],
    });
  }

// Validateur personnalisé pour date future ou présente
futureOrPresentDateValidator(control: AbstractControl): { [key: string]: boolean } | null {
  if (!control.value) {
    return null;
  }

  const selectedDate = new Date(control.value);
  const today = new Date();
  today.setHours(0, 0, 0, 0); // Reset hours for accurate date comparison

  return selectedDate < today ? { pastDate: true } : null;
}
  // Gérer la sélection de fichier
  onFileSelected(event: any): void {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedFile = file;
      this.imageError = false; 
      this.eventForm.patchValue({ image: file });
      this.eventForm.get('image')?.updateValueAndValidity();
    } else {
      this.imageError = true;  
    }
  }
  

  // Soumettre le formulaire
  onSubmit(): void {
    if (!this.selectedFile) {
      this.imageError = true;
      return;
    }
  
    if (this.eventForm.valid) {
      const formData = new FormData();
  
      formData.append('title', this.eventForm.get('title')?.value);
      formData.append('description', this.eventForm.get('description')?.value);
      formData.append('prix', this.eventForm.get('prix')?.value);
      formData.append('nombrePlaces', this.eventForm.get('nombrePlaces')?.value);
      formData.append('location', this.eventForm.get('location')?.value);
      formData.append('typeEvent', this.eventForm.get('typeEvent')?.value);
      formData.append('status', this.eventForm.get('status')?.value);
      formData.append('dateEvent', this.eventForm.get('dateEvent')?.value);
      formData.append('heureEvent', this.eventForm.get('heureEvent')?.value);
      formData.append('image', this.selectedFile); 
      formData.append('recurrence', this.eventForm.value.recurrence);

      if (this.eventForm.value.recurrence !== 'NONE') {
        formData.append('recurrenceEndDate', this.eventForm.value.recurrenceEndDate);
        
        if (this.eventForm.value.recurrence === 'CUSTOM') {
          formData.append('recurrenceDaysOfWeek', this.eventForm.value.recurrenceDaysOfWeek.join(','));
        }
      }
  
      this.http.post<any>('http://localhost:8080/piEvent/event/addEvent', formData, {
        headers: new HttpHeaders().set('Accept', 'application/json')
      })
        .subscribe(
          (response: { message: any; }) => {
            console.log('Server Response:', response);
            alert(response.message);  // Afficher le message de succès
            this.router.navigate(['/admin/events']);

          },
          (error: { error: { error: any; }; }) => {
            console.error('Error adding event:', error);
            alert(error.error.error || "An error occurred while adding the event.");
          }
        );
      
      
  }
}


onDaySelected(event: any): void {
  const selectedDays = this.eventForm.get('recurrenceDaysOfWeek')?.value || [];
  const day = event.target.value;

  if (event.target.checked) {
    // Ajouter le jour à la liste si la case est cochée
    selectedDays.push(day);
  } else {
    // Retirer le jour de la liste si la case est décochée
    const index = selectedDays.indexOf(day);
    if (index !== -1) {
      selectedDays.splice(index, 1);
    }
  }

  // Mettre à jour la valeur du formulaire
  this.eventForm.get('recurrenceDaysOfWeek')?.setValue(selectedDays);
}

onRecurrenceChange(): void {
  const recurrence = this.eventForm.get('recurrence')?.value;

  // Logique pour afficher ou masquer les champs en fonction de la récurrence
  if (recurrence === 'NONE') {
    this.showRecurrenceEndDate = false;
    this.showRecurrenceDays = false;
  } else if (recurrence === 'DAILY' || recurrence === 'WEEKLY' || recurrence === 'MONTHLY') {
    this.showRecurrenceEndDate = true;
    this.showRecurrenceDays = true; // Optionnel, si vous voulez afficher les jours
  } else if (recurrence === 'CUSTOM') {
    this.showRecurrenceEndDate = true;
    this.showRecurrenceDays = true; // Optionnel pour des jours personnalisés
  }
}


} 