import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InternshpsComponent } from './internshps.component';

describe('InternshpsComponent', () => {
  let component: InternshpsComponent;
  let fixture: ComponentFixture<InternshpsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ InternshpsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(InternshpsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
