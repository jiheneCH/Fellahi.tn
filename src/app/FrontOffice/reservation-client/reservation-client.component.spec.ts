import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReservationClientComponent } from './reservation-client.component';

describe('ReservationClientComponent', () => {
  let component: ReservationClientComponent;
  let fixture: ComponentFixture<ReservationClientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReservationClientComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReservationClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
