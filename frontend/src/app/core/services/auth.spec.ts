import { TestBed } from '@angular/core/testing';
import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { Auth } from './auth';

describe('Auth', () => {
  let service: Auth;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [Auth, provideHttpClient(), provideHttpClientTesting()]
    });
    service = TestBed.inject(Auth);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should not be authenticated before login', () => {
    expect(service.isAuthenticated()).toBe(false);
  });

  it('should store token and matricule after successful login', () => {
    service.login('G0001').subscribe();

    const req = httpMock.expectOne('http://localhost:8080/api/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush({ token: 'fake-jwt-token', matricule: 'G0001', typeMembre: 'GLOBAL' });

    expect(service.isAuthenticated()).toBe(true);
    expect(service.getMatricule()).toBe('G0001');
    expect(service.getTypeMembre()).toBe('GLOBAL');
  });

  it('should clear token on logout', () => {
    service.login('G0001').subscribe();
    const req = httpMock.expectOne('http://localhost:8080/api/auth/login');
    req.flush({ token: 'fake-jwt-token', matricule: 'G0001', typeMembre: 'GLOBAL' });

    service.logout();

    expect(service.isAuthenticated()).toBe(false);
    expect(service.getMatricule()).toBeNull();
  });
});
