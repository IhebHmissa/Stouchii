import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Periode from './periode';
import Category from './category';
import HistoryLine from './history-line';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}periode`} component={Periode} />
      <ErrorBoundaryRoute path={`${match.url}category`} component={Category} />
      <ErrorBoundaryRoute path={`${match.url}history-line`} component={HistoryLine} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
