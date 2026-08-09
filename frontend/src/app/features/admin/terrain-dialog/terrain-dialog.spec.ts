import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TerrainDialog } from './terrain-dialog';

describe('TerrainDialog', () => {
  let component: TerrainDialog;
  let fixture: ComponentFixture<TerrainDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [TerrainDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(TerrainDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
