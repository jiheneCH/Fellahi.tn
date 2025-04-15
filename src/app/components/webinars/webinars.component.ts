import { Component, OnInit, ViewChild } from '@angular/core';
import Swal from 'sweetalert2';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalComponent } from 'angular-custom-modal';
import { WebinarsService } from 'src/app/service/webinars/webinars.service';
@Component({
  moduleId: module.id,
  templateUrl: './webinars.component.html',
  styleUrls: ['./webinars.component.css'],
  
})

export class WebinarsComponent implements OnInit {
  displayType = 'list';
  course: any = []; 
  filteredCourses: any = [];
  searchCourse = '';
  CourseForm!: FormGroup;

  @ViewChild('addCourseModal') addCourseModal!: ModalComponent;

  constructor(
    private fb: FormBuilder,
    private webinarsService: WebinarsService // Inject the service
    ) {}

    ngOnInit() {
      this.initForm();
      this.loadCourse();
  }

  initForm() {
    this.CourseForm = this.fb.group({
        id: [0],
        title: ['', Validators.required],
        video: ['', Validators.required],
       
        image: ['', Validators.required],
        // Add or remove form controls as necessary
    });

    
}



loadCourse() {
  this.webinarsService.getCourse().subscribe((course: any) => {
      this.course = course;
      this.filterCourses();
  });
  this.filterCourses();
}


filterCourses() {
  this.filteredCourses = this.course.filter((course: { title: string; }) =>
  course.title.toLowerCase().includes(this.searchCourse.toLowerCase())
  );
}

editCourse(course: any = null) {
  this.addCourseModal.open();
  this.initForm();
  if (course) {
      console.log("course value is",course);
      this.CourseForm.setValue({
          id: course.id,
          title: course.title,
          video: course.video,
          image: course.image,
         
      });
  }
}


saveCourse() {
  if (this.CourseForm.invalid) {
      Swal.fire('Error', 'Please check the form fields.', 'error');
      return;
  }

  const courseData = this.CourseForm.value;
  if (courseData.id) {
      // Update offer logic here
  } else {
      // Create new offer logic here
  }

  // Assuming you have methods in your service for save (create/update)
  this.webinarsService.createCourse(courseData).subscribe(() => {
      Swal.fire('Success', 'Internship offer has been saved successfully.', 'success');
      this.addCourseModal.close();
      this.loadCourse(); // Reload or update local data accordingly
  });
}

deleteCourse(courseData: any) {
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
          this.webinarsService.deleteCourse(courseData.id).subscribe(() => {
              Swal.fire(
                  'Deleted!',
                  'Your file has been deleted.',
                  'success'
              );
              this.loadCourse(); // Reload or update local data
          });
      }
  });
}


}
