import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Reservation } from './reservation';

describe('Reservation', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Reservation, HttpClientTestingModule],
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(Reservation);
    expect(fixture.componentInstance).toBeTruthy();
  });
});
