import { TestBed } from '@angular/core/testing';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { JourFermetureDialog } from './jour-fermeture-dialog';

describe('JourFermetureDialog', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JourFermetureDialog],
      providers: [
        { provide: MatDialogRef, useValue: { close: () => {} } },
        { provide: MAT_DIALOG_DATA, useValue: { fermeture: null, sites: [] } }
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(JourFermetureDialog);
    expect(fixture.componentInstance).toBeTruthy();
  });
});
