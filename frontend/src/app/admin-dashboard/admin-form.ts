import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-admin-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './admin-form.html',
  styleUrls: ['./admin-dashboard.css']
})
export class AdminFormComponent {
  userName: string = '';
  email: string = '';

  submitForm() {
    // Logic to handle form submission, like API calls
    console.log('Form submitted with:', this.userName, this.email);
  }

  resetForm() {
    this.userName = '';
    this.email = '';
  }
}
