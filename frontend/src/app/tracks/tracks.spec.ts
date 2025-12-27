import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router'; // Import this
import { of } from 'rxjs'; // Import this

// 1. Correct the Import Name
import { TracksComponent } from './tracks';
import { HttpClientTestingModule } from '@angular/common/http/testing'; // You usually need this for Services

describe('TracksComponent', () => { // 2. Rename Describe
  let component: TracksComponent; // 3. Correct Type
  let fixture: ComponentFixture<TracksComponent>; // 4. Correct Type

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      // 5. Use the correct Component Class
      imports: [TracksComponent, HttpClientTestingModule],
      providers: [
        // Mock ActivatedRoute if your component uses it
        {
          provide: ActivatedRoute,
          useValue: { snapshot: { paramMap: { get: () => null } } }
        }
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(TracksComponent); // 6. Create correct component
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
