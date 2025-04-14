import { Component } from '@angular/core';

import {  ViewChild, OnInit } from '@angular/core';
import Swal from 'sweetalert2';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalComponent } from 'angular-custom-modal';
import { StudentService } from 'src/app/service/manage/student.service';


@Component({
  selector: 'app-company',
  templateUrl: './student.component.html',
})
export class StudentComponent implements OnInit {
  displayType : string = 'list';
  studentForm: FormGroup;
  studentList: any[] = [];
  filteredStudents: any[] = [];
  searchQuery = '';

  @ViewChild('studentModal') studentModal!: ModalComponent;

  constructor(
    private fb: FormBuilder,
    private studentService: StudentService // Injecting the StudentService
  ) {
    this.studentForm = this.fb.group({
      id: [null],
      idStudent: [""],
      firstName: ['', Validators.required],
      lastName: ['', Validators.required],
      phoneNumber: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', Validators.required],
    });
  }

  ngOnInit(): void {
    this.loadStudents();
  }

  loadStudents(): void {
    this.studentService.getStudents().subscribe(
      (data) => {
        this.studentList = data;
        this.filteredStudents = this.studentList;
      },
      (error) => {
        console.error('Error fetching students', error);
        this.showMessage('Failed to load students', 'error');
      }
    );
  }

  generateIdStudent(): string {
    const yearPrefix = new Date().getFullYear().toString().slice(-2); // Last two digits of the year
    const yearCode = parseInt(yearPrefix) - 1 + '4'; // Assuming '23' for last year and '4' for this year logic
    const nameInitials = this.studentForm.value.name ? this.studentForm.value.name.toUpperCase().substring(0, 2) : 'XX';
    const randomNumbers = Math.floor(1000 + Math.random() * 9000); // Generate 4 random digits
  
    return `${yearCode}C${nameInitials}${randomNumbers}`;
  }


  searchstudents(): void {
    this.filteredStudents = this.studentList.filter((student) =>
    student.name.toLowerCase().includes(this.searchQuery.toLowerCase())
    );
  }

  editStudent(student: any = null): void {
    this.studentModal.open();
    if (student) {
      this.studentForm.patchValue(student);
    } else {
      this.studentForm.reset();
    }
  }

  saveStudent(): void {
    if (this.studentForm.invalid) {
      Swal.fire('Error', 'Please fill out the form correctly.', 'error');
      return;
    }

    const studentData = this.studentForm.value;
    if (studentData.id) {
      this.studentService.updateStudent(studentData).subscribe(
        () => {
          this.loadStudents(); // Reload the list
          this.studentModal.close();
          this.showMessage('Student updated successfully');
        },
        (error) => {
          console.error('Error updating student', error);
          this.showMessage('Failed to update student', 'error');
        }
      );
    } else {
      studentData.idStudent = this.generateIdStudent();
      this.studentService.addStudent(studentData).subscribe(
        () => {
          this.loadStudents(); // Reload the list
          this.studentModal.close();
          this.showMessage('Student added successfully');
        },
        (error) => {
          console.error('Error adding student', error);
          this.showMessage('Failed to add student', 'error');
        }
      );
    }
  }

  deleteStudent(studentId: number): void {
    this.studentService.deleteStudent(studentId).subscribe(
      () => {
        this.loadStudents(); // Reload the list
        this.showMessage('Student deleted successfully');
      },
      (error) => {
        console.error('Error deleting student', error);
        this.showMessage('Failed to delete student', 'error');
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
