import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { TrackService } from './track.service';
import { Track } from './track.model';
import { ConferenceService } from '../conference/conference.service';
import { Conference } from '../conference/conference.model';

@Component({
  selector: 'app-track-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './track-form.html',
  styleUrl: './tracks.css',
  providers: [TrackService, ConferenceService]
})
export class TrackFormComponent implements OnInit {
  // Initialize with default values.
  // Note: conference_id is null initially to force the user to select (or default to first)
  track: Track = {
    conference_id: 0,
    name: '',
    description: ''
  };

  conferences: Conference[] = [];
  isEditMode = false;
  errorMessage = '';

  constructor(
    private trackService: TrackService,
    private conferenceService: ConferenceService,
    private router: Router,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    // 1. Load Conferences for the Dropdown first
    this.conferenceService.getAll().subscribe({
      next: (data) => {
        this.conferences = data;

        // Optional: If creating new, auto-select the first conference
        // You can remove this if you want "Select Conference" to be the default
        if (!this.isEditMode && this.conferences.length > 0) {
          this.track.conference_id = this.conferences[0].conference_id!;
        }

        this.cdr.detectChanges();
      },
      error: (e) => console.error('Error loading conferences', e)
    });

    // 2. Check if we are in Edit Mode
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.loadTrack(Number(id));
    }
  }

  loadTrack(id: number): void {
    this.trackService.getById(id).subscribe({
      next: (data) => {
        this.track = data;
        this.cdr.detectChanges(); // Force update view
      },
      error: () => {
        this.errorMessage = 'Could not load track data.';
        this.cdr.detectChanges();
      }
    });
  }

  onSubmit(): void {
    // Safety check: Ensure conference_id is a number
    this.track.conference_id = Number(this.track.conference_id);

    const observer = {
      next: () => {
        this.router.navigate(['/tracks']);
      },
      error: (e: any) => {
        console.error(e);
        this.errorMessage = this.isEditMode ? 'Failed to update track.' : 'Failed to create track.';
        this.cdr.detectChanges();
      }
    };

    if (this.isEditMode && this.track.track_id) {
      this.trackService.update(this.track.track_id, this.track).subscribe(observer);
    } else {
      this.trackService.create(this.track).subscribe(observer);
    }
  }

  cancel(): void {
    this.router.navigate(['/tracks']);
  }
}
