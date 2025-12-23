import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { AuthService } from './services/auth.service';

@Component({
  selector: 'app-home-redirect',
  standalone: true,
  template: ''
})
export class HomeRedirectComponent implements OnInit {
  
  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    const user = this.authService.getCurrentUser();
    
    if (!user) {
      // Not logged in, go to auth page
      this.router.navigate(['/auth']);
    } else {
      // Logged in, redirect based on role
      const role = user.role;
      
      if (role === 'Admin') {
        this.router.navigate(['/conference']);
      } else if (role === 'Reviewer') {
        this.router.navigate(['/review-execution']);
      } else if (role === 'Chair') {
        this.router.navigate(['/conference']);
      } else {
        // Author or default
        this.router.navigate(['/paper-submission']);
      }
    }
  }
}
