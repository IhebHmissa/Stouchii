import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './history-line.reducer';
import { IHistoryLine } from 'app/shared/model/history-line.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IHistoryLineUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const HistoryLineUpdate = (props: IHistoryLineUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { historyLineEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/history-line');
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
    values.dateModif = convertDateTimeToServer(values.dateModif);

    if (errors.length === 0) {
      const entity = {
        ...historyLineEntity,
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
          <h2 id="stouchiApp.historyLine.home.createOrEditLabel" data-cy="HistoryLineCreateUpdateHeading">
            Create or edit a HistoryLine
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : historyLineEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="history-line-id">ID</Label>
                  <AvInput id="history-line-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="categoryNameLabel" for="history-line-categoryName">
                  Category Name
                </Label>
                <AvField
                  id="history-line-categoryName"
                  data-cy="categoryName"
                  type="text"
                  name="categoryName"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="dateModifLabel" for="history-line-dateModif">
                  Date Modif
                </Label>
                <AvInput
                  id="history-line-dateModif"
                  data-cy="dateModif"
                  type="datetime-local"
                  className="form-control"
                  name="dateModif"
                  placeholder={'YYYY-MM-DD HH:mm'}
                  value={isNew ? displayDefaultDateTime() : convertDateTimeFromServer(props.historyLineEntity.dateModif)}
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="montantLabel" for="history-line-montant">
                  Montant
                </Label>
                <AvField
                  id="history-line-montant"
                  data-cy="montant"
                  type="string"
                  className="form-control"
                  name="montant"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    number: { value: true, errorMessage: 'This field should be a number.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="userLoginLabel" for="history-line-userLogin">
                  User Login
                </Label>
                <AvField id="history-line-userLogin" data-cy="userLogin" type="text" name="userLogin" />
              </AvGroup>
              <AvGroup>
                <Label id="noteLabel" for="history-line-note">
                  Note
                </Label>
                <AvField id="history-line-note" data-cy="note" type="text" name="note" />
              </AvGroup>
              <AvGroup>
                <Label id="typeCategoLabel" for="history-line-typeCatego">
                  Type Catego
                </Label>
                <AvField id="history-line-typeCatego" data-cy="typeCatego" type="text" name="typeCatego" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/history-line" replace color="info">
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
  historyLineEntity: storeState.historyLine.entity,
  loading: storeState.historyLine.loading,
  updating: storeState.historyLine.updating,
  updateSuccess: storeState.historyLine.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(HistoryLineUpdate);
