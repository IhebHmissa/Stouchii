import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Objective from './objective';
import ObjectiveDetail from './objective-detail';
import ObjectiveUpdate from './objective-update';
import ObjectiveDeleteDialog from './objective-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ObjectiveUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ObjectiveUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ObjectiveDetail} />
      <ErrorBoundaryRoute path={match.url} component={Objective} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ObjectiveDeleteDialog} />
  </>
);

export default Routes;
