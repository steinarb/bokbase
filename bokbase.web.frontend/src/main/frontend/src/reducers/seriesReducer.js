import { createReducer } from '@reduxjs/toolkit';
import {
    SERIES_RECEIVE,
    SERIES_UPDATE_RECEIVE,
    SERIES_REMOVE_RECEIVE,
    SERIES_ADD_RECEIVE,
} from '../reduxactions';

const seriesReducer = createReducer([], {
    [SERIES_RECEIVE]: (state, action) => action.payload,
    [SERIES_UPDATE_RECEIVE]: (state, action) => action.payload,
    [SERIES_REMOVE_RECEIVE]: (state, action) => action.payload,
    [SERIES_ADD_RECEIVE]: (state, action) => action.payload.series,
});

export default seriesReducer;
