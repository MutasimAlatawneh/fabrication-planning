import { TestBed } from '@angular/core/testing';

import { Fabrication } from './fabrication';

describe('Fabrication', () => {
  let service: Fabrication;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Fabrication);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
