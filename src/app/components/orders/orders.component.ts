import { Component, OnInit, ViewChild } from '@angular/core';
import Swal from 'sweetalert2';
import * as XLSX from 'xlsx';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalComponent } from 'angular-custom-modal';
// Assume InternshipOfferService exists and is implemented to interact with your backend
import { InternshipOfferService } from '../../service/internship/internship.service';
@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.css']
})
export class OrdersComponent {


  displayType = 'list';
     internshipOffers: any = []; // This should ideally be typed based on your model
     filteredInternshipOffers: any = []; // Same as above
     searchOffer = '';
     offerForm!: FormGroup;
 
     @ViewChild('addOfferModal') addOfferModal!: ModalComponent;
 
     constructor(
         private fb: FormBuilder,
         private internshipOfferService: InternshipOfferService // Inject the service
         ) {}
 
     ngOnInit() {
         this.initForm();
         this.loadInternshipOffers();
         localStorage.setItem('loginToken', 'eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzdHJpbmciLCJpYXQiOjE3MDk4MTgwMDMsImV4cCI6MTcxMTI4OTIzMn0.BcGMR9PO-UK8CRn0Y9rSxhkDmXnxsdesjKN-Mg-edE8npm'); // Set the token in localStorage
     }
 
     onFileChange(event: any) {
         if (event.target.files && event.target.files.length > 0) {
             const file = event.target.files[0];
             this.uploadFile(file);
         } else {
             console.error('No file selected!');
         }
     }
     
     
 
     initForm() {
         this.offerForm = this.fb.group({
             id: [0],
             title: ['', Validators.required],
             description: ['', Validators.required],
             location: [''],
             duration: [''],
             paid: [false],
             type: ['', Validators.required],
             // Add or remove form controls as necessary
         });
     }
 
     loadInternshipOffers() {
         this.internshipOfferService.getInternshipOffers().subscribe((offers: any) => {
             this.internshipOffers = offers;
             this.filterInternshipOffers();
         });
         this.filterInternshipOffers();
     }
 
 
     uploadFile(file: File) {
         const formData = new FormData();
         formData.append('file', file);
         console.log(formData.forEach((value, key) => {
             console.log(key + ' ' + value);
         }
         ));
     
         // Assuming your service has a method to upload files
         this.internshipOfferService.uploadInternshipOffersExcel(formData).subscribe(() => {
             Swal.fire('Success', 'Internship offers imported successfully.', 'success');
             this.loadInternshipOffers(); // Reload or update local data accordingly
         }, error => {
             console.error(error);
             Swal.fire('Error', 'There was an error importing the file.', 'error');
         });
     }
 
     
 
     filterInternshipOffers() {
         this.filteredInternshipOffers = this.internshipOffers.filter((offer: { title: string; }) =>
             offer.title.toLowerCase().includes(this.searchOffer.toLowerCase())
         );
     }
 
     editOffer(offer: any = null) {
         this.addOfferModal.open();
         this.initForm();
         if (offer) {
             this.offerForm.setValue({
                 id: offer.id,
                 title: offer.title,
                 description: offer.description,
                 location: offer.location,
                 duration: offer.duration,
                 isPaid: offer.paid,
                 style: offer.type,
             });
         }
     }
 
     saveOffer() {
         if (this.offerForm.invalid) {
             Swal.fire('Error', 'Please check the form fields.', 'error');
             return;
         }
 
         const offerData = this.offerForm.value;
         if (offerData.id) {
             // Update offer logic here
             
         } else {
             // Create new offer logic here
         }
 
         // Assuming you have methods in your service for save (create/update)
         this.internshipOfferService.createInternshipOffer(offerData).subscribe(() => {
             Swal.fire('Success', 'Internship offer has been saved successfully.', 'success');
             this.addOfferModal.close();
             this.loadInternshipOffers(); // Reload or update local data accordingly
         });
     }
 
     deleteOffer(offer: any) {
         // Delete offer logic here
         Swal.fire({
             title: 'Are you sure?',
             text: "You won't be able to revert this!",
             icon: 'warning',
             showCancelButton: true,
             confirmButtonColor: '#3085d6',
             cancelButtonColor: '#d33',
             confirmButtonText: 'Yes, delete it!'
         }).then((result) => {
             if (result.isConfirmed) {
                 // Assuming delete method exists
                 this.internshipOfferService.deleteInternshipOffer(offer.id).subscribe(() => {
                     Swal.fire(
                         'Deleted!',
                         'Your file has been deleted.',
                         'success'
                     );
                     this.loadInternshipOffers(); // Reload or update local data
                 });
             }
         });
     }
}
