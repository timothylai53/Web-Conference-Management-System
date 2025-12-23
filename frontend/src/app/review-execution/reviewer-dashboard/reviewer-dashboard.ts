import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { HttpClient } from '@angular/common/http';

import { MatCardModule } from '@angular/material/card';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';

import { ReviewerService, ReviewAssignment } from '../reviewer.service';
import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-reviewer-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    RouterModule,
    MatCardModule,
    MatTableModule,
    MatButtonModule
  ],
  templateUrl: './reviewer-dashboard.html',
  styleUrls: ['./reviewer-dashboard.css'],
  providers: [ReviewerService]
})
export class ReviewerDashboardComponent implements OnInit {

  papers: ReviewAssignment[] = [];
  displayedColumns = ['paperTitle', 'status', 'dueDate', 'action'];
  currentUserId: number = 0;

  constructor(
    private reviewerService: ReviewerService,
    private authService: AuthService,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    // Get current user from auth service
    this.authService.currentUser$.subscribe(user => {
      if (user && user.user_id) {
        this.currentUserId = user.user_id;
        // Get reviewer profile to find reviewer_id
        this.getReviewerIdAndLoadAssignments();
      }
    });
  }

  getReviewerIdAndLoadAssignments(): void {
    // Find reviewer record by user_id
    this.http.get<any[]>('http://localhost:8080/api/reviewers').subscribe({
      next: (reviewers) => {
        const reviewer = reviewers.find(r => r.user_id === this.currentUserId);
        if (reviewer && reviewer.reviewer_id) {
          this.loadAssignments(reviewer.reviewer_id);
        } else {
          console.error('No reviewer profile found for user:', this.currentUserId);
        }
      },
      error: (err) => console.error('Error fetching reviewers:', err)
    });
  }

  loadAssignments(reviewerId: number): void {
    this.reviewerService.getAssignmentsByReviewer(reviewerId)
      .subscribe({
        next: (data) => {
          this.papers = data;
          console.log('Assignments loaded:', data);
        },
        error: (err) => {
          console.error('Failed to load assignments', err);
        }
      });
  }

  reviewPaper(assignmentId: number): void {
    this.router.navigate(['/review-execution/grading', assignmentId]);
  }
}
