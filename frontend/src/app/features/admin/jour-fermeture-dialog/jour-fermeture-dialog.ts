import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { SiteModel } from '../../../core/services/site';
import { JourFermetureModel } from '../../../core/services/jour-fermeture';

@Component({
  selector: 'app-jour-fermeture-dialog',
  imports: [CommonModule, FormsModule, MatDialogModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule],
  templateUrl: './jour-fermeture-dialog.html',
  styleUrl: './jour-fermeture-dialog.scss'
})
export class JourFermetureDialog {
  dateFermeture: string;
  motif: string;
  idSite: number | null;

  constructor(
    public dialogRef: MatDialogRef<JourFermetureDialog>,
    @Inject(MAT_DIALOG_DATA) public data: { fermeture: JourFermetureModel | null; sites: SiteModel[] }
  ) {
    this.dateFermeture = data.fermeture?.dateFermeture ?? '';
    this.motif = data.fermeture?.motif ?? '';
    this.idSite = data.fermeture?.idSite ?? null;
  }

  onAnnuler(): void {
    this.dialogRef.close();
  }

  onValider(): void {
    if (!this.dateFermeture || !this.motif) {
      return;
    }
    this.dialogRef.close({
      dateFermeture: this.dateFermeture,
      motif: this.motif,
      idSite: this.idSite
    });
  }
}
