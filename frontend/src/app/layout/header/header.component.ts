import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';

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
    this.authService.currentUser$.subscribe(user => {
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
}
