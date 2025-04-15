import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TransporteurSideBarComponent } from './transporteur-side-bar.component';

describe('TransporteurSideBarComponent', () => {
  let component: TransporteurSideBarComponent;
  let fixture: ComponentFixture<TransporteurSideBarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TransporteurSideBarComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TransporteurSideBarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
