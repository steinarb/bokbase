import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_BOOK,
    BOOK_YEAR_READ_MODIFY,
    CLEAR_BOOK_DATA,
} from '../reduxactions';
import { parseIntWhenThruthyOrNullWhenFalsy } from './reducerCommon';

const defaultValue=null;

const bookYearReadReducer = createReducer(defaultValue, {
    [SELECTED_BOOK]: (state, action) => action.payload.yearRead,
    [BOOK_YEAR_READ_MODIFY]: (state, action) => parseIntWhenThruthyOrNullWhenFalsy(action.payload),
    [CLEAR_BOOK_DATA]: () => defaultValue,
});

export default bookYearReadReducer;
