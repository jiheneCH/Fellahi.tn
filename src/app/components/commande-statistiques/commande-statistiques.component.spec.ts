import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommandeStatistiquesComponent } from './commande-statistiques.component';

describe('CommandeStatistiquesComponent', () => {
  let component: CommandeStatistiquesComponent;
  let fixture: ComponentFixture<CommandeStatistiquesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CommandeStatistiquesComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CommandeStatistiquesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
