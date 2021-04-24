import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './category.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICategoryDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CategoryDetail = (props: ICategoryDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { categoryEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="categoryDetailsHeading">Category</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{categoryEntity.id}</dd>
          <dt>
            <span id="type">Type</span>
          </dt>
          <dd>{categoryEntity.type}</dd>
          <dt>
            <span id="nameCatego">Name Catego</span>
          </dt>
          <dd>{categoryEntity.nameCatego}</dd>
          <dt>
            <span id="originType">Origin Type</span>
          </dt>
          <dd>{categoryEntity.originType}</dd>
          <dt>
            <span id="montant">Montant</span>
          </dt>
          <dd>{categoryEntity.montant}</dd>
          <dt>
            <span id="color">Color</span>
          </dt>
          <dd>{categoryEntity.color}</dd>
          <dt>
            <span id="userLogin">User Login</span>
          </dt>
          <dd>{categoryEntity.userLogin}</dd>
          <dt>
            <span id="minMontant">Min Montant</span>
          </dt>
          <dd>{categoryEntity.minMontant}</dd>
          <dt>
            <span id="maxMontant">Max Montant</span>
          </dt>
          <dd>{categoryEntity.maxMontant}</dd>
          <dt>
            <span id="periodicty">Periodicty</span>
          </dt>
          <dd>{categoryEntity.periodicty}</dd>
        </dl>
        <Button tag={Link} to="/category" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/category/${categoryEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ category }: IRootState) => ({
  categoryEntity: category.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CategoryDetail);
