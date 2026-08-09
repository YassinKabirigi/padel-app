import { TestBed } from '@angular/core/testing';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { TerrainDialog } from './terrain-dialog';

describe('TerrainDialog', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TerrainDialog],
      providers: [
        { provide: MatDialogRef, useValue: { close: () => {} } },
        { provide: MAT_DIALOG_DATA, useValue: { terrain: null, sites: [] } }
      ]
    }).compileComponents();
  });

  it('should create', () => {
    const fixture = TestBed.createComponent(TerrainDialog);
    expect(fixture.componentInstance).toBeTruthy();
  });
});
