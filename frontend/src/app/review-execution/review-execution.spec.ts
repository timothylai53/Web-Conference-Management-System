import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewExecution } from './review-execution';

describe('ReviewExecution', () => {
  let component: ReviewExecution;
  let fixture: ComponentFixture<ReviewExecution>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReviewExecution]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewExecution);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
