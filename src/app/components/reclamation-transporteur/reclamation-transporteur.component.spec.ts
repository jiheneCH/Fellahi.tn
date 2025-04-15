import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReclamationTransporteurComponent } from './reclamation-transporteur.component';

describe('ReclamationTransporteurComponent', () => {
  let component: ReclamationTransporteurComponent;
  let fixture: ComponentFixture<ReclamationTransporteurComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReclamationTransporteurComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReclamationTransporteurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
