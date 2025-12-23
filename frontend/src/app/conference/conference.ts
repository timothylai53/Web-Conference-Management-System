import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { ConferenceService } from './conference.service';
import { Conference } from './conference.model';

@Component({
  selector: 'app-conference',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './conference.html',
  styleUrl: './conference.css',
  providers: [ConferenceService]
})
export class ConferenceComponent implements OnInit {
  conferences: Conference[] = [];

  // 1. Variable to track which conference to show in the modal
  selectedConference: Conference | null = null;

  constructor(
    private conferenceService: ConferenceService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadConferences();
  }

  loadConferences(): void {
    this.conferenceService.getAll().subscribe({
      next: (data) => {
        this.conferences = data;
        this.cdr.detectChanges();
      },
      error: (e) => console.error(e),
    });
  }

  // 2. Open the Modal
  viewDetails(conf: Conference): void {
    this.selectedConference = conf;
  }

  // 3. Close the Modal
  closeDetails(): void {
    this.selectedConference = null;
  }

  deleteConference(id: number): void {
    if (confirm('Are you sure you want to delete this conference?')) {
      this.conferenceService.delete(id).subscribe(() => this.loadConferences());
    }
  }
}
