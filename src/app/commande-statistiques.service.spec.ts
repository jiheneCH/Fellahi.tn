import { TestBed } from '@angular/core/testing';

import { CommandeStatistiquesService } from './commande-statistiques.service';

describe('CommandeStatistiquesService', () => {
  let service: CommandeStatistiquesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(CommandeStatistiquesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
