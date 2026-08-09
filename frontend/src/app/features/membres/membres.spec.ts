import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { Membres } from './membres';

describe('Membres', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Membres],
      providers: [provideHttpClient(), provideHttpClientTesting()]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(Membres);
    expect(fixture.componentInstance).toBeTruthy();
  });
});
