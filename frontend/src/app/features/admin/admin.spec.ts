import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { Admin } from './admin';

describe('Admin', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [Admin, HttpClientTestingModule],
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(Admin);
    const admin = fixture.componentInstance;
    expect(admin).toBeTruthy();
  });
});
