import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { Event } from 'src/app/Event';  // Assurez-vous que le chemin est correct

@Component({
  selector: 'app-event-edit',
  templateUrl: './event-edit.component.html',
  styleUrls: ['./event-edit.component.css']
})
export class EventEditComponent implements OnInit {
  eventForm!: FormGroup;
  selectedFile!: File | null;
  imageError: boolean | undefined;
  message: string = '';
  isError: boolean = false;
  minDate: string;
  event!: Event; // Déclarer l'objet event basé sur votre modèle Event
  imagePreviewUrl: string = '';


  constructor(
    private fb: FormBuilder,
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute
  ) {
    // Date minimum = aujourd'hui au format YYYY-MM-DD
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    this.minDate = today.toISOString().split('T')[0];
  }

  ngOnInit(): void {
    // Récupérer l'ID de l'événement depuis l'URL
    this.route.paramMap.subscribe(params => {
      const idEvent = Number(params.get('id')!);  // Convertir l'ID en number
      console.log('Event ID:', idEvent);  // Vérifiez que l'ID est correct dans la console
      if (idEvent > 0) {
        this.loadEvent(idEvent); // Charger les données de l'événement
      } else {
        alert('Invalid event ID');
        this.router.navigate(['/admin/events']);  // Rediriger si l'ID est invalide
      }
    });

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
      image: [''] 
    });
  }

  // Charger les données de l'événement à partir de l'API
  loadEvent(idEvent: number): void {
    this.http.get<Event>(`http://localhost:8080/piEvent/event/retrieveEvent/${idEvent}`).subscribe(
      (eventData) => {
        this.event = eventData; // Assigner l'objet Event récupéré
        // Pré-remplir le formulaire avec les données de l'événement
        this.eventForm.patchValue(eventData);
      },
      (error) => {
        console.error('Error fetching event data:', error);
        if (error.status === 500) {
          alert('Server error occurred while fetching the event data.');
        } else if (error.status === 404) {
          alert('Event not found.');
        } else {
          alert('An unexpected error occurred.');
        }
      }
    );
  }
  

  // Validateur personnalisé pour la date future ou présente
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

  // Soumettre le formulaire pour l'édition
  onSubmit() {
    if (this.eventForm.valid) {
      const updatedEvent = this.eventForm.value;
      const formData = new FormData();
      formData.append('title', updatedEvent.title);
      formData.append('description', updatedEvent.description);
      formData.append('dateEvent', updatedEvent.dateEvent);
      formData.append('heureEvent', updatedEvent.heureEvent);
      formData.append('prix', updatedEvent.prix);
      formData.append('nombrePlaces', updatedEvent.nombrePlaces);
      formData.append('location', updatedEvent.location);
      formData.append('typeEvent', updatedEvent.typeEvent);
      formData.append('status', updatedEvent.status);
  
      if (this.selectedFile) {
        formData.append('image', this.selectedFile, this.selectedFile.name);
      }
  
      // Envoi de la requête PUT
      this.http.put<any>(`http://localhost:8080/piEvent/event/updateEvent/${this.event.idEvent}`, formData)
        .subscribe(
          (response) => {
            this.message = 'Event updated successfully!';
            this.isError = false;
            this.router.navigate(['/admin/events']);
          },
          (error) => {
            console.error('Error updating event:', error);
            this.message = 'An error occurred while updating the event.';
            this.isError = true;
          }
        );
    }
  }
  
}
