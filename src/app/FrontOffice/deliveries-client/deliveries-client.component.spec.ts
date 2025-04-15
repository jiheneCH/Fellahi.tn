import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DeliveriesClientComponent } from './deliveries-client.component';

describe('DeliveriesClientComponent', () => {
  let component: DeliveriesClientComponent;
  let fixture: ComponentFixture<DeliveriesClientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DeliveriesClientComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DeliveriesClientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
