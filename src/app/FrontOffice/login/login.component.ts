import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  errorMSG:string='';
  isloading:boolean=false;
  #loginhash:string='';

   constructor(private _Router:Router ,private _FormBuilder:FormBuilder){}

  // loginform:FormGroup=new FormGroup({
  //   email:new FormControl('',[Validators.required,Validators.email]),
  //   password: new FormControl('',[Validators.required,Validators.pattern(/^[A-Z][a-z0-9]{6,20}$/)])
  // })
  loginform:FormGroup=this._FormBuilder.group({
    email:['' , [Validators.required,Validators.email ]],
    password:['' ,[ Validators.required,Validators.pattern(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/)]]
  })
   
  
}
