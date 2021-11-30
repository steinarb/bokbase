import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_BOOK,
    BOOK_SERIES_SELECT,
    CLEAR_BOOK_DATA,
} from '../reduxactions';
import { parseIntWhenThruthyOrNullWhenFalsy } from './reducerCommon';

const defaultValue=null;

const bookSeriesIdReducer = createReducer(defaultValue, {
    [SELECTED_BOOK]: (state, action) => action.payload.seriesId,
    [BOOK_SERIES_SELECT]: (state, action) => parseIntWhenThruthyOrNullWhenFalsy(action.payload),
    [CLEAR_BOOK_DATA]: () => defaultValue,
});

export default bookSeriesIdReducer;
