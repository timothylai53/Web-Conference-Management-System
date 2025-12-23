import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DiscussionService } from './discussion.service';

@Component({
  standalone: true,
  selector: 'app-discussion',
  imports: [CommonModule, FormsModule],
  templateUrl: './discussion.html',
  styleUrls: ['./discussion.css']
})
export class DiscussionComponent implements OnInit {

  paperId!: number;
  messages: any[] = [];
  newMessage = '';

  constructor(
    private route: ActivatedRoute,
    private discussionService: DiscussionService
  ) {}

  ngOnInit(): void {
    this.paperId = Number(
      this.route.snapshot.paramMap.get('paperId')
    );
    this.loadMessages();
  }

  loadMessages(): void {
    this.discussionService
      .getMessages(this.paperId)
      .subscribe(res => this.messages = res);
  }

  send(): void {
    if (!this.newMessage.trim()) return;

    this.discussionService.sendMessage({
      paperId: this.paperId,
      participantId: 1, // later from login/auth
      message: this.newMessage
    }).subscribe(() => {
      this.newMessage = '';
      this.loadMessages();
    });
  }
}
