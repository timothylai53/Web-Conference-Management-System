import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './header.component.html',
  styleUrl: './header.component.css'
})
export class HeaderComponent {
  // Temporary switch to test the UI.
  // Set this to 'true' to see what it looks like when logged in.
  isLoggedIn = false;

  logout() {
    this.isLoggedIn = false;
    // Add real logout logic here later (Member 1's job)
  }
}
