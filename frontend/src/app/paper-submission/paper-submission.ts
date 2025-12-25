import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { provideHttpClient, withInterceptorsFromDi } from '@angular/common/http';
import { PaperSubmissionService, Paper as PaperAPI, Track, Keyword, Author } from './paper-submission.service';

// Angular Material imports
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatSelectModule } from '@angular/material/select';
import { MatTableModule } from '@angular/material/table';
import { MatChipsModule } from '@angular/material/chips';
import { MatStepperModule } from '@angular/material/stepper';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatBadgeModule } from '@angular/material/badge';
import { MatDividerModule } from '@angular/material/divider';

interface AuthorRow {
  first_name: string;
  last_name: string;
  email: string;
  affiliation: string;
  is_corresponding_author: boolean;
  author_order: number;
}

interface PaperDisplay {
  paper_id?: number;
  title: string;
  track_id: number;
  trackName: string;
  conference_id?: number;
  abstractText: string;
  submission_file?: string;
  file_type?: string;
  version?: number;
  status: string;
  submitted_at?: Date;
  last_updated?: Date;
  submitted_by?: number;
}

@Component({
  selector: 'app-paper-submission',
  standalone: true,
  imports: [
    CommonModule, 
    FormsModule,
    MatCardModule,
    MatButtonModule,
    MatInputModule,
    MatFormFieldModule,
    MatSelectModule,
    MatTableModule,
    MatChipsModule,
    MatStepperModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatCheckboxModule,
    MatTooltipModule,
    MatBadgeModule,
    MatDividerModule
  ],
  providers: [PaperSubmissionService],
  templateUrl: './paper-submission.html',
  styleUrl: './paper-submission-material.css',
})
export class PaperSubmission implements OnInit {
  // View control
  currentView: 'dashboard' | 'wizard' | 'detail' = 'dashboard';
  currentStep = 0;
  isAmendment = false; // Track if we're amending a rejected paper
  amendingPaperId?: number; // Store the ID of paper being amended
  
  // Table columns for Material table
  displayedColumns: string[] = ['paper_id', 'title', 'conference', 'track', 'status', 'submitted_at', 'last_updated', 'actions'];
  
  // Step 1 - Paper Info
  paperInfo = {
    title: '',
    abstractText: '',
    track_id: 0,
    conference_id: 0
  };

  // Step 2 - Authors
  authors: AuthorRow[] = [{
    first_name: '',
    last_name: '',
    email: '',
    affiliation: '',
    is_corresponding_author: false,
    author_order: 1
  }];

  // Step 3 - File Upload
  fileInfo = {
    submission_file: null as File | null,
    file_type: 'PDF',
    version: 1
  };
  uploadedFileName = '';

  // Step 4 - Keywords
  selectedKeywords: string[] = [];
  newKeyword = '';
  availableKeywords: string[] = [
    'Machine Learning', 'Artificial Intelligence', 'Deep Learning', 
    'Natural Language Processing', 'Computer Vision', 'Data Science',
    'Neural Networks', 'Reinforcement Learning', 'Transfer Learning',
    'Edge Computing', 'IoT', 'Big Data', 'Cloud Computing'
  ];

  // Data
  conferences: any[] = [];
  tracks: Track[] = [];
  submittedPapers: PaperDisplay[] = [];
  selectedPaper: PaperDisplay | null = null;
  
  loading = false;
  errorMessage = '';

  constructor(
    private paperService: PaperSubmissionService,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit() {
    console.log('[ngOnInit] Paper Submission component initialized');
    console.log('[ngOnInit] About to call loadConferences()');
    this.loadConferences();
    console.log('[ngOnInit] loadConferences() called');
    this.loadTracks();
    this.loadPapers();
  }

  loadConferences() {
    console.log('[loadConferences] Starting...');
    this.paperService.getConferences().subscribe({
      next: (conferences) => {
        this.conferences = conferences;
        console.log('Conferences loaded:', conferences);
        console.log('Conference count:', this.conferences.length);
      },
      error: (error) => {
        console.error('Error loading conferences:', error);
        this.errorMessage = 'Failed to load conferences.';
      }
    });
  }

  loadTracks() {
    this.paperService.getAllTracks().subscribe({
      next: (tracks) => {
        this.tracks = tracks;
        console.log('Tracks loaded:', tracks);
      },
      error: (error) => {
        console.error('Error loading tracks:', error);
        this.errorMessage = 'Backend connection failed. Using default tracks.';
        this.tracks = [
          { track_id: 1, name: 'Artificial Intelligence' },
          { track_id: 2, name: 'Machine Learning' },
          { track_id: 3, name: 'Data Science' },
          { track_id: 4, name: 'Web Development' },
          { track_id: 5, name: 'Software Engineering' }
        ];
      }
    });
  }

  loadPapers() {
    console.log('[loadPapers] Starting, setting loading = true');
    this.loading = true;
    this.errorMessage = '';
    
    // Get current user from localStorage
    const currentUser = localStorage.getItem('currentUser');
    if (!currentUser) {
      console.warn('No user logged in');
      this.loading = false;
      this.submittedPapers = [];
      return;
    }

    const user = JSON.parse(currentUser);
    const userId = user.user_id;
    console.log('[loadPapers] Loading papers for user ID:', userId);
    
    // Load only papers submitted by the current user
    this.paperService.getPapersByUserId(userId).subscribe({
      next: (papers) => {
        console.log('Papers loaded for user:', papers.length);
        console.log('Raw papers data:', papers);
        console.log('Available tracks:', this.tracks);
        
        // Debug: Check first paper structure
        if (papers.length > 0) {
          console.log('First paper object:', papers[0]);
          console.log('First paper conference_id:', papers[0].conference_id);
          console.log('First paper keys:', Object.keys(papers[0]));
        }
        
        this.submittedPapers = papers.map(paper => {
          const track = this.tracks.find(t => t.track_id === paper.track_id);
          const mapped = {
            paper_id: paper.paper_id,
            title: paper.title,
            track_id: paper.track_id,
            trackName: track?.name || 'Unknown Track',
            conference_id: paper.conference_id,
            abstractText: paper.abstractText,
            submission_file: paper.submission_file,
            file_type: paper.file_type,
            version: paper.version,
            status: paper.status || 'Submitted',
            submitted_at: paper.submitted_at,
            last_updated: paper.last_updated,
            submitted_by: paper.submitted_by
          };
          console.log('Mapped paper:', mapped);
          return mapped;
        });
        console.log('Mapped submittedPapers:', this.submittedPapers);
        this.loading = false;
        console.log('Loading set to false, papers count:', this.submittedPapers.length);
        console.log('Current view:', this.currentView);
        console.log('Loading state:', this.loading);
        // Force change detection
        this.cdr.detectChanges();
        console.log('Change detection triggered');
      },
      error: (error) => {
        console.error('Error loading papers:', error);
        this.errorMessage = 'Cannot connect to backend server.';
        this.submittedPapers = [];
        this.loading = false;
      }
    });
  }

  // View Navigation
  showDashboard() {
    console.log('[showDashboard] Switching to dashboard view');
    this.currentView = 'dashboard';
    this.loadPapers();
  }

  showWizard() {
    this.currentView = 'wizard';
    this.currentStep = 0;
    this.isAmendment = false;
    this.amendingPaperId = undefined;
    this.resetForm();
  }

  showAmendWizard(paper: PaperDisplay) {
    this.currentView = 'wizard';
    this.currentStep = 0;
    this.isAmendment = true;
    this.amendingPaperId = paper.paper_id;
    
    // Pre-populate form with existing paper data
    this.paperInfo = {
      title: paper.title,
      abstractText: paper.abstractText,
      track_id: paper.track_id,
      conference_id: paper.conference_id || 0
    };
    
    this.fileInfo.version = (paper.version || 1) + 1;
    
    // Load existing authors for this paper
    this.loadPaperAuthors(paper.paper_id!);
  }

  loadPaperAuthors(paperId: number) {
    this.paperService.getAuthorsByPaperId(paperId).subscribe({
      next: (authors) => {
        if (authors && authors.length > 0) {
          this.authors = authors.map(a => ({
            first_name: a.first_name,
            last_name: a.last_name,
            email: a.email,
            affiliation: a.affiliation || '',
            is_corresponding_author: a.is_corresponding_author || false,
            author_order: a.author_order || 1
          }));
        }
      },
      error: (err) => console.error('Error loading authors:', err)
    });
  }

  showDetail(paper: PaperDisplay) {
    this.selectedPaper = paper;
    this.currentView = 'detail';
  }

  // Wizard Navigation
  nextStep() {
    if (this.currentStep < 4) {
      if (this.validateStep(this.currentStep)) {
        this.currentStep++;
      }
    }
  }

  previousStep() {
    if (this.currentStep > 1) {
      this.currentStep--;
    }
  }

  validateStep(step: number): boolean {
    switch(step) {
      case 1:
        if (!this.paperInfo.title.trim()) {
          alert('Please enter paper title');
          return false;
        }
        if (!this.paperInfo.abstractText.trim()) {
          alert('Please enter abstract');
          return false;
        }
        if (!this.paperInfo.track_id) {
          alert('Please select a track');
          return false;
        }
        return true;
      case 2:
        if (this.authors.length === 0) {
          alert('Please add at least one author');
          return false;
        }
        for (let author of this.authors) {
          if (!author.first_name.trim() || !author.email.trim()) {
            alert('Please fill in author first name and email');
            return false;
          }
        }
        return true;
      case 3:
        if (!this.fileInfo.submission_file) {
          alert('Please upload a submission file');
          return false;
        }
        return true;
      default:
        return true;
    }
  }

  // Author Management
  addAuthor() {
    this.authors.push({
      first_name: '',
      last_name: '',
      email: '',
      affiliation: '',
      is_corresponding_author: false,
      author_order: this.authors.length + 1
    });
  }

  removeAuthor(index: number) {
    if (this.authors.length > 1) {
      this.authors.splice(index, 1);
      // Update author orders
      this.authors.forEach((author, i) => author.author_order = i + 1);
    }
  }

  // File Upload
  onFileSelected(event: any) {
    const file = event.target.files[0];
    if (file) {
      // Validate file size (10MB max)
      if (file.size > 10 * 1024 * 1024) {
        this.errorMessage = 'File size exceeds 10MB limit';
        return;
      }
      
      this.fileInfo.submission_file = file;
      this.uploadedFileName = file.name;
      this.fileInfo.file_type = file.type.includes('pdf') ? 'PDF' : 'DOC';
      this.errorMessage = '';
    }
  }

  // Upload file to server
  async uploadFile(): Promise<string> {
    if (!this.fileInfo.submission_file) {
      throw new Error('No file selected');
    }

    const formData = new FormData();
    formData.append('file', this.fileInfo.submission_file);

    try {
      const response: any = await this.paperService.uploadFile(formData).toPromise();
      return response.filename; // Return the server-generated filename
    } catch (error) {
      console.error('File upload error:', error);
      throw new Error('Failed to upload file');
    }
  }

  // Keywords Management
  addKeyword() {
    if (this.newKeyword.trim() && !this.selectedKeywords.includes(this.newKeyword.trim())) {
      this.selectedKeywords.push(this.newKeyword.trim());
      this.newKeyword = '';
    }
  }

  toggleKeyword(keyword: string) {
    const index = this.selectedKeywords.indexOf(keyword);
    if (index > -1) {
      this.selectedKeywords.splice(index, 1);
    } else {
      this.selectedKeywords.push(keyword);
    }
  }

  removeKeyword(keyword: string) {
    const index = this.selectedKeywords.indexOf(keyword);
    if (index > -1) {
      this.selectedKeywords.splice(index, 1);
    }
  }

  // Submit Paper (Final Step)
  async submitPaper() {
    if (!this.validateStep(4)) return;
    
    this.loading = true;
    this.errorMessage = '';
    
    // Get current user ID
    const currentUser = localStorage.getItem('currentUser');
    if (!currentUser) {
      alert('You must be logged in to submit a paper');
      this.loading = false;
      return;
    }
    const user = JSON.parse(currentUser);
    const userId = user.user_id;
    
    try {
      // If this is an amendment, use the amendment endpoint
      if (this.isAmendment && this.amendingPaperId) {
        await this.submitAmendment(userId);
        return;
      }
      
      // Step 1: Upload file to server
      let uploadedFilename = '';
      if (this.fileInfo.submission_file) {
        uploadedFilename = await this.uploadFile();
        console.log('File uploaded successfully:', uploadedFilename);
      }

      // Step 2: Create Paper with uploaded filename
      const newPaper: PaperAPI = {
        track_id: this.paperInfo.track_id,
        conference_id: this.paperInfo.conference_id,
        title: this.paperInfo.title,
        abstractText: this.paperInfo.abstractText,
        submission_file: uploadedFilename, // Use server-generated filename
        file_type: this.fileInfo.file_type,
        version: this.fileInfo.version,
        status: 'Submitted',
        submitted_by: userId, // Use actual logged-in user ID
        submitted_at: new Date(),
        last_updated: new Date()
      };

      this.paperService.createPaper(newPaper).subscribe({
        next: (savedPaper) => {
          console.log('Paper created:', savedPaper);
          
          // Step 2: Create Authors
          this.authors.forEach((author) => {
            const authorData: Author = {
              paper_id: savedPaper.paper_id,
              first_name: author.first_name,
              last_name: author.last_name,
              email: author.email,
              affiliation: author.affiliation,
              is_corresponding_author: author.is_corresponding_author,
              author_order: author.author_order
            };
            this.paperService.createAuthor(authorData).subscribe({
              next: (a) => console.log('Author created:', a),
              error: (e) => console.error('Error creating author:', e)
            });
          });

          // Step 3: Create Keywords
          this.selectedKeywords.forEach(keywordText => {
            const keyword: Keyword = { keyword: keywordText };
            this.paperService.createKeyword(keyword).subscribe({
              next: (k) => console.log('Keyword created:', k),
              error: (e) => console.error('Error creating keyword:', e)
            });
          });

          console.log('[submitPaper] Paper creation successful');
          // Immediately switch view and reset loading
          this.currentView = 'dashboard';
          this.loading = false;
          this.resetForm();
          // Show alert and reload papers
          alert('Paper submitted successfully!');
          this.loadPapers();
        },
        error: (error) => {
          console.error('Error submitting paper:', error);
          alert('Failed to submit paper. Please try again.');
          this.loading = false;
        }
      });
    } catch (error) {
      console.error('Error:', error);
      alert('An error occurred during submission.');
      this.loading = false;
    }
  }

  async submitAmendment(userId: number) {
    try {
      // Step 1: Upload new file if provided
      let uploadedFilename = '';
      if (this.fileInfo.submission_file) {
        uploadedFilename = await this.uploadFile();
        console.log('Amended file uploaded successfully:', uploadedFilename);
      }

      // Step 2: Submit amended paper
      const amendedPaper: PaperAPI = {
        title: this.paperInfo.title,
        abstractText: this.paperInfo.abstractText,
        track_id: this.paperInfo.track_id,
        submission_file: uploadedFilename || undefined,
        file_type: this.fileInfo.file_type
      };

      this.paperService.amendPaper(this.amendingPaperId!, amendedPaper).subscribe({
        next: (updatedPaper) => {
          console.log('Paper amended successfully:', updatedPaper);
          
          // Update authors if changed
          // Note: In a production system, you'd want to delete old authors and create new ones
          // For simplicity, we'll keep the existing authors
          
          this.currentView = 'dashboard';
          this.loading = false;
          this.resetForm();
          alert('Paper amended and resubmitted successfully!');
          this.loadPapers();
        },
        error: (error) => {
          console.error('Error amending paper:', error);
          alert('Failed to amend paper. Please try again.');
          this.loading = false;
        }
      });
    } catch (error) {
      console.error('Error:', error);
      alert('An error occurred during amendment submission.');
      this.loading = false;
    }
  }

  resetForm() {
    this.paperInfo = { title: '', abstractText: '', track_id: 0, conference_id: 0 };
    this.authors = [{
      first_name: '', last_name: '', email: '', affiliation: '',
      is_corresponding_author: false, author_order: 1
    }];
    this.fileInfo = { submission_file: null, file_type: 'PDF', version: 1 };
    this.uploadedFileName = '';
    this.selectedKeywords = [];
    this.currentStep = 0;
    this.loading = false;
    this.isAmendment = false;
    this.amendingPaperId = undefined;
  }

  // Paper Actions
  withdrawPaper(id?: number) {
    if (!id) return;
    if (confirm('Are you sure you want to withdraw this paper?')) {
      this.paperService.deletePaper(id).subscribe({
        next: () => {
          alert('Paper withdrawn successfully!');
          this.loadPapers();
        },
        error: (error) => {
          console.error('Error withdrawing paper:', error);
          alert('Failed to withdraw paper.');
        }
      });
    }
  }

  uploadCameraReady(paperId?: number) {
    alert('Camera-ready upload feature coming soon for Paper ID: ' + paperId);
  }

  getStatusClass(status: string): string {
    return 'status-' + status.toLowerCase().replace(/\s+/g, '-');
  }

  getConferenceName(conferenceId?: number): string {
    console.log('[getConferenceName] Called with conferenceId:', conferenceId, 'Type:', typeof conferenceId);
    console.log('[getConferenceName] Available conferences:', this.conferences);
    
    if (!conferenceId) {
      console.log('[getConferenceName] No conferenceId, returning N/A');
      return 'N/A';
    }
    
    const conference = this.conferences.find(c => {
      console.log('[getConferenceName] Comparing:', c.conference_id, '(type:', typeof c.conference_id, ') with', conferenceId, '(type:', typeof conferenceId, ')');
      return c.conference_id == conferenceId; // Use == to handle string/number comparison
    });
    
    console.log('[getConferenceName] Found conference:', conference);
    return conference ? conference.title : 'Unknown Conference';
  }
}
