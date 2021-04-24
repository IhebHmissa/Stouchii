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

import { IHistoryLine, defaultValue } from 'app/shared/model/history-line.model';

export const ACTION_TYPES = {
  FETCH_HISTORYLINE_LIST: 'historyLine/FETCH_HISTORYLINE_LIST',
  FETCH_HISTORYLINE: 'historyLine/FETCH_HISTORYLINE',
  CREATE_HISTORYLINE: 'historyLine/CREATE_HISTORYLINE',
  UPDATE_HISTORYLINE: 'historyLine/UPDATE_HISTORYLINE',
  PARTIAL_UPDATE_HISTORYLINE: 'historyLine/PARTIAL_UPDATE_HISTORYLINE',
  DELETE_HISTORYLINE: 'historyLine/DELETE_HISTORYLINE',
  RESET: 'historyLine/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IHistoryLine>,
  entity: defaultValue,
  links: { next: 0 },
  updating: false,
  totalItems: 0,
  updateSuccess: false,
};

export type HistoryLineState = Readonly<typeof initialState>;

// Reducer

export default (state: HistoryLineState = initialState, action): HistoryLineState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_HISTORYLINE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_HISTORYLINE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_HISTORYLINE):
    case REQUEST(ACTION_TYPES.UPDATE_HISTORYLINE):
    case REQUEST(ACTION_TYPES.DELETE_HISTORYLINE):
    case REQUEST(ACTION_TYPES.PARTIAL_UPDATE_HISTORYLINE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_HISTORYLINE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_HISTORYLINE):
    case FAILURE(ACTION_TYPES.CREATE_HISTORYLINE):
    case FAILURE(ACTION_TYPES.UPDATE_HISTORYLINE):
    case FAILURE(ACTION_TYPES.PARTIAL_UPDATE_HISTORYLINE):
    case FAILURE(ACTION_TYPES.DELETE_HISTORYLINE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_HISTORYLINE_LIST): {
      const links = parseHeaderForLinks(action.payload.headers.link);

      return {
        ...state,
        loading: false,
        links,
        entities: loadMoreDataWhenScrolled(state.entities, action.payload.data, links),
        totalItems: parseInt(action.payload.headers['x-total-count'], 10),
      };
    }
    case SUCCESS(ACTION_TYPES.FETCH_HISTORYLINE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_HISTORYLINE):
    case SUCCESS(ACTION_TYPES.UPDATE_HISTORYLINE):
    case SUCCESS(ACTION_TYPES.PARTIAL_UPDATE_HISTORYLINE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_HISTORYLINE):
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

const apiUrl = 'api/history-lines';

// Actions

export const getEntities: ICrudGetAllAction<IHistoryLine> = (page, size, sort) => {
  const requestUrl = `${apiUrl}${sort ? `?page=${page}&size=${size}&sort=${sort}` : ''}`;
  return {
    type: ACTION_TYPES.FETCH_HISTORYLINE_LIST,
    payload: axios.get<IHistoryLine>(`${requestUrl}${sort ? '&' : '?'}cacheBuster=${new Date().getTime()}`),
  };
};

export const getEntity: ICrudGetAction<IHistoryLine> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_HISTORYLINE,
    payload: axios.get<IHistoryLine>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IHistoryLine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_HISTORYLINE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const updateEntity: ICrudPutAction<IHistoryLine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_HISTORYLINE,
    payload: axios.put(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const partialUpdate: ICrudPutAction<IHistoryLine> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.PARTIAL_UPDATE_HISTORYLINE,
    payload: axios.patch(`${apiUrl}/${entity.id}`, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IHistoryLine> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_HISTORYLINE,
    payload: axios.delete(requestUrl),
  });
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
