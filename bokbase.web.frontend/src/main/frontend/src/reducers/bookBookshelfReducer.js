import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_BOOK,
    BOOK_BOOKSHELF_SELECT,
    CLEAR_BOOK_DATA,
} from '../reduxactions';
import { nullWhenFalsy } from './reducerCommon';

const defaultValue=null;

const bookBookshelfReducer = createReducer(defaultValue, {
    [SELECTED_BOOK]: (state, action) => action.payload.bookshelf,
    [BOOK_BOOKSHELF_SELECT]: (state, action) => nullWhenFalsy(action.payload),
    [CLEAR_BOOK_DATA]: () => defaultValue,
});

export default bookBookshelfReducer;
