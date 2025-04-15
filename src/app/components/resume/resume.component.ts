import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { Component, OnInit, ViewChild } from '@angular/core';
import { FontIconsComponent } from '../font-icons/font-icons';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ResumeService } from 'src/app/service/resume/resume.service';
import { StudentService } from 'src/app/service/manage/student.service';
import Swal from 'sweetalert2';
import { PdfReader } from 'pdfreader';
import { DataService } from 'src/app/service/haythem/data.service';
import { error } from 'console';
import { ModalComponent } from 'angular-custom-modal';

@Component({
  selector: 'app-resume',
  templateUrl:"resume.component.html",
  styleUrls: ['./resume.component.css']
})
export class ResumeComponent implements OnInit{
  @ViewChild('isAddNoteModal') isAddNoteModal!: ModalComponent;
  userId: any=""
  pdf:any=""
  userResume:any = File
  checkFile:any=false;
  keys:any=[]
  values:any=[];
  applyForm=new FormGroup({});
  finalForm=new FormGroup({
    title:new FormControl(""),
    description:new FormControl("")
  });
  constructor(private fb: FormBuilder,private studentService:StudentService, private resumeService: ResumeService, private dataService:DataService){
  }

  ngOnInit(): void{
    
    this.studentService.getStudents().subscribe(res =>{
      if(res.length>0){
        this.userId = res[0].id;
        console.log(res[0])
        if(res[0].resume)
        this.checkFile=true;
      }
      
    })
  }
  onChangeResume(event:any){
    
    if(event.target.files.length >0){
      const file = event.target.files[0];
      const formData = new FormData();
    formData.append('file', file);  
      this.userResume = file
      // console.log(file)
      this.resumeService.saveResumeToStudent(this.userId,formData).subscribe((response=>{
        this.checkFile =true
        
        this.resumeService.showResume(file.name).subscribe(res =>{
          try {
            this.showMessage("Uploadded Successfully!","success")
          const fileURL = URL.createObjectURL(res);
          this.pdf = fileURL;
            
          } catch (error) {
            this.showMessage("Something went wrong!","error")
          }
           
        })
      }))
    }
    

  }
  viewResumeInfos(){
    this.resumeService.showResumeInfos().subscribe((res:any)=>{
      this.isAddNoteModal.open();
      this.keys= Object.keys(res)
      this.values= Object.values(res)
      for(let key of this.keys){
        this.applyForm.addControl(key,new FormControl(this.values[this.keys.indexOf(key)]))
      }
      // for(let value of this.values){
      //   this.applyForm.addControl(value,new FormControl(value))
      // }
        
      })
      

  }
  submitContent(){
    
    const keysContent = Object.keys(this.applyForm.value);
    const valuesContent = Object.values(this.applyForm.value);
   
    for(let key of keysContent){
      this.finalForm.setValue({
        title:key,
        description:valuesContent[keysContent.indexOf(key)] as string
      })
      this.resumeService.addContentToProfile(this.finalForm.value,this.userId).subscribe(res=>{
        console.log(res)
      })
    }
    
    this.isAddNoteModal.close();
    
    this.showMessage("Content submitted successfully!");
  }
  viewResume(){
    this.studentService.getStudents().subscribe(res=>{
      const file = res[0].resume.name
      
      this.dataService.getPdf(file).subscribe(
        (blob: Blob) => {
          const fileURL = URL.createObjectURL(blob);
          console.log(file)
          window.open(fileURL, '_blank');
        },
        (error) => {
          console.error('Error fetching PDF:', error);
          // Handle error, e.g., display an error message to the user
        }
      );
    })
   
    
  }

  saveProfile(form:any){
    console.log(form)
  }
  showMessage(msg = '', type = 'success') {
    const toast: any = Swal.mixin({
        toast: true,
        position: 'top',
        padding:'auto',
        showConfirmButton: false,
        timer: 2000,
        customClass: { container: 'toast' },
    });
    toast.fire({
        icon: type,
        title: msg,
        padding: '10px 20px',
    });
}
  
}
