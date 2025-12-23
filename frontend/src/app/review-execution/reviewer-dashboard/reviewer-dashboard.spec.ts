import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReviewerDashboardComponent } from './reviewer-dashboard';
import { RouterTestingModule } from '@angular/router/testing';

describe('ReviewerDashboardComponent', () => {
  let component: ReviewerDashboardComponent;
  let fixture: ComponentFixture<ReviewerDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ReviewerDashboardComponent, // standalone component
        RouterTestingModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ReviewerDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
