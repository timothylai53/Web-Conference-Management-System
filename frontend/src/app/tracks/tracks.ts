import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule, ActivatedRoute, Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { TrackService } from './track.service';
import { Track } from './track.model';
import { ConferenceService } from '../conference/conference.service';
import { Conference } from '../conference/conference.model';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-tracks',
  standalone: true,
  imports: [CommonModule, RouterModule, FormsModule],
  templateUrl: './tracks.html',
  styleUrl: '../conference/conference.css',
  providers: [TrackService, ConferenceService]
})
export class TracksComponent implements OnInit {

  // Data containers
  allTracks: Track[] = [];        // Always holds EVERYTHING
  displayedTracks: Track[] = [];  // Holds what is currently visible
  conferencesList: Conference[] = []; // For the dropdown menu
  conferencesMap: Map<number, string> = new Map();

  // Filter State
  selectedConferenceId: number | string = 'all'; // Default to 'all'

  constructor(
    private trackService: TrackService,
    private conferenceService: ConferenceService,
    private cdr: ChangeDetectorRef,
    private route: ActivatedRoute,
    private router: Router,
    public authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadData();
  }

  loadData(): void {
    // 1. Get Conferences
    this.conferenceService.getAll().subscribe(confs => {
      this.conferencesList = confs; // Save for dropdown
      confs.forEach(c => this.conferencesMap.set(c.conference_id!, c.title));

      // 2. Get All Tracks
      this.trackService.getAll().subscribe(data => {
        this.allTracks = data;

        // 3. Check URL for initial filter
        this.route.queryParams.subscribe(params => {
          const id = params['conference_id'];
          if (id) {
            this.selectedConferenceId = Number(id);
          } else {
            this.selectedConferenceId = 'all';
          }
          this.applyFilter(); // Run filter logic
        });
      });
    });
  }

  // --- THE NEW FILTER LOGIC ---
  applyFilter(): void {
    if (this.selectedConferenceId === 'all') {
      this.displayedTracks = [...this.allTracks];
      // Update URL to remove query param (clean URL)
      this.router.navigate([], { queryParams: { conference_id: null }, queryParamsHandling: 'merge' });
    } else {
      const filterId = Number(this.selectedConferenceId);
      this.displayedTracks = this.allTracks.filter(t => t.conference_id === filterId);
      // Update URL to reflect selection
      this.router.navigate([], { queryParams: { conference_id: filterId }, queryParamsHandling: 'merge' });
    }
    this.cdr.detectChanges();
  }

  deleteTrack(id: number): void {
    if (confirm('Are you sure you want to delete this track?')) {
      this.trackService.delete(id).subscribe(() => {
        // Remove from local array to avoid full reload
        this.allTracks = this.allTracks.filter(t => t.track_id !== id);
        this.applyFilter();
      });
    }
  }

  getConferenceName(id: number): string {
    return this.conferencesMap.get(id) || 'Unknown Conference';
  }
}
