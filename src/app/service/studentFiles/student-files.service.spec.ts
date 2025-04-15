import { TestBed } from '@angular/core/testing';

import { StudentFilesService } from './student-files.service';

describe('StudentFilesService', () => {
  let service: StudentFilesService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StudentFilesService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
