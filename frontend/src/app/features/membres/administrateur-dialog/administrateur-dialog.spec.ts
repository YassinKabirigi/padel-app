import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdministrateurDialog } from './administrateur-dialog';

describe('AdministrateurDialog', () => {
  let component: AdministrateurDialog;
  let fixture: ComponentFixture<AdministrateurDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdministrateurDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(AdministrateurDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
