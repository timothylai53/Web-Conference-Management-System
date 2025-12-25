import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-review-setup',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './review-setup.component.html',
  styleUrls: ['./review-setup.component.css']
})
export class ReviewSetupComponent implements OnInit {

  reviewers: any[] = [];
  papers: any[] = [];
  assignments: any[] = [];

  selectedPaper: any = null;
  selectedReviewer: any = null;

  constructor(private http: HttpClient) {}

  ngOnInit(): void {
    this.loadReviewers();
    this.loadPapers();
    this.loadAssignments();
  }

  loadReviewers() {
    this.http.get<any[]>('http://localhost:8080/api/reviewers')
      .subscribe({
        next: (data) => {
          this.reviewers = data;
          console.log('Reviewers loaded:', data);
          console.log('First reviewer:', data[0]);
        },
        error: (err) => console.error('Error loading reviewers:', err)
      });
  }

  loadPapers() {
    this.http.get<any[]>('http://localhost:8080/api/papers')
      .subscribe({
        next: (data) => {
          // Filter only submitted papers
          this.papers = data.filter(p => p.status === 'Submitted' || p.status === 'Under Review');
          console.log('Papers loaded:', this.papers);
          console.log('First paper:', this.papers[0]);
        },
        error: (err) => console.error('Error loading papers:', err)
      });
  }

  loadAssignments() {
    this.http.get<any[]>('http://localhost:8080/api/review-assignments')
      .subscribe({
        next: (data) => {
          this.assignments = data;
          console.log('Assignments loaded:', data);
          console.log('First assignment:', data[0]);
        },
        error: (err) => console.error('Error loading assignments:', err)
      });
  }

  selectPaperForAssignment(paper: any) {
    this.selectedPaper = paper;
    this.selectedReviewer = null;
  }

  cancelAssignment() {
    this.selectedPaper = null;
    this.selectedReviewer = null;
  }

  getAssignmentCount(paperId: number): number {
    return this.assignments.filter(a => a.paper_id === paperId).length;
  }

  getReviewerAssignmentCount(reviewerId: number): number {
    return this.assignments.filter(a => a.reviewer_id === reviewerId).length;
  }

  isReviewerOverloaded(reviewer: any): boolean {
    const count = this.getReviewerAssignmentCount(reviewer.reviewer_id);
    return count >= reviewer.max_papers;
  }

  getPaperTitle(paperId: number): string {
    const paper = this.papers.find(p => p.paper_id === paperId);
    return paper ? paper.title : `Paper #${paperId}`;
  }

  getReviewerName(reviewerId: number): string {
    const reviewer = this.reviewers.find(r => r.reviewer_id === reviewerId);
    if (reviewer && reviewer.user) {
      return `${reviewer.user.first_name} ${reviewer.user.last_name}`;
    }
    return `Reviewer #${reviewerId}`;
  }

  isAlreadyAssigned(paperId: number, reviewerId: number): boolean {
    // Only check for active (Pending or In Progress) assignments
    // Allow reassignment if previous assignment was Completed (paper was reviewed and potentially rejected/resubmitted)
    return this.assignments.some(
      a => a.paper_id === paperId && 
           a.reviewer_id === reviewerId && 
           (a.status === 'Pending' || a.status === 'In Progress')
    );
  }

  assignReviewerToPaper(paper: any) {
    if (!paper.selectedReviewerId) {
      alert('Please select a reviewer');
      return;
    }

    const selectedReviewer = this.reviewers.find(r => r.reviewer_id === paper.selectedReviewerId);
    
    if (!selectedReviewer) {
      alert('Invalid reviewer selection');
      return;
    }

    if (this.isReviewerOverloaded(selectedReviewer)) {
      alert('This reviewer has reached their maximum paper capacity');
      return;
    }

    if (this.isAlreadyAssigned(paper.paper_id, paper.selectedReviewerId)) {
      alert('This reviewer is already assigned to this paper');
      return;
    }

    const payload = {
      paper_id: paper.paper_id,
      reviewer_id: paper.selectedReviewerId,
      assigned_at: new Date().toISOString(),
      due_date: new Date(Date.now() + 14 * 24 * 60 * 60 * 1000).toISOString(), // 14 days from now
      status: 'Pending'
    };

    this.http.post('http://localhost:8080/api/review-assignments', payload)
      .subscribe({
        next: () => {
          const reviewerName = selectedReviewer.user?.first_name || 'Reviewer';
          alert(`Successfully assigned "${paper.title}" to ${reviewerName}`);
          
          // Update paper status to Under Review
          this.updatePaperStatus(paper.paper_id, 'Under Review');
          
          // Reload assignments
          this.loadAssignments();
          
          // Clear selection
          paper.selectedReviewerId = null;
        },
        error: (err) => {
          console.error('Assignment failed:', err);
          alert('Failed to assign reviewer. Please try again.');
        }
      });
  }

  assignReviewer() {
    if (!this.selectedPaper || !this.selectedReviewer) {
      alert('Please select both a paper and a reviewer');
      return;
    }

    if (this.isReviewerOverloaded(this.selectedReviewer)) {
      alert('This reviewer has reached their maximum paper capacity');
      return;
    }

    // Check if already assigned (only active assignments)
    const existingAssignment = this.assignments.find(
      a => a.paper_id === this.selectedPaper.paper_id && 
           a.reviewer_id === this.selectedReviewer.reviewer_id &&
           (a.status === 'Pending' || a.status === 'In Progress')
    );

    if (existingAssignment) {
      alert('This reviewer is already assigned to this paper');
      return;
    }

    const payload = {
      paper_id: this.selectedPaper.paper_id,
      reviewer_id: this.selectedReviewer.reviewer_id,
      assigned_at: new Date(),
      due_date: new Date(Date.now() + 14 * 24 * 60 * 60 * 1000), // 14 days from now
      status: 'Pending'
    };

    this.http.post('http://localhost:8080/api/review-assignments', payload)
      .subscribe({
        next: () => {
          alert(`Successfully assigned "${this.selectedPaper.title}" to ${this.selectedReviewer.user?.first_name || 'Reviewer'}`);
          
          // Update paper status to Under Review
          this.updatePaperStatus(this.selectedPaper.paper_id, 'Under Review');
          
          // Reload assignments
          this.loadAssignments();
          
          // Clear selection
          this.selectedPaper = null;
          this.selectedReviewer = null;
        },
        error: (err) => {
          console.error('Assignment failed:', err);
          alert('Failed to assign reviewer. Please try again.');
        }
      });
  }

  updatePaperStatus(paperId: number, status: string) {
    const paper = this.papers.find(p => p.paper_id === paperId);
    if (paper) {
      paper.status = status;
      this.http.put(`http://localhost:8080/api/papers/${paperId}`, { ...paper, status })
        .subscribe({
          next: () => console.log('Paper status updated'),
          error: (err) => console.error('Error updating paper status:', err)
        });
    }
  }

  removeAssignment(assignmentId: number) {
    if (!confirm('Are you sure you want to remove this assignment?')) {
      return;
    }

    this.http.delete(`http://localhost:8080/api/review-assignments/${assignmentId}`)
      .subscribe({
        next: () => {
          alert('Assignment removed successfully');
          this.loadAssignments();
        },
        error: (err) => {
          console.error('Error removing assignment:', err);
          alert('Failed to remove assignment');
        }
      });
  }
}
