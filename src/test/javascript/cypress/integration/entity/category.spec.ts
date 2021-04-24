import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Category e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/categories*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('category');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Categories', () => {
    cy.intercept('GET', '/api/categories*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('category');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Category').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Category page', () => {
    cy.intercept('GET', '/api/categories*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('category');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('category');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Category page', () => {
    cy.intercept('GET', '/api/categories*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('category');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Category');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Category page', () => {
    cy.intercept('GET', '/api/categories*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('category');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Category');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of Category', () => {
    cy.intercept('GET', '/api/categories*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('category');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Category');

    cy.get(`[data-cy="type"]`).type('Graphic Jewelery', { force: true }).invoke('val').should('match', new RegExp('Graphic Jewelery'));

    cy.get(`[data-cy="nameCatego"]`)
      .type('grey clicks-and-mortar', { force: true })
      .invoke('val')
      .should('match', new RegExp('grey clicks-and-mortar'));

    cy.get(`[data-cy="originType"]`).type('Cotton', { force: true }).invoke('val').should('match', new RegExp('Cotton'));

    cy.get(`[data-cy="montant"]`).type('70953').should('have.value', '70953');

    cy.get(`[data-cy="color"]`).type('olive', { force: true }).invoke('val').should('match', new RegExp('olive'));

    cy.get(`[data-cy="userLogin"]`).type('Response frame', { force: true }).invoke('val').should('match', new RegExp('Response frame'));

    cy.get(`[data-cy="minMontant"]`).type('47845').should('have.value', '47845');

    cy.get(`[data-cy="maxMontant"]`).type('33438').should('have.value', '33438');

    cy.get(`[data-cy="periodicty"]`).type('innovate', { force: true }).invoke('val').should('match', new RegExp('innovate'));

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/categories*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('category');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of Category', () => {
    cy.intercept('GET', '/api/categories*').as('entitiesRequest');
    cy.intercept('GET', '/api/categories/*').as('dialogDeleteRequest');
    cy.intercept('DELETE', '/api/categories/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('category');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('category').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/categories*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('category');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
