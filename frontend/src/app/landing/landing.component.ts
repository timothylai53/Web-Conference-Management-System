import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-landing',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.css']
})
export class LandingComponent {
  
  constructor(
    private router: Router,
    private authService: AuthService
  ) {}

  navigateToAuth(): void {
    this.router.navigate(['/auth']);
  }

  navigateToConferences(): void {
    this.router.navigate(['/conference']);
  }

  navigateToReviewerApplication(): void {
    this.router.navigate(['/reviewer-registration']);
  }

  isLoggedIn(): boolean {
    return this.authService.getCurrentUser() !== null;
  }
}
