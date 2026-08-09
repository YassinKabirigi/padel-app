import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { SiteModel } from '../../../core/services/site';
import { MembreModel } from '../../../core/services/membre';

@Component({
  selector: 'app-membre-dialog',
  imports: [CommonModule, FormsModule, MatDialogModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule],
  templateUrl: './membre-dialog.html',
  styleUrl: './membre-dialog.scss'
})
export class MembreDialog {
  nom: string;
  prenom: string;
  email: string;
  telephone: string;
  typeMembre: string;
  siteId: number | null;

  constructor(
    public dialogRef: MatDialogRef<MembreDialog>,
    @Inject(MAT_DIALOG_DATA) public data: { membre: MembreModel | null; sites: SiteModel[] }
  ) {
    this.nom = data.membre?.nom ?? '';
    this.prenom = data.membre?.prenom ?? '';
    this.email = data.membre?.email ?? '';
    this.telephone = data.membre?.telephone ?? '';
    this.typeMembre = data.membre?.typeMembre ?? 'GLOBAL';
    this.siteId = data.membre?.site?.idSite ?? null;
  }

  onAnnuler(): void {
    this.dialogRef.close();
  }

  onValider(): void {
    if (!this.nom || !this.prenom || !this.email) {
      return;
    }
    this.dialogRef.close({
      nom: this.nom,
      prenom: this.prenom,
      email: this.email,
      telephone: this.telephone,
      typeMembre: this.typeMembre,
      site: this.typeMembre === 'SITE' ? { idSite: this.siteId } : null
    });
  }
}
