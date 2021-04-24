import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './periode.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPeriodeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PeriodeDetail = (props: IPeriodeDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { periodeEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="periodeDetailsHeading">Periode</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{periodeEntity.id}</dd>
          <dt>
            <span id="dateDeb">Date Deb</span>
          </dt>
          <dd>{periodeEntity.dateDeb ? <TextFormat value={periodeEntity.dateDeb} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="dateFin">Date Fin</span>
          </dt>
          <dd>{periodeEntity.dateFin ? <TextFormat value={periodeEntity.dateFin} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="frequancy">Frequancy</span>
          </dt>
          <dd>{periodeEntity.frequancy}</dd>
          <dt>
            <span id="fixedMontant">Fixed Montant</span>
          </dt>
          <dd>{periodeEntity.fixedMontant}</dd>
          <dt>
            <span id="numberleft">Numberleft</span>
          </dt>
          <dd>{periodeEntity.numberleft}</dd>
          <dt>
            <span id="typeCatego">Type Catego</span>
          </dt>
          <dd>{periodeEntity.typeCatego}</dd>
        </dl>
        <Button tag={Link} to="/periode" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/periode/${periodeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ periode }: IRootState) => ({
  periodeEntity: periode.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PeriodeDetail);
