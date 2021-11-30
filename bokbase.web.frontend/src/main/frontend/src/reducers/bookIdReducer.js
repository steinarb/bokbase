import { createReducer } from '@reduxjs/toolkit';
import { SELECT_BOOK } from '../reduxactions';
import { parseIntWhenThruthyOrNullWhenFalsy } from './reducerCommon';

const bookIdReducer = createReducer(null, {
    [SELECT_BOOK]: (state, action) => parseIntWhenThruthyOrNullWhenFalsy(action.payload),
});

export default bookIdReducer;
