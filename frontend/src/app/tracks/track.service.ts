import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Track } from './track.model';

@Injectable({
  providedIn: 'root'
})
export class TrackService {
  private apiUrl = 'http://localhost:8080/api/tracks';

  constructor(private http: HttpClient) { }

  getAll(): Observable<Track[]> {
    return this.http.get<Track[]>(this.apiUrl);
  }

  getById(id: number): Observable<Track> {
    return this.http.get<Track>(`${this.apiUrl}/${id}`);
  }

  create(track: Track): Observable<Track> {
    return this.http.post<Track>(this.apiUrl, track);
  }

  update(id: number, track: Track): Observable<Track> {
    return this.http.put<Track>(`${this.apiUrl}/${id}`, track);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
