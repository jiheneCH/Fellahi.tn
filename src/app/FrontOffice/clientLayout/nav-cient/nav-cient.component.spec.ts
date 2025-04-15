import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NavCientComponent } from './nav-cient.component';

describe('NavCientComponent', () => {
  let component: NavCientComponent;
  let fixture: ComponentFixture<NavCientComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NavCientComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NavCientComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
