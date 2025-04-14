import { ComponentFixture, TestBed } from '@angular/core/testing';

import { StatistiquesAdminFarmerComponent } from './statistiques-admin-farmer.component';

describe('StatistiquesAdminFarmerComponent', () => {
  let component: StatistiquesAdminFarmerComponent;
  let fixture: ComponentFixture<StatistiquesAdminFarmerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ StatistiquesAdminFarmerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StatistiquesAdminFarmerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
