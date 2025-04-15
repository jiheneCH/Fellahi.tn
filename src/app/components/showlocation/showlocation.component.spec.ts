import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowlocationComponent } from './showlocation.component';

describe('ShowlocationComponent', () => {
  let component: ShowlocationComponent;
  let fixture: ComponentFixture<ShowlocationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ShowlocationComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowlocationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
