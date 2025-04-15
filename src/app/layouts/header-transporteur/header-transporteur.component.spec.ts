import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HeaderTransporteurComponent } from './header-transporteur.component';

describe('HeaderTransporteurComponent', () => {
  let component: HeaderTransporteurComponent;
  let fixture: ComponentFixture<HeaderTransporteurComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HeaderTransporteurComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HeaderTransporteurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
