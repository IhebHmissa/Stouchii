import axios from 'axios';
import {
  parseHeaderForLinks,
  loadMoreDataWhenScrolled,
  ICrudGetAction,
  ICrudGetAllAction,
  ICrudPutAction,
  ICrudDeleteAction,
} from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IObjective, defaultValue } from 'app/shared/model/objective.model';

export const ACTION_TYPES = {
  FETCH_OBJECTIVE_LIST: 'objective/FETCH_OBJECTIVE_LIST',
  FETCH_OBJECTIVE: 'objective/FETCH_OBJECTIVE',
  CREATE_OBJECTIVE: 'objective/CREATE_OBJECTIVE',
  UPDATE_OBJECTIVE: 'objective/UPDATE_OBJECTIVE',
  PARTIAL_UPDATE_OBJECTIVE: 'objective/PARTIAL_UPDATE_OBJECTIVE',
  DELETE_OBJECTIVE: 'objective/DELETE_OBJECTIVE',
  RESET: 'objective/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IObjective>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ObjectiveState = Readonly<typeof initialState>;

// Reducer

export default (state: ObjectiveState = initialState, action): ObjectiveState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_OBJECTIVE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_OBJECTIVE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_OBJECTIVE):
    case REQUEST(ACTION_TYPES.UPDATE_OBJECTIVE):
    case REQUEST(ACTION_TYPES.DELETE_OBJECTIVE):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_OBJECTIVE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_OBJECTIVE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_OBJECTIVE):
    case FAILURE(ACTION_TYPES.CREATE_OBJECTIVE):
    case FAILURE(ACTION_TYPES.UPDATE_OBJECTIVE):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_OBJECTIVE):
    case FAILURE(ACTION_TYPES.DELETE_OBJECTIVE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_OBJECTIVE_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_OBJECTIVE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_OBJECTIVE):
    case SUCCESS(ACTION_TYPES.UPDATE_OBJECTIVE):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_OBJECTIVE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_OBJECTIVE):
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

const apiUrl = 'api/objectives';

// Actions

export const getEntities: ICrudGetAllAction<IObjective> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_OBJECTIVE_LIST,
    payload: axios.get<IObjective>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IObjective> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_OBJECTIVE,
    payload: axios.get<IObjective>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IObjective> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_OBJECTIVE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const updateEntity: ICrudPutAction<IObjective> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_OBJECTIVE,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IObjective> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_OBJECTIVE,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IObjective> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_OBJECTIVE,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
