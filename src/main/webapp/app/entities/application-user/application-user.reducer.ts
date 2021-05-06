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

import { IApplicationUser, defaultValue } from 'app/shared/model/application-user.model';

export const ACTION_TYPES = {
  FETCH_APPLICATIONUSER_LIST: 'applicationUser/FETCH_APPLICATIONUSER_LIST',
  FETCH_APPLICATIONUSER: 'applicationUser/FETCH_APPLICATIONUSER',
  CREATE_APPLICATIONUSER: 'applicationUser/CREATE_APPLICATIONUSER',
  UPDATE_APPLICATIONUSER: 'applicationUser/UPDATE_APPLICATIONUSER',
  PARTIAL_UPDATE_APPLICATIONUSER: 'applicationUser/PARTIAL_UPDATE_APPLICATIONUSER',
  DELETE_APPLICATIONUSER: 'applicationUser/DELETE_APPLICATIONUSER',
  RESET: 'applicationUser/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IApplicationUser>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type ApplicationUserState = Readonly<typeof initialState>;

// Reducer

export default (state: ApplicationUserState = initialState, action): ApplicationUserState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_APPLICATIONUSER_LIST):
    case REQUEST(ACTION_TYPES.FETCH_APPLICATIONUSER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_APPLICATIONUSER):
    case REQUEST(ACTION_TYPES.UPDATE_APPLICATIONUSER):
    case REQUEST(ACTION_TYPES.DELETE_APPLICATIONUSER):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_APPLICATIONUSER):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_APPLICATIONUSER_LIST):
    case FAILURE(ACTION_TYPES.FETCH_APPLICATIONUSER):
    case FAILURE(ACTION_TYPES.CREATE_APPLICATIONUSER):
    case FAILURE(ACTION_TYPES.UPDATE_APPLICATIONUSER):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_APPLICATIONUSER):
    case FAILURE(ACTION_TYPES.DELETE_APPLICATIONUSER):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_APPLICATIONUSER_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_APPLICATIONUSER):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_APPLICATIONUSER):
    case SUCCESS(ACTION_TYPES.UPDATE_APPLICATIONUSER):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_APPLICATIONUSER):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_APPLICATIONUSER):
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

const apiUrl = 'api/application-users';

// Actions

export const getEntities: ICrudGetAllAction<IApplicationUser> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_APPLICATIONUSER_LIST,
    payload: axios.get<IApplicationUser>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IApplicationUser> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_APPLICATIONUSER,
    payload: axios.get<IApplicationUser>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IApplicationUser> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_APPLICATIONUSER,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const updateEntity: ICrudPutAction<IApplicationUser> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_APPLICATIONUSER,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IApplicationUser> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_APPLICATIONUSER,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IApplicationUser> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_APPLICATIONUSER,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
