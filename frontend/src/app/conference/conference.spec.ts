import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing'; // Needed for Service
import { ConferenceComponent } from './conference'; // Updated import name

describe('ConferenceComponent', () => {
  let component: ConferenceComponent;
  let fixture: ComponentFixture<ConferenceComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ConferenceComponent,
        HttpClientTestingModule // Mock the backend connection for tests
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ConferenceComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
