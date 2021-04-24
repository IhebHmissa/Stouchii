import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './history-line.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IHistoryLineDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const HistoryLineDetail = (props: IHistoryLineDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { historyLineEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="historyLineDetailsHeading">HistoryLine</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{historyLineEntity.id}</dd>
          <dt>
            <span id="categoryName">Category Name</span>
          </dt>
          <dd>{historyLineEntity.categoryName}</dd>
          <dt>
            <span id="dateModif">Date Modif</span>
          </dt>
          <dd>
            {historyLineEntity.dateModif ? <TextFormat value={historyLineEntity.dateModif} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="montant">Montant</span>
          </dt>
          <dd>{historyLineEntity.montant}</dd>
          <dt>
            <span id="userLogin">User Login</span>
          </dt>
          <dd>{historyLineEntity.userLogin}</dd>
          <dt>
            <span id="note">Note</span>
          </dt>
          <dd>{historyLineEntity.note}</dd>
          <dt>
            <span id="typeCatego">Type Catego</span>
          </dt>
          <dd>{historyLineEntity.typeCatego}</dd>
        </dl>
        <Button tag={Link} to="/history-line" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/history-line/${historyLineEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ historyLine }: IRootState) => ({
  historyLineEntity: historyLine.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HistoryLineDetail);
