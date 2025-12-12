import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegistrationPayment } from './registration-payment';

describe('RegistrationPayment', () => {
  let component: RegistrationPayment;
  let fixture: ComponentFixture<RegistrationPayment>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RegistrationPayment]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegistrationPayment);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
