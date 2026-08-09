import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';
import { SiteModel } from '../../../core/services/site';

@Component({
  selector: 'app-site-dialog',
  imports: [CommonModule, FormsModule, MatDialogModule, MatFormFieldModule, MatInputModule, MatButtonModule],
  templateUrl: './site-dialog.html',
  styleUrl: './site-dialog.scss'
})
export class SiteDialog {
  nom: string;
  adresse: string;
  heureOuverture: string;
  heureFermeture: string;

  constructor(
    public dialogRef: MatDialogRef<SiteDialog>,
    @Inject(MAT_DIALOG_DATA) public data: { site: SiteModel | null }
  ) {
    this.nom = data.site?.nom ?? '';
    this.adresse = data.site?.adresse ?? '';
    this.heureOuverture = data.site?.heureOuverture ?? '08:00';
    this.heureFermeture = data.site?.heureFermeture ?? '22:00';
  }

  onAnnuler(): void {
    this.dialogRef.close();
  }

  onValider(): void {
    if (!this.nom || !this.adresse) {
      return;
    }
    this.dialogRef.close({
      nom: this.nom,
      adresse: this.adresse,
      heureOuverture: this.heureOuverture,
      heureFermeture: this.heureFermeture
    });
  }
}
