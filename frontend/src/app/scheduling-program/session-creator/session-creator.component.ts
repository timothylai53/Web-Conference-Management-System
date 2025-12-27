import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { SessionService } from '../services/session.service';
import { RoomService } from '../services/room.service';

@Component({
  selector: 'app-session-creator',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './session-creator.component.html',
  styleUrls: ['../schedule-view/schedule-view.component.css'],
  providers: [SessionService, RoomService]
})
export class SessionCreatorComponent implements OnInit {

  session: any = {};
  sessions: any[] = [];
  rooms: any[] = [];

  constructor(
    private sessionService: SessionService,
    private roomService: RoomService
  ) {}

  ngOnInit(): void {
    this.loadRooms();
    this.loadSessions();
  }

  loadRooms() {
    this.roomService.getRooms().subscribe(data => {
      this.rooms = data;
    });
  }

  loadSessions() {
    this.sessionService.getSessions().subscribe(data => {
      this.sessions = data;
    });
  }

  saveSession() {
    // Create a copy of the session with properly formatted timestamps
    const sessionData = {
      ...this.session,
      start_time: this.session.start_time ? new Date(this.session.start_time).toISOString() : null,
      end_time: this.session.end_time ? new Date(this.session.end_time).toISOString() : null
    };

    if (this.session.session_id) {
      this.sessionService.updateSession(this.session.session_id, sessionData).subscribe(() => {
        this.session = {};
        this.loadSessions();
      });
    } else {
      this.sessionService.addSession(sessionData).subscribe(() => {
        this.session = {};
        this.loadSessions();
      });
    }
  }

  editSession(session: any) {
    this.session = { 
      ...session,
      start_time: this.formatDateTimeLocal(session.start_time),
      end_time: this.formatDateTimeLocal(session.end_time)
    };
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  formatDateTimeLocal(dateTime: string): string {
    if (!dateTime) return '';
    const date = new Date(dateTime);
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    return `${year}-${month}-${day}T${hours}:${minutes}`;
  }

  formatDateTime(dateTime: string): string {
    if (!dateTime) return '';
    const date = new Date(dateTime);
    return date.toLocaleString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
      hour12: true
    });
  }

  deleteSession(id: number) {
    this.sessionService.deleteSession(id).subscribe(() => {
      this.loadSessions();
    });
  }

  getRoomName(roomId: number): string {
    if (!roomId) return 'No Room';
    const room = this.rooms.find(r => r.room_id == roomId); // Use == for type coercion
    return room ? room.name : `Room ${roomId}`;
  }
}
