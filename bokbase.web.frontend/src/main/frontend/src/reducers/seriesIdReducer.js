import { createReducer } from '@reduxjs/toolkit';
import { SELECT_SERIES } from '../reduxactions';
import { parseIntWhenThruthyOrNullWhenFalsy } from './reducerCommon';

const seriesIdReducer = createReducer(null, {
    [SELECT_SERIES]: (state, action) => parseIntWhenThruthyOrNullWhenFalsy(action.payload),
});

export default seriesIdReducer;
