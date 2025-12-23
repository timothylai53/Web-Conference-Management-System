import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ReviewAssignment {
  assignmentId: number;
  paperId: number;
  reviewerId: number;
  assignedAt: string;
  dueDate: string;
  status: string;
}

@Injectable({
  providedIn: 'root'
})
export class ReviewerService {

  private baseUrl = 'http://localhost:8080/api/reviewers';

  constructor(private http: HttpClient) {}

  getAssignmentsByReviewer(reviewerId: number): Observable<ReviewAssignment[]> {
    return this.http.get<ReviewAssignment[]>(
      `${this.baseUrl}/${reviewerId}/assignments`
    );
  }
}
