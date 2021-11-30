import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_BOOK,
    BOOK_MONTH_READ_MODIFY,
    CLEAR_BOOK_DATA,
} from '../reduxactions';
import { parseIntWhenThruthyOrNullWhenFalsy } from './reducerCommon';

const defaultValue=null;

const bookMonthReadReducer = createReducer(defaultValue, {
    [SELECTED_BOOK]: (state, action) => action.payload.monthRead,
    [BOOK_MONTH_READ_MODIFY]: (state, action) => parseIntWhenThruthyOrNullWhenFalsy(action.payload),
    [CLEAR_BOOK_DATA]: () => defaultValue,
});

export default bookMonthReadReducer;
