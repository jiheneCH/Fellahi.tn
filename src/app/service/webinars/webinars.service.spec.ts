import { TestBed } from '@angular/core/testing';

import { WebinarsService } from './webinars.service';

describe('WebinarsService', () => {
  let service: WebinarsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(WebinarsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});