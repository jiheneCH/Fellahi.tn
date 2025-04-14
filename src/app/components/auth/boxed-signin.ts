import { animate, style, transition, trigger } from '@angular/animations';
import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { Store } from '@ngrx/store';
import { TranslateService } from '@ngx-translate/core';
import { AppService } from 'src/app/service/app.service';
import { AuthenticationService } from 'src/app/service/auth/auth.service';

@Component({
    moduleId: module.id,
    templateUrl: './boxed-signin.html',
    animations: [
        trigger('toggleAnimation', [
            transition(':enter', [style({ opacity: 0, transform: 'scale(0.95)' }), animate('100ms ease-out', style({ opacity: 1, transform: 'scale(1)' }))]),
            transition(':leave', [animate('75ms', style({ opacity: 0, transform: 'scale(0.95)' }))]),
        ]),
    ],
})
export class BoxedSigninComponent {
    store: any;
    id : any;
    password : any;

    constructor(public auh : AuthenticationService, public storeData: Store<any>, public router: Router, private appSetting: AppService) {
        this.initStore();
    }
    async initStore() {
        this.storeData
            .select((d) => d.index)
            .subscribe((d) => {
                this.store = d;
            });
    }

    login() {
        console.log(this.id, this.password);
        
        this.auh.authenticate({email : this.id, password : this.password}).subscribe((res : any) => {
            console.log(res);
            if(res.access_token) {
                localStorage.setItem('token', res.access_token);
                this.router.navigate(['/dashboard']);
            }
        });
    
    }
}
