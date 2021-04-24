import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { getEntity, updateEntity, createEntity, reset } from './category.reducer';
import { ICategory } from 'app/shared/model/category.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ICategoryUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const CategoryUpdate = (props: ICategoryUpdateProps) => {
  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const { categoryEntity, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/category' + props.location.search);
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
    if (errors.length === 0) {
      const entity = {
        ...categoryEntity,
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
          <h2 id="stouchiApp.category.home.createOrEditLabel" data-cy="CategoryCreateUpdateHeading">
            Create or edit a Category
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : categoryEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="category-id">ID</Label>
                  <AvInput id="category-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="typeLabel" for="category-type">
                  Type
                </Label>
                <AvField
                  id="category-type"
                  data-cy="type"
                  type="text"
                  name="type"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="nameCategoLabel" for="category-nameCatego">
                  Name Catego
                </Label>
                <AvField id="category-nameCatego" data-cy="nameCatego" type="text" name="nameCatego" />
              </AvGroup>
              <AvGroup>
                <Label id="originTypeLabel" for="category-originType">
                  Origin Type
                </Label>
                <AvField
                  id="category-originType"
                  data-cy="originType"
                  type="text"
                  name="originType"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="montantLabel" for="category-montant">
                  Montant
                </Label>
                <AvField id="category-montant" data-cy="montant" type="string" className="form-control" name="montant" />
              </AvGroup>
              <AvGroup>
                <Label id="colorLabel" for="category-color">
                  Color
                </Label>
                <AvField
                  id="category-color"
                  data-cy="color"
                  type="text"
                  name="color"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                    maxLength: { value: 7, errorMessage: 'This field cannot be longer than 7 characters.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="userLoginLabel" for="category-userLogin">
                  User Login
                </Label>
                <AvField
                  id="category-userLogin"
                  data-cy="userLogin"
                  type="text"
                  name="userLogin"
                  validate={{
                    required: { value: true, errorMessage: 'This field is required.' },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="minMontantLabel" for="category-minMontant">
                  Min Montant
                </Label>
                <AvField id="category-minMontant" data-cy="minMontant" type="string" className="form-control" name="minMontant" />
              </AvGroup>
              <AvGroup>
                <Label id="maxMontantLabel" for="category-maxMontant">
                  Max Montant
                </Label>
                <AvField id="category-maxMontant" data-cy="maxMontant" type="string" className="form-control" name="maxMontant" />
              </AvGroup>
              <AvGroup>
                <Label id="periodictyLabel" for="category-periodicty">
                  Periodicty
                </Label>
                <AvField id="category-periodicty" data-cy="periodicty" type="text" name="periodicty" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/category" replace color="info">
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
  categoryEntity: storeState.category.entity,
  loading: storeState.category.loading,
  updating: storeState.category.updating,
  updateSuccess: storeState.category.updateSuccess,
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(CategoryUpdate);
