import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router, NavigationEnd } from '@angular/router';
import { AuthService, User } from '../../services/auth.service';
import { filter } from 'rxjs/operators';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent implements OnInit {
  isLoggedIn = false;
  isAdmin = false;
  isReviewer = false;
  isChair = false;
  userRole = 'User';
  userName = '';

  constructor(
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    // Subscribe to auth changes
    this.authService.currentUser$.subscribe((user: User | null) => {
      this.isLoggedIn = !!user;
      if (user) {
        this.userName = `${user.first_name} ${user.last_name}`;
        this.userRole = user.role || 'User';
        this.isAdmin = user.role === 'Admin';
        this.isReviewer = user.role === 'Reviewer';
        this.isChair = user.role === 'Chair';
      } else {
        this.userName = '';
        this.userRole = 'User';
        this.isAdmin = false;
        this.isReviewer = false;
        this.isChair = false;
      }
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/auth']);
  }

  navigateHome() {
    // Navigate to appropriate home page based on role
    if (this.isAdmin) {
      this.router.navigate(['/conference']);
    } else if (this.isReviewer) {
      this.router.navigate(['/review-execution']);
    } else if (this.isChair) {
      this.router.navigate(['/conference']);
    } else {
      // Author or default
      this.router.navigate(['/paper-submission']);
    }
  }

  getHomeRoute(): string {
    // Navigate to appropriate home page based on role
    if (this.isAdmin) {
      return '/conference';
    } else if (this.isReviewer) {
      return '/review-execution';
    } else if (this.isChair) {
      return '/conference';
    } else {
      // Author or default
      return '/paper-submission';
    }
  }
}
