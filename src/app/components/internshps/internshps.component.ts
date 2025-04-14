import { Component, OnInit, PipeTransform, ViewChild } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ModalComponent } from 'angular-custom-modal';
import Swal from 'sweetalert2';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { InternshipService } from 'src/app/service/internshipStudent/internship.service';
import { Internship } from './internship';
import { DataService } from 'src/app/service/haythem/data.service';
import { ResumeService } from 'src/app/service/resume/resume.service';
import { StudentFilesService } from 'src/app/service/studentFiles/student-files.service';
import { ApplicationService } from 'src/app/service/application/application.service';

@Component({
  selector: 'app-internshps',
  templateUrl: './internshps.component.html',
  styleUrls: ['./internshps.component.css']
})
export class InternshpsComponent implements OnInit{



  constructor(private fb: FormBuilder,private internshipService:InternshipService, private studentFilesService: StudentFilesService, private resumeService:ResumeService, private dataService: DataService,private applicationService:ApplicationService) {}
  defaultParams = {
      id: null,
      title: '',
      description: '',
      tag: '',
      user: '',
      thumb: '',
  };
  @ViewChild('isAddNoteModal') isAddNoteModal!: ModalComponent;
  @ViewChild('isViewInternshipModal') isViewInternshipModal!: ModalComponent;
  @ViewChild('isViewNoteModal') isViewNoteModal!: ModalComponent;
  params!: FormGroup;
  isShowNoteMenu = false;
  internships: any=[];
  searchText: any="";
  filterdInternshipsList: any = '';
  student: any = {};
  students: any = [];
  selectedTab: any = 'all';
  deletedNote: any = null;
  internshipsMarked:any = [];
  applications:any=[];
  application:any = {
    internshipOffer: null,
    student: null,
    applicationDate: Date,
    interviewDate: Date,
    studentFiles: null
  };
  selectedInternship: any = {
      id: null,
      title: '',
      description: '',
      isPaid: '',
      type: '',
      style: '',
      company:''
  };
userCoverLetter:any ="";
userResume:any="";
pdf:any="";
percentageMatch:any=[];
matchDescription:any=[];
coverLetterUploaded:any=false;
resumeUploaded:any=false;
checkFile=false;
  ngOnInit() {
        
        this.filterdInternshipsList = this.internships;
        // console.log(this.filterdInternshipsList)
        
        this.dataService.getStudents().subscribe((res:any) =>{
          let internship = {} as Internship;
          if(res[0]) {
            this.student=res[0];
            console.log(this.student.studentFiles.coverLetter)
            if(this.student.studentFiles.coverLetter!=null){
              this.coverLetterUploaded=true;
            }
            console.log(this.student)
            if(this.student.studentFiles.resume!=null){
              this.resumeUploaded=true;
            }
            this.internshipService.getAllInternshps().subscribe((res:any) =>{
              this.internshipService.getMatch().subscribe(res=>{
                for(let element of res){
                  this.percentageMatch.push(element.jobDescriptionMatch)
                  this.matchDescription.push(element.candidateSummary)
                }
              })
              this.internshipService.getFavInternships(this.student.id).subscribe((resFav:any) =>{
                // if(resFav.length!=0) {
                //   for(let i=0;i<resFav.length;i++){
                //     internship=resFav[i];
                //     internship.isFav=true
                //     this.internships.push(internship);
                // }
                // }
                
                  for(let i=0;i<res.length;i++){
                    internship=res[i];
                    for(let j=0;j<resFav.length;j++){
                      if(resFav[j].id==res[i].id) res[i].isFav = true;
                    }
                    this.internships.push(internship);
                  
                }
                
              })                
              
            })
          }
            
      })
      
  }

  initForm() {
      this.params = this.fb.group({
          id: [0],
          title: ['', Validators.required],
          description: [''],
          tag: [''],
          user: [''],
          thumb: [''],
      });
  }
  deleteApp(appId:Number){
    this.internshipService.deleteApplication(appId).subscribe(res=>{
      this.applicationService.getApplicationFromStudent(this.student.id).subscribe(res =>{
        this.applications=res;
        console.log(this.applications)
      })
    });
    
  }
  returnStudents(selectedInternship:any){
    return selectedInternship.students
  }
  searchInternships() {
      if (this.selectedTab != 'fav') {
          if(this.selectedTab == "app"){
            this.applicationService.getApplicationFromStudent(this.student.id).subscribe(res =>{
              this.applications=res;
              console.log(this.applications)
            })
          }
          else if (this.selectedTab != 'all') {
            this.internships = this.internships.filter((d: { type: any, style:any }) => d.type === this.selectedTab || d.style === this.selectedTab);
          } 
        }
        else {
        this.internshipService.getFavInternships(this.student.id).subscribe((res:any) =>{
            this.internships = res;
            this.internships.map((d:any) => d.isFav = true);
        });
      }
    
    
  }

  onChangeResume(event:any){
    
    if(event.target.files.length >0){
      const file = event.target.files[0];
      const formData = new FormData();
    formData.append('file', file);  
      this.userResume = file
      // console.log(file)
      this.resumeService.saveResumeToStudent(this.student.id,formData).subscribe((response=>{
        this.resumeUploaded =true
      }))
    }
    

  }

  onChangeCoverLetter(event:any){
    
    if(event.target.files.length >0){
      const file = event.target.files[0];
      const formData = new FormData();
      formData.append('file', file);  
      this.userCoverLetter = file
      // console.log(file)
      this.studentFilesService.addCoverLetterToStudent(this.student.id,formData).subscribe(res=>{
        this.coverLetterUploaded=true;
      })
    }
    

  }
  onSubmitApplication(){
    this.showMessageWithConfirmation("Are you sure you want to proceed?","question");
  }

  tabChanged(type: string) {
      this.selectedTab = type;
      this.internships = this.filterdInternshipsList;
      this.searchInternships();
      this.isShowNoteMenu = false;
  }

  setFav(internship: Internship) {
    
    this.internshipService.setFavInternships(internship.id,this.student.id).subscribe((res:any) =>{
        internship.isFav =res;
        if(internship.isFav) this.showMessage('Internship Added to favorites!','success');
        else this.showMessage('Internship removed from favorites!','error');
        this.searchInternships();
    });
    
  }

  setTag(internship: any, name: string = '') {
    //   this.internships = this.internships.filter((d: {type:any }) => d.type ===internship.type);
    //   this.filterdInternshipsList=
    //   console.log(item.type)
    //   this.searchInternships();
  }

//   deleteNoteConfirm(note: any) {
//       setTimeout(() => {
//           this.deletedNote = note;
//           this.isDeleteNoteModal.open();
//       });
//   }

  viewNote(note: any) {
      setTimeout(() => {
          this.selectedInternship = note;
          this.dataService.getStudentsByInternship(note.id).subscribe((res:any) =>{
            this.students=res;
            this.isViewNoteModal.open();
            
        });
          
      });
  }

  editNote(note: any = null) {
      this.isShowNoteMenu = !this.isShowNoteMenu;
      // this.isAddNoteModal.open();
      // this.initForm();
      // if (note) {
      //     this.params.setValue({
      //         id: note.id,
      //         title: note.title,
      //         description: note.description,
      //         user: note.user,
      //         thumb: note.thumb
      //     });
      // }
      console.log(note)
  }

  deleteNote() {
    //   this.notesList = this.notesList.filter((d: { id: any }) => d.id != this.deletedNote.id);
    //   this.searchInternships();
    //   this.showMessage('Note has been deleted successfully.');
    //   this.isDeleteNoteModal.close();
  }

  showMessage(msg = '', type = 'success') {
      const toast: any = Swal.mixin({
          toast: true,
          position: 'top',
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
  showMessageWithConfirmation(msg = '', type = 'success') {
    const toast: any = Swal.mixin({
        toast: true,
        position: 'center',
        showConfirmButton: true,
        showCancelButton:true,
        customClass: { container: 'toast' },
    });
    toast.fire({
        icon: type,
        title: msg,
        padding: '10px 20px',
    }).then((result:any)=>{
      if(result.isConfirmed) {
        // this.application.student = this.student;
        // this.application.applicationDate = new Date("yyyy-MM-dd HH:mm");
        // this.application.internshipOffer = this.selectedInternship;
        // this.application.studentFiles = this.student.studentFiles;
        this.applicationService.addApplicationToStudent(this.selectedInternship.id,this.student.id).subscribe(res =>{
          console.log(res)
          // this.applications.push(res);
        },err=>{console.log(err)})
      }
      else{

      }
    });
}

}
