import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { SessionService } from '../services/session.service';
import { RoomService } from '../services/room.service';

@Component({
  selector: 'app-schedule-view',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './schedule-view.component.html',
  styleUrls: ['./schedule-view.component.css'],
  providers: [SessionService, RoomService]
})
export class ScheduleViewComponent implements OnInit {

  sessions: any[] = [];
  rooms: any[] = [];
  currentWeekStart: Date = new Date();
  weekDays: any[] = [];
  timeSlots: string[] = [];

  constructor(
    private sessionService: SessionService,
    private roomService: RoomService
  ) {
    this.initializeWeek();
    this.generateTimeSlots();
  }

  ngOnInit(): void {
    this.loadRooms();
    this.loadSessions();
  }

  initializeWeek() {
    const today = new Date();
    const dayOfWeek = today.getDay();
    const diff = today.getDate() - dayOfWeek + (dayOfWeek === 0 ? -6 : 1);
    this.currentWeekStart = new Date(today.setDate(diff));
    this.generateWeekDays();
  }

  generateWeekDays() {
    this.weekDays = [];
    const days = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
    for (let i = 0; i < 7; i++) {
      const date = new Date(this.currentWeekStart);
      date.setDate(this.currentWeekStart.getDate() + i);
      this.weekDays.push({
        name: days[i],
        date: date.getDate(),
        fullDate: date
      });
    }
  }

  generateTimeSlots() {
    this.timeSlots = [];
    for (let hour = 8; hour <= 20; hour++) {
      this.timeSlots.push(`${hour.toString().padStart(2, '0')}:00`);
    }
  }

  getWeekRange(): string {
    const endDate = new Date(this.currentWeekStart);
    endDate.setDate(this.currentWeekStart.getDate() + 6);
    const options: Intl.DateTimeFormatOptions = { month: 'short', day: 'numeric', year: 'numeric' };
    return `${this.currentWeekStart.toLocaleDateString('en-US', options)} - ${endDate.toLocaleDateString('en-US', options)}`;
  }

  previousWeek() {
    this.currentWeekStart.setDate(this.currentWeekStart.getDate() - 7);
    this.generateWeekDays();
  }

  nextWeek() {
    this.currentWeekStart.setDate(this.currentWeekStart.getDate() + 7);
    this.generateWeekDays();
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

  getRoomName(roomId: number): string {
    if (!roomId) return 'No Room';
    const room = this.rooms.find(r => r.room_id == roomId);
    return room ? room.name : `Room ${roomId}`;
  }

  getSessionsForDay(date: Date): any[] {
    return this.sessions.filter(s => {
      const sessionDate = new Date(s.start_time);
      return sessionDate.toDateString() === date.toDateString();
    }).sort((a, b) => {
      return new Date(a.start_time).getTime() - new Date(b.start_time).getTime();
    });
  }

  formatTime(dateTime: string): string {
    const date = new Date(dateTime);
    return date.toLocaleTimeString('en-US', { hour: '2-digit', minute: '2-digit', hour12: true });
  }
}
