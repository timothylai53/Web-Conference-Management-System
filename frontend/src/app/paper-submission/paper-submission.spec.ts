import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaperSubmission } from './paper-submission';

describe('PaperSubmission', () => {
  let component: PaperSubmission;
  let fixture: ComponentFixture<PaperSubmission>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PaperSubmission]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PaperSubmission);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
