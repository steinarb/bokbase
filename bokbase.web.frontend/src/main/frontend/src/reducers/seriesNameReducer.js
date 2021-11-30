import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_SERIES,
    SERIES_NAME_MODIFY,
    CLEAR_SERIES_DATA,
} from '../reduxactions';
import { nullWhenFalsy } from './reducerCommon';

const defaultValue=null;

const seriesNameReducer = createReducer(defaultValue, {
    [SELECTED_SERIES]: (state, action) => action.payload ? action.payload.name : state,
    [SERIES_NAME_MODIFY]: (state, action) => nullWhenFalsy(action.payload),
    [CLEAR_SERIES_DATA]: () => defaultValue,
});

export default seriesNameReducer;
