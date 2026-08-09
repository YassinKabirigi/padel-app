import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-badge',
  imports: [CommonModule],
  templateUrl: './badge.html',
  styleUrl: './badge.scss'
})
export class Badge {
  @Input() texte: string = '';
  @Input() type: string = '';

  get couleur(): string {
    const couleurs: { [key: string]: string } = {
      'GLOBAL': '#7c4dff',
      'SITE': '#00bcd4',
      'LIBRE': '#66bb6a',
      'ADMIN_GLOBAL': '#5e35b1',
      'ADMIN_SITE': '#3949ab',
      'PRIVE': '#7c4dff',
      'PUBLIC': '#00bcd4',
      'PAYE': '#66bb6a',
      'NON_PAYE': '#ef5350',
      'ORGANISATEUR': '#5e35b1',
      'DISPONIBLE': '#66bb6a'
    };
    return couleurs[this.type] || '#888';
  }
}
