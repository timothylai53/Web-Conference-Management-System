import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class RoomService {

  private api = 'http://host.docker.internal:8080/api/rooms';

  constructor(private http: HttpClient) {}

  getRooms() {
    return this.http.get<any[]>(this.api);
  }

  addRoom(room: any) {
    return this.http.post(this.api, room);
  }

  updateRoom(id: number, room: any) {
    return this.http.put(`${this.api}/${id}`, room);
  }

  deleteRoom(id: number) {
    return this.http.delete(`${this.api}/${id}`);
  }
}
