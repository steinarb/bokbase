import { createReducer } from '@reduxjs/toolkit';
import { SELECT_PUBLISHER } from '../reduxactions';
import { parseIntWhenThruthyOrNullWhenFalsy } from './reducerCommon';

const publisherIdReducer = createReducer(null, {
    [SELECT_PUBLISHER]: (state, action) => parseIntWhenThruthyOrNullWhenFalsy(action.payload),
});

export default publisherIdReducer;
