import { TestBed } from '@angular/core/testing';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { SiteDialog } from './site-dialog';

describe('SiteDialog', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SiteDialog],
      providers: [
        { provide: MatDialogRef, useValue: { close: () => {} } },
        { provide: MAT_DIALOG_DATA, useValue: { site: null } }
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(SiteDialog);
    expect(fixture.componentInstance).toBeTruthy();
  });
});
