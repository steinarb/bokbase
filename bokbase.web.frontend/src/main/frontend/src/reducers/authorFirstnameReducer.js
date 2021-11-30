import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_AUTHOR,
    AUTHOR_FIRSTNAME_MODIFY,
    CLEAR_AUTHOR_DATA,
} from '../reduxactions';
import { nullWhenFalsy } from './reducerCommon';

const defaultValue=null;

const authorFirstnameReducer = createReducer(defaultValue, {
    [SELECTED_AUTHOR]: (state, action) => action.payload ? action.payload.firstname : state,
    [AUTHOR_FIRSTNAME_MODIFY]: (state, action) => nullWhenFalsy(action.payload),
    [CLEAR_AUTHOR_DATA]: () => defaultValue,
});

export default authorFirstnameReducer;
