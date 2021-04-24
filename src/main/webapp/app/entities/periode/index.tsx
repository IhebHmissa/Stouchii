import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Periode from './periode';
import PeriodeDetail from './periode-detail';
import PeriodeUpdate from './periode-update';
import PeriodeDeleteDialog from './periode-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={PeriodeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={PeriodeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={PeriodeDetail} />
      <ErrorBoundaryRoute path={match.url} component={Periode} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={PeriodeDeleteDialog} />
  </>
);

export default Routes;
