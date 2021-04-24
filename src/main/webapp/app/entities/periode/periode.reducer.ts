import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPeriode, defaultValue } from 'app/shared/model/periode.model';

export const ACTION_TYPES = {
  FETCH_PERIODE_LIST: 'periode/FETCH_PERIODE_LIST',
  FETCH_PERIODE: 'periode/FETCH_PERIODE',
  CREATE_PERIODE: 'periode/CREATE_PERIODE',
  UPDATE_PERIODE: 'periode/UPDATE_PERIODE',
  PARTIAL_UPDATE_PERIODE: 'periode/PARTIAL_UPDATE_PERIODE',
  DELETE_PERIODE: 'periode/DELETE_PERIODE',
  RESET: 'periode/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPeriode>,
  entity: defaultValue,
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type PeriodeState = Readonly<typeof initialState>;

// Reducer

export default (state: PeriodeState = initialState, action): PeriodeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PERIODE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PERIODE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PERIODE):
    case REQUEST(ACTION_TYPES.UPDATE_PERIODE):
    case REQUEST(ACTION_TYPES.DELETE_PERIODE):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_PERIODE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PERIODE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PERIODE):
    case FAILURE(ACTION_TYPES.CREATE_PERIODE):
    case FAILURE(ACTION_TYPES.UPDATE_PERIODE):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_PERIODE):
    case FAILURE(ACTION_TYPES.DELETE_PERIODE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PERIODE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    case SUCCESS(ACTION_TYPES.FETCH_PERIODE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PERIODE):
    case SUCCESS(ACTION_TYPES.UPDATE_PERIODE):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_PERIODE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PERIODE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/periodes';

// Actions

export const getEntities: ICrudGetAllAction<IPeriode> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_PERIODE_LIST,
    payload: axios.get<IPeriode>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IPeriode> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PERIODE,
    payload: axios.get<IPeriode>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IPeriode> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PERIODE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPeriode> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PERIODE,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IPeriode> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_PERIODE,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPeriode> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PERIODE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
