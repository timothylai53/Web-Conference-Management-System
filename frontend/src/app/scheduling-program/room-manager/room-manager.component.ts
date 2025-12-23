import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';

import { RoomService } from '../services/room.service';

@Component({
  selector: 'app-room-manager',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './room-manager.component.html',
  styleUrls: ['../schedule-view/schedule-view.component.css'],
  providers: [RoomService]
})
export class RoomManagerComponent implements OnInit {

  rooms: any[] = [];
  room: any = {};

  constructor(private roomService: RoomService) {}

  ngOnInit(): void {
    this.loadRooms();
  }

  loadRooms() {
    this.roomService.getRooms().subscribe(data => {
      this.rooms = data;
    });
  }

  saveRoom() {
    if (this.room.room_id) {
      this.roomService.updateRoom(this.room.room_id, this.room).subscribe(() => {
        this.room = {};
        this.loadRooms();
      });
    } else {
      this.roomService.addRoom(this.room).subscribe(() => {
        this.room = {};
        this.loadRooms();
      });
    }
  }

  editRoom(room: any) {
    this.room = { ...room };
    window.scrollTo({ top: 0, behavior: 'smooth' });
  }

  deleteRoom(id: number) {
    this.roomService.deleteRoom(id).subscribe(() => {
      this.loadRooms();
    });
  }
}
