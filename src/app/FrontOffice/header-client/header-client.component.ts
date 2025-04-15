import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/service/auth/auth.service';


@Component({
  selector: 'app-header-client',
  templateUrl: './header-client.component.html',
  styleUrls: ['./header-client.component.css']
})
export class HeaderClientComponent {
  constructor(
    private authService: AuthenticationService,
    private router: Router
  ) {}
  isScrolled = false;
  logout(): void {
    // Clear token / session logic here
    this.authService.logout(); // if you have an AuthService
    this.router.navigate(['/login']);
  }
  

}
