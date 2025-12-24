import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { AdminService } from './admin.service';
import { ActivityLog } from './admin.model';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.html',
  styleUrls: ['./admin-dashboard.css'],
  standalone: true,
  imports: [CommonModule, RouterModule, HttpClientModule],
  providers: [AdminService]
})
export class AdminDashboard implements OnInit {
  activityLogs: ActivityLog[] = [];
  notifications: any[] = [];
  emailLogs: any[] = [];
  pendingApplications: any[] = [];
  isLoading = true;

  constructor(
    private adminService: AdminService,
    private http: HttpClient,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadActivityLogs();
    this.loadPendingApplications();
  }

  loadActivityLogs(): void {
    this.adminService.getActivityLogs().subscribe({
      next: (data) => {
        this.activityLogs = data;
        this.isLoading = false;
      },
      error: (error) => {
        console.error('Error fetching activity logs:', error);
        this.isLoading = false;
      },
    });
  }

  loadPendingApplications(): void {
    this.http.get<any[]>('http://localhost:8080/api/reviewer-applications/pending')
      .subscribe({
        next: (data) => {
          this.pendingApplications = data;
        },
        error: (err) => console.error('Error loading applications:', err)
      });
  }

  approveApplication(appId: number): void {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser || !currentUser.user_id) {
      alert('You must be logged in as admin');
      return;
    }

    if (!confirm('Approve this reviewer application?')) return;

    this.http.post(
      `http://localhost:8080/api/reviewer-applications/approve/${appId}`,
      null,
      { params: { adminUserId: currentUser.user_id.toString() } }
    ).subscribe({
      next: (response: any) => {
        alert(response.message);
        this.loadPendingApplications();
      },
      error: (err) => {
        alert('Error: ' + (err.error?.message || 'Failed to approve'));
      }
    });
  }

  rejectApplication(appId: number): void {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser || !currentUser.user_id) {
      alert('You must be logged in as admin');
      return;
    }

    const reason = prompt('Enter rejection reason:');
    if (!reason) return;

    this.http.post(
      `http://localhost:8080/api/reviewer-applications/reject/${appId}`,
      { reason },
      { params: { adminUserId: currentUser.user_id.toString() } }
    ).subscribe({
      next: (response: any) => {
        alert(response.message);
        this.loadPendingApplications();
      },
      error: (err) => {
        alert('Error: ' + (err.error?.message || 'Failed to reject'));
      }
    });
  }
}
