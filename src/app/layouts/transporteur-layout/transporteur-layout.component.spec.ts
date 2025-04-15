import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransporteurLayoutComponent } from './transporteur-layout.component';

describe('TransporteurLayoutComponent', () => {
  let component: TransporteurLayoutComponent;
  let fixture: ComponentFixture<TransporteurLayoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TransporteurLayoutComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransporteurLayoutComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
