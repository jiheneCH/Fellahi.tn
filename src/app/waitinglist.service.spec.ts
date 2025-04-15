import { TestBed } from '@angular/core/testing';

import { WaitinglistService } from './waitinglist.service';

describe('WaitinglistService', () => {
  let service: WaitinglistService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WaitinglistService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
