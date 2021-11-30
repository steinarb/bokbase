import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_AUTHOR,
    AUTHOR_LASTNAME_MODIFY,
    CLEAR_AUTHOR_DATA,
} from '../reduxactions';
import { nullWhenFalsy } from './reducerCommon';

const defaultValue=null;

const authorLastnameReducer = createReducer(defaultValue, {
    [SELECTED_AUTHOR]: (state, action) => action.payload ? action.payload.lastname : state,
    [AUTHOR_LASTNAME_MODIFY]: (state, action) => nullWhenFalsy(action.payload),
    [CLEAR_AUTHOR_DATA]: () => defaultValue,
});

export default authorLastnameReducer;
