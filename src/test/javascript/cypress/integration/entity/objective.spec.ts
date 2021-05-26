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

describe('Objective e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/objectives*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('objective');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load Objectives', () => {
    cy.intercept('GET', '/api/objectives*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('objective');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('Objective').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details Objective page', () => {
    cy.intercept('GET', '/api/objectives*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('objective');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('objective');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create Objective page', () => {
    cy.intercept('GET', '/api/objectives*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('objective');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Objective');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit Objective page', () => {
    cy.intercept('GET', '/api/objectives*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('objective');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('Objective');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of Objective', () => {
    cy.intercept('GET', '/api/objectives*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('objective');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('Objective');

    cy.get(`[data-cy="name"]`)
      .type('New needs-based Gloves', { force: true })
      .invoke('val')
      .should('match', new RegExp('New needs-based Gloves'));

    cy.get(`[data-cy="note"]`).type('convergence', { force: true }).invoke('val').should('match', new RegExp('convergence'));

    cy.get(`[data-cy="userLogin"]`)
      .type('Synchronised pink Refined', { force: true })
      .invoke('val')
      .should('match', new RegExp('Synchronised pink Refined'));

    cy.get(`[data-cy="amountTot"]`).type('26843').should('have.value', '26843');

    cy.get(`[data-cy="amountVar"]`).type('89253').should('have.value', '89253');

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/objectives*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('objective');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of Objective', () => {
    cy.intercept('GET', '/api/objectives*').as('entitiesRequest');
    cy.intercept('GET', '/api/objectives/*').as('dialogDeleteRequest');
    cy.intercept('DELETE', '/api/objectives/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('objective');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('objective').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/objectives*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('objective');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
