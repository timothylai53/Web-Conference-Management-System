import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Paper {
  paper_id?: number;
  track_id: number;
  conference_id?: number;
  title: string;
  abstractText: string;
  submission_file?: string;
  file_type?: string;
  version?: number;
  plagiarism_score?: number;
  status?: string;
  submitted_by?: number;
  submitted_at?: Date;
  last_updated?: Date;
}

export interface Author {
  author_id?: number;
  user_id?: number;
  paper_id?: number;
  first_name: string;
  last_name: string;
  email: string;
  affiliation?: string;
  is_corresponding_author?: boolean;
  author_order?: number;
}

export interface Keyword {
  keyword_id?: number;
  keyword: string;
}

export interface Track {
  track_id?: number;
  conference_id?: number;
  name: string;
  description?: string;
  chair_id?: number;
}

export interface Review {
  review_id?: number;
  assignment_id?: number;
  reviewer_id?: number;
  overall_score?: number;
  comments_to_author?: string;
  comments_to_chair?: string;
  recommendation?: string;
  round_number?: number;
  due_date?: Date;
  attachment?: string;
  reviewed_at?: Date;
}

export interface Rebuttal {
  rebuttal_id?: number;
  paper_id?: number;
  author_id?: number;
  review_round?: number;
  content?: string;
  submitted_at?: Date;
}

@Injectable({
  providedIn: 'root'
})
export class PaperSubmissionService {
  private baseUrl = 'http://localhost:8080/api';

  constructor(public http: HttpClient) {}

  // Conference APIs
  getConferences(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/conferences`);
  }

  // Paper APIs
  getAllPapers(): Observable<Paper[]> {
    return this.http.get<Paper[]>(`${this.baseUrl}/papers`);
  }

  getPaperById(id: number): Observable<Paper> {
    return this.http.get<Paper>(`${this.baseUrl}/papers/${id}`);
  }

  createPaper(paper: Paper): Observable<Paper> {
    return this.http.post<Paper>(`${this.baseUrl}/papers`, paper);
  }

  updatePaper(id: number, paper: Paper): Observable<Paper> {
    return this.http.put<Paper>(`${this.baseUrl}/papers/${id}`, paper);
  }

  deletePaper(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/papers/${id}`);
  }

  // File Upload APIs
  uploadFile(formData: FormData): Observable<any> {
    return this.http.post(`${this.baseUrl}/files/upload`, formData);
  }

  getFileUrl(filename: string): string {
    return `${this.baseUrl}/files/${filename}`;
  }

  // Author APIs
  getAllAuthors(): Observable<Author[]> {
    return this.http.get<Author[]>(`${this.baseUrl}/author`);
  }

  getAuthorById(id: number): Observable<Author> {
    return this.http.get<Author>(`${this.baseUrl}/author/${id}`);
  }

  createAuthor(author: Author): Observable<Author> {
    return this.http.post<Author>(`${this.baseUrl}/author`, author);
  }

  updateAuthor(id: number, author: Author): Observable<Author> {
    return this.http.put<Author>(`${this.baseUrl}/author/${id}`, author);
  }

  deleteAuthor(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/author/${id}`);
  }

  // Keyword APIs
  getAllKeywords(): Observable<Keyword[]> {
    return this.http.get<Keyword[]>(`${this.baseUrl}/keywords`);
  }

  getKeywordById(id: number): Observable<Keyword> {
    return this.http.get<Keyword>(`${this.baseUrl}/keywords/${id}`);
  }

  createKeyword(keyword: Keyword): Observable<Keyword> {
    return this.http.post<Keyword>(`${this.baseUrl}/keywords`, keyword);
  }

  updateKeyword(id: number, keyword: Keyword): Observable<Keyword> {
    return this.http.put<Keyword>(`${this.baseUrl}/keywords/${id}`, keyword);
  }

  deleteKeyword(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/keywords/${id}`);
  }

  // Track APIs
  getAllTracks(): Observable<Track[]> {
    return this.http.get<Track[]>(`${this.baseUrl}/tracks`);
  }

  getTrackById(id: number): Observable<Track> {
    return this.http.get<Track>(`${this.baseUrl}/tracks/${id}`);
  }

  createTrack(track: Track): Observable<Track> {
    return this.http.post<Track>(`${this.baseUrl}/tracks`, track);
  }

  updateTrack(id: number, track: Track): Observable<Track> {
    return this.http.put<Track>(`${this.baseUrl}/tracks/${id}`, track);
  }

  deleteTrack(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/tracks/${id}`);
  }

  // Review APIs
  getAllReviews(): Observable<Review[]> {
    return this.http.get<Review[]>(`${this.baseUrl}/reviews`);
  }

  getReviewById(id: number): Observable<Review> {
    return this.http.get<Review>(`${this.baseUrl}/reviews/${id}`);
  }

  createReview(review: Review): Observable<Review> {
    return this.http.post<Review>(`${this.baseUrl}/reviews`, review);
  }

  updateReview(id: number, review: Review): Observable<Review> {
    return this.http.put<Review>(`${this.baseUrl}/reviews/${id}`, review);
  }

  deleteReview(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/reviews/${id}`);
  }

  // Rebuttal APIs
  getAllRebuttals(): Observable<Rebuttal[]> {
    return this.http.get<Rebuttal[]>(`${this.baseUrl}/rebuttals`);
  }

  getRebuttalById(id: number): Observable<Rebuttal> {
    return this.http.get<Rebuttal>(`${this.baseUrl}/rebuttals/${id}`);
  }

  createRebuttal(rebuttal: Rebuttal): Observable<Rebuttal> {
    return this.http.post<Rebuttal>(`${this.baseUrl}/rebuttals`, rebuttal);
  }

  updateRebuttal(id: number, rebuttal: Rebuttal): Observable<Rebuttal> {
    return this.http.put<Rebuttal>(`${this.baseUrl}/rebuttals/${id}`, rebuttal);
  }

  deleteRebuttal(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/rebuttals/${id}`);
  }
}
