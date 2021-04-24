import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './periode.reducer';
import { IPeriode } from 'app/shared/model/periode.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IPeriodeUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const PeriodeUpdate = (props: IPeriodeUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { periodeEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/periode' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    values.dateDeb = convertDateTimeToServer(values.dateDeb);
    values.dateFin = convertDateTimeToServer(values.dateFin);

    if (errors.length === 0) {
      const entity = {
        ...periodeEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="stouchiApp.periode.home.createOrEditLabel" data-cy="PeriodeCreateUpdateHeading">
            Create or edit a Periode
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : periodeEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="periode-id">ID</Label>
                  <AvInput id="periode-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="dateDebLabel" for="periode-dateDeb">
                  Date Deb
                </Label>
                <AvInput
                  id="periode-dateDeb"
                  data-cy="dateDeb"
                  type="datetime-local"
                  className="form-control"
                  name="dateDeb"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.periodeEntity.dateDeb)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="dateFinLabel" for="periode-dateFin">
                  Date Fin
                </Label>
                <AvInput
                  id="periode-dateFin"
                  data-cy="dateFin"
                  type="datetime-local"
                  className="form-control"
                  name="dateFin"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.periodeEntity.dateFin)}
                />
              </AvGroup>
              <AvGroup>
                <Label id="frequancyLabel" for="periode-frequancy">
                  Frequancy
                </Label>
                <AvField id="periode-frequancy" data-cy="frequancy" type="text" name="frequancy" />
              </AvGroup>
              <AvGroup>
                <Label id="fixedMontantLabel" for="periode-fixedMontant">
                  Fixed Montant
                </Label>
                <AvField id="periode-fixedMontant" data-cy="fixedMontant" type="string" className="form-control" name="fixedMontant" />
              </AvGroup>
              <AvGroup>
                <Label id="numberleftLabel" for="periode-numberleft">
                  Numberleft
                </Label>
                <AvField id="periode-numberleft" data-cy="numberleft" type="string" className="form-control" name="numberleft" />
              </AvGroup>
              <AvGroup>
                <Label id="typeCategoLabel" for="periode-typeCatego">
                  Type Catego
                </Label>
                <AvField id="periode-typeCatego" data-cy="typeCatego" type="text" name="typeCatego" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/periode" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  periodeEntity: storeState.periode.entity,
  loading: storeState.periode.loading,
  updating: storeState.periode.updating,
  updateSuccess: storeState.periode.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(PeriodeUpdate);
