import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_BOOK,
    BOOK_ISBN13_MODIFY,
    CLEAR_BOOK_DATA,
} from '../reduxactions';
import { nullWhenFalsy } from './reducerCommon';

const defaultValue=null;

const bookIsbn13Reducer = createReducer(defaultValue, {
    [SELECTED_BOOK]: (state, action) => action.payload.isbn13,
    [BOOK_ISBN13_MODIFY]: (state, action) => nullWhenFalsy(action.payload),
    [CLEAR_BOOK_DATA]: () => defaultValue,
});

export default bookIsbn13Reducer;
