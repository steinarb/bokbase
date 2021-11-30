import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_PUBLISHER,
    PUBLISHER_NAME_MODIFY,
    CLEAR_PUBLISHER_DATA,
} from '../reduxactions';
import { nullWhenFalsy } from './reducerCommon';

const defaultValue=null;

const publisherNameReducer = createReducer(defaultValue, {
    [SELECTED_PUBLISHER]: (state, action) => action.payload ? action.payload.name : state,
    [PUBLISHER_NAME_MODIFY]: (state, action) => nullWhenFalsy(action.payload),
    [CLEAR_PUBLISHER_DATA]: () => defaultValue,
});

export default publisherNameReducer;
