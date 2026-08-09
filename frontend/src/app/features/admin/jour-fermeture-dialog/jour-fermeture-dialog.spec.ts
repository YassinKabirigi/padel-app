import { ComponentFixture, TestBed } from '@angular/core/testing';

import { JourFermetureDialog } from './jour-fermeture-dialog';

describe('JourFermetureDialog', () => {
  let component: JourFermetureDialog;
  let fixture: ComponentFixture<JourFermetureDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [JourFermetureDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(JourFermetureDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
