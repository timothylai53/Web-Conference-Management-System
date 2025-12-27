import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-reviewer-registration',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './reviewer-registration.component.html',
  styleUrls: ['./reviewer-registration.component.css']
})
export class ReviewerRegistrationComponent {

  reviewerData = {
    first_name: '',
    last_name: '',
    email: '',
    password_hash: '',
    confirmPassword: '',
    affiliation: '',
    country: '',
    orcid: '',
    expertise_area: '',
    max_papers: 5,
    bio: ''
  };

  showError = false;
  errorMessage = '';
  isSubmitting = false;

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  onSubmit(): void {
    // Validation
    if (!this.reviewerData.first_name || !this.reviewerData.last_name ||
        !this.reviewerData.email || !this.reviewerData.password_hash ||
        !this.reviewerData.expertise_area) {
      this.showErrorMsg('Please fill in all required fields');
      return;
    }

    if (this.reviewerData.password_hash !== this.reviewerData.confirmPassword) {
      this.showErrorMsg('Passwords do not match');
      return;
    }

    if (this.reviewerData.password_hash.length < 6) {
      this.showErrorMsg('Password must be at least 6 characters');
      return;
    }

    this.isSubmitting = true;

    // Prepare data (remove confirmPassword)
    const { confirmPassword, ...applicationData } = this.reviewerData;

    this.http.post('http://host.docker.internal:8080/api/reviewer-applications/submit', applicationData)
      .subscribe({
        next: (response: any) => {
          alert('✅ Application submitted successfully!\n\nYour reviewer application is pending admin approval. You will be notified once it has been reviewed.');
          this.router.navigate(['/']);
        },
        error: (err) => {
          console.error('Application error:', err);
          this.showErrorMsg(err.error?.message || 'Failed to submit application');
          this.isSubmitting = false;
        }
      });
  }

  private showErrorMsg(msg: string): void {
    this.errorMessage = msg;
    this.showError = true;
    setTimeout(() => {
      this.showError = false;
    }, 5000);
  }

  goBack(): void {
    this.router.navigate(['/']);
  }
}
