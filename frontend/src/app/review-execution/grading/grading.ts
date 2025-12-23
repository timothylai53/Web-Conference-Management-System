import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';

import { MatCardModule } from '@angular/material/card';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';

@Component({
  standalone: true,
  selector: 'app-grading',
  imports: [
    CommonModule,
    FormsModule,
    RouterModule,
    MatCardModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule,
    MatFormFieldModule
  ],
  templateUrl: './grading.html',
  styleUrls: ['./grading.css']
})
export class GradingComponent implements OnInit {

  // =====================
  // ROUTE PARAM
  // =====================
  assignmentId!: number;

  // =====================
  // ASSIGNMENT & PAPER DATA
  // =====================
  assignment: any = null;
  paper: any = null;
  loading = false;

  // =====================
  // SCORING
  // =====================
  scoreCriteria = [
    { name: 'Originality', score: 0 },
    { name: 'Technical Quality', score: 0 },
    { name: 'Clarity', score: 0 },
    { name: 'Relevance', score: 0 }
  ];

  overallScore = 0;
  confidence?: number;

  // =====================
  // COMMENTS
  // =====================
  commentsToAuthor = '';
  commentsToChair = '';
  recommendation = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient
  ) {}

  ngOnInit(): void {
    this.assignmentId = Number(
      this.route.snapshot.paramMap.get('assignmentId')
    );

    this.loadAssignmentDetails();
  }

  // =====================
  // AUTO-CALCULATE OVERALL SCORE
  // =====================
  calculateOverallScore(): void {
    // Sum all criteria scores (each 0-10) and multiply by 2.5 to get 0-100 scale
    const total = this.scoreCriteria.reduce((sum, c) => sum + (c.score || 0), 0);
    this.overallScore = Math.round(total * 2.5); // 4 criteria × 10 points × 2.5 = 100
  }

  onScoreChange(): void {
    this.calculateOverallScore();
  }

  // =====================
  // FILE DOWNLOAD
  // =====================
  getFileDownloadUrl(): string {
    if (this.paper && this.paper.submission_file) {
      return `http://localhost:8080/api/files/${this.paper.submission_file}`;
    }
    return '';
  }

  // =====================
  // LOAD ASSIGNMENT & PAPER
  // =====================
  loadAssignmentDetails(): void {
    this.loading = true;
    
    // Get assignment details
    this.http.get<any>(`http://localhost:8080/api/review-assignments/${this.assignmentId}`)
      .subscribe({
        next: (assignment) => {
          this.assignment = assignment;
          
          // Load paper details
          if (assignment.paper_id) {
            this.http.get<any>(`http://localhost:8080/api/papers/${assignment.paper_id}`)
              .subscribe({
                next: (paper) => {
                  this.paper = paper;
                  this.loading = false;
                },
                error: (err) => {
                  console.error('Error loading paper:', err);
                  this.loading = false;
                }
              });
          }
        },
        error: (err) => {
          console.error('Error loading assignment:', err);
          this.loading = false;
          alert('Failed to load assignment details');
        }
      });
  }

  // =====================
  // SUBMIT REVIEW
  // =====================
  // =====================
  submitReview(): void {
    if (!this.recommendation) {
      alert('Please select a recommendation before submitting');
      return;
    }

    const payload = {
      assignment_id: this.assignmentId,
      reviewer_id: this.assignment?.reviewer_id,
      overall_score: this.overallScore,
      comments_to_author: this.commentsToAuthor,
      comments_to_chair: this.commentsToChair,
      recommendation: this.recommendation,
      round_number: 1,
      reviewed_at: new Date()
    };

    this.http.post('http://localhost:8080/api/reviews', payload)
      .subscribe({
        next: (response) => {
          console.log('Review submitted:', response);
          alert('Review submitted successfully!');
          this.router.navigate(['/review-execution/dashboard']);
        },
        error: (err) => {
          console.error('Error submitting review:', err);
          alert('Failed to submit review. Please try again.');
        }
      });
  }
}

