import { Component, OnInit, ViewChild } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { ModalComponent } from 'angular-custom-modal';
import { ApplicationService } from 'src/app/service/application/application.service';
import { DataService } from 'src/app/service/haythem/data.service';
import { InternshipService } from 'src/app/service/internshipStudent/internship.service';
import { ResumeService } from 'src/app/service/resume/resume.service';
import { StudentFilesService } from 'src/app/service/studentFiles/student-files.service';
import Swal from 'sweetalert2';
import { jsPDF } from "jspdf";
@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit{
  @ViewChild('isAddDetailModal') isAddDetailModal!: ModalComponent;
  @ViewChild('isAddResumeDetailsModal') isAddResumeDetailsModal!: ModalComponent;
  student: any={
    firstName:"",
    lastName:"",
    phoneNumber:""
  }
  templateIndex:any =0;
  fileURL:any = "";
  pdf:any = "";
  contents:any=[];
  documents:any={};
  docKeys:any=[];
  docValues:any=[];
  files:any=[];
  markedInternships:any=[];
  applications:any=[];
  detailForm=new FormGroup({
    title:new FormControl(""),
    description:new FormControl("")
  });
  additionalForm=new FormGroup({
    adress:new FormControl(""),
    github:new FormControl(""),
    linkedIn:new FormControl("")
  })
  
  constructor(private fb: FormBuilder, private dataService:DataService, private internshipService:InternshipService, private applicationService:ApplicationService, private resumeService:ResumeService, private studentFilesService:StudentFilesService){
  }
  
  ngOnInit(): void{
    this.dataService.getStudents().subscribe(res=>{
      if(res[0]){
        
        this.student = res[0];
        console.log(this.student.studentFiles.archive);
        this.files = this.student.studentFiles.archive;
        this.resumeService.getContent(this.student.id).subscribe(res1=>{
          this.contents = res1;
        })
        this.internshipService.getFavInternships(this.student.id).subscribe(res1=>{
          console.log(res1);
          this.markedInternships = res1;
        })
        this.applicationService.getApplicationFromStudent(this.student.id).subscribe(res1=>{
          this.applications = res1;
        })
        this.studentFilesService.getStudentFiles(this.student.id).subscribe(res1=>{
          this.documents = res1;
          this.docKeys = Object.keys(this.documents);
          this.docValues = Object.values(this.documents);
        })
      }
    })
    
    
  }
  setTemplate(number:Number){
    console.log(number)
    this.templateIndex=number;
  }
  viewResumeTemplate(){
    this.isAddResumeDetailsModal.open()
  }
  addOtherDetail(){
    this.isAddDetailModal.open();

  }
  saveDetail(form:any){
    console.log(form.value)
    this.resumeService.addContentToProfile(form.value,this.student.id).subscribe(res=>{
      this.ngOnInit()
      this.isAddDetailModal.close();
      this.showMessage(`${form.value.title} added successfully`)
    })
  }
  createNewResume(additionalForm:any){
    this.pdf = new jsPDF();
    let space=30
      let name=`${this.student.firstName} ${this.student.lastName}`
      let info=`${this.student.email} | ${this.student.phoneNumber} | ${additionalForm.value.adress} `
      let textLines=""
    if(this.templateIndex==1){
      this.pdf.text(name,15,10);
      this.pdf.setFontSize(10);
      this.pdf.text(info,15,20);
      if(additionalForm.value.linkedIn)
        this.pdf.textWithLink('| LinkedIn ', 15+this.pdf.getTextDimensions(info).w+2, 20, { url: `${additionalForm.value.linkedIn}` });
      if(additionalForm.value.github)
      this.pdf.textWithLink(' | Github', 15+this.pdf.getTextDimensions(info).w+16, 20, { url: `${additionalForm.value.github}` });
      for(let content of this.contents){
        this.pdf.setFontSize(13);
        space+=10;
        this.pdf.setFont('Times','bold')
        this.pdf.text(content.title,10,space);
        this.pdf.line(10,space+2,200,space+2);
        console.log(this.pdf.getFontList())
        textLines = this.pdf.setFont('Times','normal')
        .setFontSize(10)
        .splitTextToSize(content.description,190);
        space+=10;
        this.pdf.text(10,space,textLines);
        space+=this.pdf.getTextDimensions(textLines).h;
      }
    
      
    }else if(this.templateIndex==2){
      let verif=true;
      this.pdf.setFontSize(20);
      this.pdf.text(name,100-name.length,20);
      this.pdf.line(10,25,200,25);
      let newSpace=0;
      if(additionalForm.value.linkedIn || additionalForm.value.github || additionalForm.value.adress){
        this.pdf.setFontSize(13);
        this.pdf.setFont('Times','bold')
        space+=10;
        this.pdf.text("CONTACT",10,space);
        this.pdf.setFont('Times','normal')
          .setFontSize(10)
        space+=10
        this.pdf.text(`Phone Number: ${this.student.phoneNumber}`,10,space);
        space+=5
        this.pdf.textWithLink('LinkedIn ', 10, space, { url: `${additionalForm.value.linkedIn}` });
        space+=5
        this.pdf.textWithLink('Github ', 10, space, { url: `${additionalForm.value.github}` });
        space+=5
        this.pdf.text(`Adress: ${additionalForm.value.adress}`,10,space);
        space+=5
        this.pdf.line(10,space,100,space);
        

      }
      for(let content of this.contents){
        this.pdf.setFontSize(13);
        space+=10;
        this.pdf.setFont('Times','bold')
        if(space<250 && verif){
          this.pdf.text(content.title,10,space);
          console.log(this.pdf.getFontList())
          textLines = this.pdf.setFont('Times','normal')
          .setFontSize(10)
          .splitTextToSize(content.description,90);
          space+=10;
          this.pdf.text(10,space,textLines);
          
          space+=this.pdf.getTextDimensions(textLines).h;
          this.pdf.line(10,space,100,space);
        }else{
          verif=false
          space = 40
          newSpace +=40
          this.pdf.text(content.title,110,newSpace);
          console.log(this.pdf.getFontList())
          textLines = this.pdf.setFont('Times','normal')
          .setFontSize(10)
          .splitTextToSize(content.description,110);
          newSpace+=10;
          this.pdf.text(110,newSpace,textLines);
          
          newSpace+=this.pdf.getTextDimensions(textLines).h;
          this.pdf.line(110,newSpace,200,newSpace);
        }
        
        
        console.log(space)
      }

    }
    this.pdf.save(`CV_`+`${this.student.firstName}_`+`${this.student.lastName}.pdf`)
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
