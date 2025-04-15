import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransporteurDashboardComponent } from './transporteur-dashboard.component';

describe('TransporteurDashboardComponent', () => {
  let component: TransporteurDashboardComponent;
  let fixture: ComponentFixture<TransporteurDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TransporteurDashboardComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransporteurDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
