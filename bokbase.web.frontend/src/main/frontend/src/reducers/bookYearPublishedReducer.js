import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_BOOK,
    BOOK_YEAR_PUBLISHED_MODIFY,
    CLEAR_BOOK_DATA,
} from '../reduxactions';
import { parseIntWhenThruthyOrNullWhenFalsy } from './reducerCommon';

const defaultValue=null;

const bookYearPublishedReducer = createReducer(defaultValue, {
    [SELECTED_BOOK]: (state, action) => action.payload.yearPublished,
    [BOOK_YEAR_PUBLISHED_MODIFY]: (state, action) => parseIntWhenThruthyOrNullWhenFalsy(action.payload),
    [CLEAR_BOOK_DATA]: () => defaultValue,
});

export default bookYearPublishedReducer;
