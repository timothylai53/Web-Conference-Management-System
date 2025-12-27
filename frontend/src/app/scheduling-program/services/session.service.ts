import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  private api = 'http://host.docker.internal:8080/api/sessions';

  constructor(private http: HttpClient) {}

  getSessions() {
    return this.http.get<any[]>(this.api);
  }

  addSession(session: any) {
    return this.http.post(this.api, session);
  }

  updateSession(id: number, session: any) {
    return this.http.put(`${this.api}/${id}`, session);
  }

  deleteSession(id: number) {
    return this.http.delete(`${this.api}/${id}`);
  }
}
