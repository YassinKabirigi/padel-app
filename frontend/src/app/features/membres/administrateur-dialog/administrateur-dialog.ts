import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { SiteModel } from '../../../core/services/site';
import { AdministrateurModel } from '../../../core/services/administrateur';

@Component({
  selector: 'app-administrateur-dialog',
  imports: [CommonModule, FormsModule, MatDialogModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule],
  templateUrl: './administrateur-dialog.html',
  styleUrl: './administrateur-dialog.scss'
})
export class AdministrateurDialog {
  nom: string;
  prenom: string;
  email: string;
  typeAdmin: string;
  siteId: number | null;

  constructor(
    public dialogRef: MatDialogRef<AdministrateurDialog>,
    @Inject(MAT_DIALOG_DATA) public data: { admin: AdministrateurModel | null; sites: SiteModel[] }
  ) {
    this.nom = data.admin?.nom ?? '';
    this.prenom = data.admin?.prenom ?? '';
    this.email = data.admin?.email ?? '';
    this.typeAdmin = data.admin?.typeAdmin ?? 'GLOBAL';
    this.siteId = data.admin?.site?.idSite ?? null;
  }

  onAnnuler(): void {
    this.dialogRef.close();
  }

  onValider(): void {
    if (!this.nom || !this.prenom || !this.email) {
      return;
    }
    if (this.typeAdmin === 'SITE' && !this.siteId) {
      return;
    }
    this.dialogRef.close({
      nom: this.nom,
      prenom: this.prenom,
      email: this.email,
      typeAdmin: this.typeAdmin,
      site: this.typeAdmin === 'SITE' ? { idSite: this.siteId } : null
    });
  }
}
