import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';

@Component({
  standalone: true,
  selector: 'app-grading',
  imports: [
    CommonModule,
    FormsModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    MatSelectModule,
    MatButtonModule
  ],
  templateUrl: './grading.html',
  styleUrls: ['./grading.css']
})
export class GradingComponent implements OnInit {

  assignmentId!: number;

  // ✅ REQUIRED properties (used in HTML)
  pdfUrl = 'https://example.com/sample.pdf';

  scoreCriteria = [
    { name: 'Originality', score: 0 },
    { name: 'Technical Quality', score: 0 },
    { name: 'Clarity', score: 0 }
  ];

  overallScore = 0;
  commentsToAuthor = '';
  commentsToChair = '';
  recommendation = '';
  confidence?: number;

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.assignmentId = Number(
      this.route.snapshot.paramMap.get('assignmentId')
    );
  }

  submitReview(): void {
    console.log('Review submitted', {
      assignmentId: this.assignmentId,
      scores: this.scoreCriteria,
      overallScore: this.overallScore,
      recommendation: this.recommendation,
      confidence: this.confidence
    });
  }
}
