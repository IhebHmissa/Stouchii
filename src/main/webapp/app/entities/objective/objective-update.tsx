import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './objective.reducer';
import { IObjective } from 'app/shared/model/objective.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IObjectiveUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ObjectiveUpdate = (props: IObjectiveUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { objectiveEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/objective');
  };

  useEffect(() => {
    if (!isNew) {
      props.getEntity(props.match.params.id);
    }
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...objectiveEntity,
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
          <h2 id="stouchiApp.objective.home.createOrEditLabel" data-cy="ObjectiveCreateUpdateHeading">
            Create or edit a Objective
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : objectiveEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="objective-id">ID</Label>
                  <AvInput id="objective-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="nameLabel" for="objective-name">
                  Name
                </Label>
                <AvField id="objective-name" data-cy="name" type="text" name="name" />
              </AvGroup>
              <AvGroup>
                <Label id="noteLabel" for="objective-note">
                  Note
                </Label>
                <AvField id="objective-note" data-cy="note" type="text" name="note" />
              </AvGroup>
              <AvGroup>
                <Label id="userLoginLabel" for="objective-userLogin">
                  User Login
                </Label>
                <AvField id="objective-userLogin" data-cy="userLogin" type="text" name="userLogin" />
              </AvGroup>
              <AvGroup>
                <Label id="amountTotLabel" for="objective-amountTot">
                  Amount Tot
                </Label>
                <AvField id="objective-amountTot" data-cy="amountTot" type="string" className="form-control" name="amountTot" />
              </AvGroup>
              <AvGroup>
                <Label id="amountVarLabel" for="objective-amountVar">
                  Amount Var
                </Label>
                <AvField id="objective-amountVar" data-cy="amountVar" type="string" className="form-control" name="amountVar" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/objective" replace color="info">
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
  objectiveEntity: storeState.objective.entity,
  loading: storeState.objective.loading,
  updating: storeState.objective.updating,
  updateSuccess: storeState.objective.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ObjectiveUpdate);
