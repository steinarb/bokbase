import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_BOOK,
    BOOK_TITLE_MODIFY,
    CLEAR_BOOK_DATA,
} from '../reduxactions';
import { nullWhenFalsy } from './reducerCommon';

const defaultValue=null;

const bookTitleReducer = createReducer(defaultValue, {
    [SELECTED_BOOK]: (state, action) => action.payload.title,
    [BOOK_TITLE_MODIFY]: (state, action) => nullWhenFalsy(action.payload),
    [CLEAR_BOOK_DATA]: () => defaultValue,
});

export default bookTitleReducer;
