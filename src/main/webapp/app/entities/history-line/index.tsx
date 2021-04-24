import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import HistoryLine from './history-line';
import HistoryLineDetail from './history-line-detail';
import HistoryLineUpdate from './history-line-update';
import HistoryLineDeleteDialog from './history-line-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={HistoryLineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={HistoryLineUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={HistoryLineDetail} />
      <ErrorBoundaryRoute path={match.url} component={HistoryLine} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={HistoryLineDeleteDialog} />
  </>
);

export default Routes;
