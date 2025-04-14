import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TendanceArticleComponent } from './tendance-article.component';

describe('TendanceArticleComponent', () => {
  let component: TendanceArticleComponent;
  let fixture: ComponentFixture<TendanceArticleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TendanceArticleComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(TendanceArticleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
