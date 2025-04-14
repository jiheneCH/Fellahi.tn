import { TestBed } from '@angular/core/testing';

import { InternshipStudentInterceptor } from './internship-student.interceptor';

describe('InternshipStudentInterceptor', () => {
  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      InternshipStudentInterceptor
      ]
  }));

  it('should be created', () => {
    const interceptor: InternshipStudentInterceptor = TestBed.inject(InternshipStudentInterceptor);
    expect(interceptor).toBeTruthy();
  });
});
