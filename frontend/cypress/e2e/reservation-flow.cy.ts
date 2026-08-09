describe('Parcours principal - Connexion et Réservation', () => {
  it('permet à un membre de se connecter et de réserver un match', () => {
    cy.visit('http://localhost:4200/login');

    cy.get('input[name="matricule"]').type('G1042');
    cy.get('button[type="submit"]').click();

    cy.url().should('include', '/dashboard');

    cy.contains('a', 'Réservation').click();

    cy.url().should('include', '/reservation');

    cy.get('mat-select[name="terrain"]').click();
    cy.get('mat-option').first().click();

    const joursAleatoires = 25 + Math.floor(Math.random() * 65);
    const dateFuture = new Date();
    dateFuture.setDate(dateFuture.getDate() + joursAleatoires);
    const dateString = dateFuture.toISOString().split('T')[0];

    const heureAleatoire = 9 + Math.floor(Math.random() * 10);
    const heureString = `${heureAleatoire.toString().padStart(2, '0')}:00`;

    cy.get('input[name="date"]').type(dateString);
    cy.get('input[name="heure"]').type(heureString);

    cy.contains('button', 'Réserver mon match').click();

    cy.contains('Match créé avec succès', { timeout: 5000 }).should('be.visible');
  });
});
