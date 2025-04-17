import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RegisterGoogleComponent } from './register-google.component';

describe('RegisterGoogleComponent', () => {
  let component: RegisterGoogleComponent;
  let fixture: ComponentFixture<RegisterGoogleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RegisterGoogleComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RegisterGoogleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
