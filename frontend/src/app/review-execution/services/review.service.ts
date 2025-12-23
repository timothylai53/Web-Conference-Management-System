import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface ReviewPayload {
  assignmentId: number;
  reviewerId: number;
  overallScore: number;
  recommendation: string;
  commentsToAuthor: string;
  commentsToChair: string;
  roundNumber: number;
}

@Injectable({
  providedIn: 'root'
})
export class ReviewService {

  private API = 'http://localhost:8080/api/reviews';

  constructor(private http: HttpClient) {}

  submitReview(payload: ReviewPayload): Observable<any> {
    return this.http.post(`${this.API}/submit`, payload);
  }

  getReviewByAssignment(assignmentId: number): Observable<any> {
    return this.http.get(`${this.API}?assignmentId=${assignmentId}`);
  }
}
