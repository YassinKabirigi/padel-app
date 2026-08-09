import { TestBed } from '@angular/core/testing';

import { Penalite } from './penalite';

describe('Penalite', () => {
  let service: Penalite;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Penalite);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
