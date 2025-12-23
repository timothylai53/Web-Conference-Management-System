import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { ConferenceService } from './conference.service';

@Component({
  selector: 'app-conference-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './conference-form.html',
  styleUrl: './conference.css',
  providers: [ConferenceService]
})
export class ConferenceFormComponent implements OnInit {

  // Initialize all fields
  conference: any = {
    title: '',
    acronym: '',
    location: '',
    description: '',
    status: 'upcoming',
    website_url: '',
    start_date: '',
    end_date: '',
    submission_deadline: '',
    late_submission_deadline: '',
    review_deadline: '',
    rebuttal_deadline: '',
    camera_ready_deadline: '',
    registration_deadline: ''
  };

  isEditMode = false;
  conferenceId: number | null = null;
  errorMessage = '';

  constructor(
    private conferenceService: ConferenceService,
    private router: Router,
    private route: ActivatedRoute,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.conferenceId = +id;
      this.loadConference(this.conferenceId);
    }
  }

  loadConference(id: number): void {
    this.conferenceService.getById(id).subscribe({
      next: (data: any) => {
        this.conference = data;

        // Apply formatting to all date fields
        this.conference.start_date = this.formatDate(data.start_date);
        this.conference.end_date = this.formatDate(data.end_date);
        this.conference.submission_deadline = this.formatDate(data.submission_deadline);
        this.conference.late_submission_deadline = this.formatDate(data.late_submission_deadline);
        this.conference.review_deadline = this.formatDate(data.review_deadline);
        this.conference.rebuttal_deadline = this.formatDate(data.rebuttal_deadline);
        this.conference.camera_ready_deadline = this.formatDate(data.camera_ready_deadline);
        this.conference.registration_deadline = this.formatDate(data.registration_deadline);

        this.cdr.detectChanges();
      },
      error: (e) => {
        console.error('Error loading conference:', e);
        this.errorMessage = 'Could not load conference details.';
      }
    });
  }

  // FIXED: Safer date formatting to avoid timezone shifts
  formatDate(dateString: string | null): string {
    if (!dateString) return '';
    const date = new Date(dateString);
    // Use standard formatting to ensure YYYY-MM-DD matches HTML input type="date"
    const year = date.getFullYear();
    const month = ('0' + (date.getMonth() + 1)).slice(-2);
    const day = ('0' + date.getDate()).slice(-2);
    return `${year}-${month}-${day}`;
  }

  onSubmit(): void {
    if (!this.conference.title || !this.conference.acronym || !this.conference.start_date) {
      this.errorMessage = 'Please fill in all required fields marked with *';
      return;
    }

    const observer = {
      next: () => this.router.navigate(['/conference']),
      error: (e: any) => {
        console.error(e);
        this.errorMessage = 'Failed to save conference. Please try again.';
      }
    };

    if (this.isEditMode && this.conferenceId) {
      this.conferenceService.update(this.conferenceId, this.conference).subscribe(observer);
    } else {
      this.conferenceService.create(this.conference).subscribe(observer);
    }
  }

  cancel(): void {
    this.router.navigate(['/conference']);
  }
}
