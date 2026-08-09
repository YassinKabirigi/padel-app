import { TestBed } from '@angular/core/testing';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { AdministrateurDialog } from './administrateur-dialog';

describe('AdministrateurDialog', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdministrateurDialog],
      providers: [
        { provide: MatDialogRef, useValue: { close: () => {} } },
        { provide: MAT_DIALOG_DATA, useValue: { admin: null, sites: [] } }
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(AdministrateurDialog);
    expect(fixture.componentInstance).toBeTruthy();
  });
});
