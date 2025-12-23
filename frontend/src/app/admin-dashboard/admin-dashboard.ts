import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { AdminService } from './admin.service';
import { ActivityLog } from './admin.model';

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.html',
  styleUrls: ['./admin-dashboard.css'],
  standalone: true,
  imports: [CommonModule, RouterModule],
  providers: [AdminService]
})
export class AdminDashboard implements OnInit {
  activityLogs: ActivityLog[] = [];
  notifications: any[] = [];
  emailLogs: any[] = [];
  isLoading = true;

  constructor(private adminService: AdminService) {}

  ngOnInit(): void {
    this.loadActivityLogs();
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
}
