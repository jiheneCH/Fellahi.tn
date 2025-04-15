import { TestBed } from '@angular/core/testing';

import { ReclamationAdminService } from './reclamation-admin.service';

describe('ReclamationAdminService', () => {
  let service: ReclamationAdminService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ReclamationAdminService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
