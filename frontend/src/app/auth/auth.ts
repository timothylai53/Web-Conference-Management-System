import { Component, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';

import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-auth',
  standalone: true,
  imports: [CommonModule, FormsModule, HttpClientModule],
  templateUrl: './auth.html',
  styleUrls: ['./auth.css']
})
export class AuthComponent {

  isRegister: boolean = false;
  errorMessage: string = '';
  showError: boolean = false;

  // ---------- LOGIN MODEL ----------
  loginObj: any = {
    email: '',
    password: ''
  };

  // ---------- REGISTER MODEL ----------
  registerObj: any = {
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    affiliation: '',
    country: ''
  };

  constructor(
    private router: Router,
    private http: HttpClient,
    private cd: ChangeDetectorRef,
    private authService: AuthService
  ) {}

  // ================= LOGIN =================
  onLogin(): void {

    if (!this.loginObj.email || !this.loginObj.password) {
      this.errorMessage = 'Please fill in all fields.';
      this.triggerError();
      return;
    }

    this.showError = false;

    this.http.post('http://host.docker.internal:8080/api/auth/login', this.loginObj)
      .subscribe({
        next: (res: any) => {

          console.log('LOGIN RESPONSE:', res);

          // 🔥 Support BOTH formats:
          // { user: {...} } OR { ...user }
          const user = res?.user ?? res;

          if (user && (user.email || user.user_id)) {

            // Save login state
            this.authService.loginSuccess(user);

            // Role-based redirect
            const role = user.role;

            if (role === 'Admin' || role === 'Chair') {
              this.router.navigate(['/conference']);
            } else if (role === 'Reviewer') {
              this.router.navigate(['/review-execution']);
            } else {
              this.router.navigate(['/paper-submission']); // Author / default
            }

          } else {
            this.errorMessage = 'Invalid response from server';
            this.triggerError();
          }
        },
        error: (err) => {
          if (err.status === 401) {
            this.errorMessage = 'Invalid email or password';
          } else {
            this.errorMessage = 'Server connection failed (Is Backend running?)';
          }
          this.triggerError();
        }
      });
  }

  // ================= REGISTER =================
  onRegister(): void {

    if (!this.registerObj.email || !this.registerObj.password || !this.registerObj.firstName) {
      this.errorMessage = 'Please fill in required fields.';
      this.triggerError();
      return;
    }

    this.showError = false;

    // Frontend -> Backend mapping
    const dbUser = {
      first_name: this.registerObj.firstName,
      last_name: this.registerObj.lastName,
      email: this.registerObj.email,
      password_hash: this.registerObj.password,
      affiliation: this.registerObj.affiliation,
      country: this.registerObj.country,
      category: 'student',
      is_email_verified: false
    };

    this.http.post('http://host.docker.internal:8080/api/auth/register', dbUser)
      .subscribe({
        next: () => {
          alert('Registration Successful! Please Sign In.');

          // Reset form
          this.registerObj = {
            firstName: '',
            lastName: '',
            email: '',
            password: '',
            affiliation: '',
            country: ''
          };

          this.isRegister = false;
          this.cd.detectChanges();
        },
        error: (err) => {
          console.error('Registration Error:', err);
          this.errorMessage = err.error?.message || 'Registration Failed. Email might be taken.';
          this.triggerError();
        }
      });
  }

  // ================= ERROR HANDLER =================
  private triggerError(): void {
    this.showError = true;
    this.cd.detectChanges();

    setTimeout(() => {
      this.showError = false;
      this.cd.detectChanges();
    }, 4000);
  }
}
