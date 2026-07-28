import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SitesTest } from './sites-test';

describe('SitesTest', () => {
  let component: SitesTest;
  let fixture: ComponentFixture<SitesTest>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SitesTest],
    }).compileComponents();

    fixture = TestBed.createComponent(SitesTest);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
