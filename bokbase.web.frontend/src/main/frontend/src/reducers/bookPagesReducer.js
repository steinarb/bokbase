import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_BOOK,
    BOOK_PAGES_MODIFY,
    CLEAR_BOOK_DATA,
} from '../reduxactions';
import { parseIntWhenThruthyOrNullWhenFalsy } from './reducerCommon';

const defaultValue=null;

const bookPagesReducer = createReducer(defaultValue, {
    [SELECTED_BOOK]: (state, action) => action.payload.pages,
    [BOOK_PAGES_MODIFY]: (state, action) => parseIntWhenThruthyOrNullWhenFalsy(action.payload),
    [CLEAR_BOOK_DATA]: () => defaultValue,
});

export default bookPagesReducer;
