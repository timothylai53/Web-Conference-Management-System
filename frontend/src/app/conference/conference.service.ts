import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Conference } from './conference.model';

@Injectable({
  providedIn: 'root'
})
export class ConferenceService {
  private apiUrl = 'http://host.docker.internal:8080/api/conferences';

  constructor(private http: HttpClient) { }

  // Get all conferences
  getAll(): Observable<Conference[]> {
    return this.http.get<Conference[]>(this.apiUrl);
  }

  // Get one conference by ID
  getById(id: number): Observable<Conference> {
    return this.http.get<Conference>(`${this.apiUrl}/${id}`);
  }

  // Create a new conference
  create(conference: Conference): Observable<Conference> {
    return this.http.post<Conference>(this.apiUrl, conference);
  }

  // Update a conference
  update(id: number, conference: Conference): Observable<Conference> {
    return this.http.put<Conference>(`${this.apiUrl}/${id}`, conference);
  }

  // Delete a conference
  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
