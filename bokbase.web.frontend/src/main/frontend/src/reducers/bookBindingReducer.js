import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_BOOK,
    BOOK_BINDING_MODIFY,
    CLEAR_BOOK_DATA,
} from '../reduxactions';
import { nullWhenFalsy } from './reducerCommon';

const defaultValue=null;

const bookBindingReducer = createReducer(defaultValue, {
    [SELECTED_BOOK]: (state, action) => action.payload.binding,
    [BOOK_BINDING_MODIFY]: (state, action) => nullWhenFalsy(action.payload),
    [CLEAR_BOOK_DATA]: () => defaultValue,
});

export default bookBindingReducer;
