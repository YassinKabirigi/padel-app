import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MembreDialog } from './membre-dialog';

describe('MembreDialog', () => {
  let component: MembreDialog;
  let fixture: ComponentFixture<MembreDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MembreDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(MembreDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
