import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReclamationAdminTreatComponent } from './reclamation-admin-treat.component';

describe('ReclamationAdminTreatComponent', () => {
  let component: ReclamationAdminTreatComponent;
  let fixture: ComponentFixture<ReclamationAdminTreatComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReclamationAdminTreatComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReclamationAdminTreatComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
