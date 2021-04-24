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

describe('Periode e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/periodes*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('periode');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Periodes', () => {
    cy.intercept('GET', '/api/periodes*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('periode');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Periode').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Periode page', () => {
    cy.intercept('GET', '/api/periodes*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('periode');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('periode');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Periode page', () => {
    cy.intercept('GET', '/api/periodes*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('periode');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Periode');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Periode page', () => {
    cy.intercept('GET', '/api/periodes*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('periode');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Periode');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of Periode', () => {
    cy.intercept('GET', '/api/periodes*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('periode');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Periode');

    cy.get(`[data-cy="dateDeb"]`).type('2021-04-22T17:44').invoke('val').should('equal', '2021-04-22T17:44');

    cy.get(`[data-cy="dateFin"]`).type('2021-04-23T03:24').invoke('val').should('equal', '2021-04-23T03:24');

    cy.get(`[data-cy="frequancy"]`).type('content-based', { force: true }).invoke('val').should('match', new RegExp('content-based'));

    cy.get(`[data-cy="fixedMontant"]`).type('96485').should('have.value', '96485');

    cy.get(`[data-cy="numberleft"]`).type('27242').should('have.value', '27242');

    cy.get(`[data-cy="typeCatego"]`).type('neural', { force: true }).invoke('val').should('match', new RegExp('neural'));

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/periodes*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('periode');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of Periode', () => {
    cy.intercept('GET', '/api/periodes*').as('entitiesRequest');
    cy.intercept('GET', '/api/periodes/*').as('dialogDeleteRequest');
    cy.intercept('DELETE', '/api/periodes/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('periode');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('periode').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/periodes*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('periode');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
