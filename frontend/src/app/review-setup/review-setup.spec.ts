import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewSetup } from './review-setup';

describe('ReviewSetup', () => {
  let component: ReviewSetup;
  let fixture: ComponentFixture<ReviewSetup>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReviewSetup]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewSetup);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
