import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class DiscussionService {

  private API = 'http://host.docker.internal:8080/api/discussions';

  constructor(private http: HttpClient) {}

  // ✅ Get discussion messages by paperId
  getMessages(paperId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.API}/${paperId}`);
  }

  // ✅ Send new discussion message
  sendMessage(payload: {
    paperId: number;
    participantId: number;
    message: string;
  }): Observable<any> {
    return this.http.post<any>(this.API, payload);
  }
}
