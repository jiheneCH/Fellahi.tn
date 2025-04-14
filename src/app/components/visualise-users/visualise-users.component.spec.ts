import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VisualiseUsersComponent } from './visualise-users.component';

describe('VisualiseUsersComponent', () => {
  let component: VisualiseUsersComponent;
  let fixture: ComponentFixture<VisualiseUsersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VisualiseUsersComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VisualiseUsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
