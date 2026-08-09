import { TestBed } from '@angular/core/testing';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MembreDialog } from './membre-dialog';

describe('MembreDialog', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MembreDialog],
      providers: [
        { provide: MatDialogRef, useValue: { close: () => {} } },
        { provide: MAT_DIALOG_DATA, useValue: { membre: null, sites: [] } }
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(MembreDialog);
    expect(fixture.componentInstance).toBeTruthy();
  });
});
