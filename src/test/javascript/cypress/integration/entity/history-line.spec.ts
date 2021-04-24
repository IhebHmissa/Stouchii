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

describe('HistoryLine e2e test', () => {
  let startingEntitiesCount = 0;

  before(() => {
    cy.window().then(win => {
      win.sessionStorage.clear();
    });

    cy.clearCookies();
    cy.intercept('GET', '/api/history-lines*').as('entitiesRequest');
    cy.visit('');
    cy.login('admin', 'admin');
    cy.clickOnEntityMenuItem('history-line');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.visit('/');
  });

  it('should load HistoryLines', () => {
    cy.intercept('GET', '/api/history-lines*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('history-line');
    cy.wait('@entitiesRequest');
    cy.getEntityHeading('HistoryLine').should('exist');
    if (startingEntitiesCount === 0) {
      cy.get(entityTableSelector).should('not.exist');
    } else {
      cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
    }
    cy.visit('/');
  });

  it('should load details HistoryLine page', () => {
    cy.intercept('GET', '/api/history-lines*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('history-line');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityDetailsButtonSelector).first().click({ force: true });
      cy.getEntityDetailsHeading('historyLine');
      cy.get(entityDetailsBackButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should load create HistoryLine page', () => {
    cy.intercept('GET', '/api/history-lines*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('history-line');
    cy.wait('@entitiesRequest');
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('HistoryLine');
    cy.get(entityCreateSaveButtonSelector).should('exist');
    cy.visit('/');
  });

  it('should load edit HistoryLine page', () => {
    cy.intercept('GET', '/api/history-lines*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('history-line');
    cy.wait('@entitiesRequest');
    if (startingEntitiesCount > 0) {
      cy.get(entityEditButtonSelector).first().click({ force: true });
      cy.getEntityCreateUpdateHeading('HistoryLine');
      cy.get(entityCreateSaveButtonSelector).should('exist');
    }
    cy.visit('/');
  });

  it('should create an instance of HistoryLine', () => {
    cy.intercept('GET', '/api/history-lines*').as('entitiesRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('history-line');
    cy.wait('@entitiesRequest').then(({ request, response }) => (startingEntitiesCount = response.body.length));
    cy.get(entityCreateButtonSelector).click({ force: true });
    cy.getEntityCreateUpdateHeading('HistoryLine');

    cy.get(`[data-cy="categoryName"]`).type('Steel', { force: true }).invoke('val').should('match', new RegExp('Steel'));

    cy.get(`[data-cy="dateModif"]`).type('2021-04-23T14:07').invoke('val').should('equal', '2021-04-23T14:07');

    cy.get(`[data-cy="montant"]`).type('68114').should('have.value', '68114');

    cy.get(`[data-cy="userLogin"]`).type('Rustic Granite', { force: true }).invoke('val').should('match', new RegExp('Rustic Granite'));

    cy.get(`[data-cy="note"]`)
      .type('architecture Beauty', { force: true })
      .invoke('val')
      .should('match', new RegExp('architecture Beauty'));

    cy.get(`[data-cy="typeCatego"]`)
      .type('Accountability Factors', { force: true })
      .invoke('val')
      .should('match', new RegExp('Accountability Factors'));

    cy.get(entityCreateSaveButtonSelector).click({ force: true });
    cy.scrollTo('top', { ensureScrollable: false });
    cy.get(entityCreateSaveButtonSelector).should('not.exist');
    cy.intercept('GET', '/api/history-lines*').as('entitiesRequestAfterCreate');
    cy.visit('/');
    cy.clickOnEntityMenuItem('history-line');
    cy.wait('@entitiesRequestAfterCreate');
    cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount + 1);
    cy.visit('/');
  });

  it('should delete last instance of HistoryLine', () => {
    cy.intercept('GET', '/api/history-lines*').as('entitiesRequest');
    cy.intercept('GET', '/api/history-lines/*').as('dialogDeleteRequest');
    cy.intercept('DELETE', '/api/history-lines/*').as('deleteEntityRequest');
    cy.visit('/');
    cy.clickOnEntityMenuItem('history-line');
    cy.wait('@entitiesRequest').then(({ request, response }) => {
      startingEntitiesCount = response.body.length;
      if (startingEntitiesCount > 0) {
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount);
        cy.get(entityDeleteButtonSelector).last().click({ force: true });
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('historyLine').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click({ force: true });
        cy.wait('@deleteEntityRequest');
        cy.intercept('GET', '/api/history-lines*').as('entitiesRequestAfterDelete');
        cy.visit('/');
        cy.clickOnEntityMenuItem('history-line');
        cy.wait('@entitiesRequestAfterDelete');
        cy.get(entityTableSelector).should('have.lengthOf', startingEntitiesCount - 1);
      }
      cy.visit('/');
    });
  });
});
