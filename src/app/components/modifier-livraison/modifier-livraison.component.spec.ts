import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ModifierLivraisonComponent } from './modifier-livraison.component';

describe('ModifierLivraisonComponent', () => {
  let component: ModifierLivraisonComponent;
  let fixture: ComponentFixture<ModifierLivraisonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ModifierLivraisonComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ModifierLivraisonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
