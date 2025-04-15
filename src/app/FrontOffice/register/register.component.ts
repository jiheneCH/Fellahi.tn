import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormControlOptions, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  msgerror:string='';
  isloading:boolean=false;

  
  // for form and it's validation 
registerform:FormGroup=new FormGroup({
  name :new FormControl('',[Validators.required, Validators.minLength(3),Validators.maxLength(20)]),
  email:new FormControl('',[Validators.required,Validators.email]),
  password:new FormControl('',[Validators.required,Validators.pattern(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/)]),
  // rePassword:new FormControl('',[Validators.required]),
   rePassword:new FormControl(''),
  
  phone:new FormControl('',[Validators.required , Validators.pattern(/^01[0125][0-9]{8}$/)])
},{validators:[this.confirmpassword]} as FormControlOptions)  

confirmpassword(group:FormGroup):void{
let password=group.get('password');
let rePassword=group.get('rePassword');

if(rePassword?.value == ''){
  rePassword.setErrors({required:true});
}
else if(rePassword?.value != password?.value){
  rePassword?.setErrors({mismatch:true});
}
}






}
