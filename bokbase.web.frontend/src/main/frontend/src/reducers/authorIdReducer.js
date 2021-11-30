import { createReducer } from '@reduxjs/toolkit';
import { SELECT_AUTHOR } from '../reduxactions';
import { parseIntWhenThruthyOrNullWhenFalsy } from './reducerCommon';

const authorIdReducer = createReducer(null, {
    [SELECT_AUTHOR]: (state, action) => parseIntWhenThruthyOrNullWhenFalsy(action.payload),
});

export default authorIdReducer;
