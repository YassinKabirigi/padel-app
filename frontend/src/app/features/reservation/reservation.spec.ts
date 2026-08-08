import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { Reservation } from './reservation';

describe('Reservation', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Reservation],
      providers: [provideHttpClient(), provideHttpClientTesting()]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(Reservation);
    expect(fixture.componentInstance).toBeTruthy();
  });
});
