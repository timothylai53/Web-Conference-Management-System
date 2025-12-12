import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SchedulingProgram } from './scheduling-program';

describe('SchedulingProgram', () => {
  let component: SchedulingProgram;
  let fixture: ComponentFixture<SchedulingProgram>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SchedulingProgram]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SchedulingProgram);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
