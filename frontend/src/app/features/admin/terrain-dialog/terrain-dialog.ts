import { Component, Inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MatDialogModule, MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatButtonModule } from '@angular/material/button';
import { SiteModel } from '../../../core/services/site';
import { TerrainModel } from '../../../core/services/terrain';

@Component({
  selector: 'app-terrain-dialog',
  imports: [CommonModule, FormsModule, MatDialogModule, MatFormFieldModule, MatInputModule, MatSelectModule, MatButtonModule],
  templateUrl: './terrain-dialog.html',
  styleUrl: './terrain-dialog.scss'
})
export class TerrainDialog {
  numero: string;
  siteId: number | null;

  constructor(
    public dialogRef: MatDialogRef<TerrainDialog>,
    @Inject(MAT_DIALOG_DATA) public data: { terrain: TerrainModel | null; sites: SiteModel[] }
  ) {
    this.numero = data.terrain?.numero ?? '';
    this.siteId = data.terrain?.site?.idSite ?? null;
  }

  onAnnuler(): void {
    this.dialogRef.close();
  }

  onValider(): void {
    if (!this.numero || !this.siteId) {
      return;
    }
    this.dialogRef.close({
      numero: this.numero,
      site: { idSite: this.siteId }
    });
  }
}
