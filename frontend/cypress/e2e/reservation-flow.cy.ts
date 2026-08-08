describe('Parcours principal - Connexion et Réservation', () => {
  it('permet à un membre de se connecter et de réserver un match', () => {
    cy.visit('http://localhost:4200/login');

    cy.get('input[name="matricule"]').type('G0001');
    cy.get('button[type="submit"]').click();

    cy.url().should('include', '/reservation');

    cy.contains('Réserver un match').should('be.visible');

    cy.get('mat-select[name="terrain"]').click();
    cy.get('mat-option').first().click();

    // Date aleatoire entre J+25 et J+90 pour eviter tout conflit
    // avec un match cree lors d'une execution precedente du test
    const joursAleatoires = 25 + Math.floor(Math.random() * 65);
    const dateFuture = new Date();
    dateFuture.setDate(dateFuture.getDate() + joursAleatoires);
    const dateString = dateFuture.toISOString().split('T')[0];

    // Heure aleatoire entre 9h et 19h pour la meme raison
    const heureAleatoire = 9 + Math.floor(Math.random() * 10);
    const heureString = `${heureAleatoire.toString().padStart(2, '0')}:00`;

    cy.get('input[name="date"]').type(dateString);
    cy.get('input[name="heure"]').type(heureString);

    cy.get('button[type="submit"]').contains('Réserver').click();

    cy.contains('Match créé avec succès', { timeout: 5000 }).should('be.visible');
  });
});
