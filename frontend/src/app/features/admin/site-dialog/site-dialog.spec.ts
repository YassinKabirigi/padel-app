import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SiteDialog } from './site-dialog';

describe('SiteDialog', () => {
  let component: SiteDialog;
  let fixture: ComponentFixture<SiteDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SiteDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(SiteDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
