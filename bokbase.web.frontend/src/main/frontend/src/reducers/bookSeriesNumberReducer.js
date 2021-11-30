import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_BOOK,
    BOOK_SERIES_NUMBER_MODIFY,
    CLEAR_BOOK_DATA,
} from '../reduxactions';
import { parseFloatWhenThruthyOrNullWhenFalsy } from './reducerCommon';

const defaultValue=null;

const bookSeriesNumberReducer = createReducer(defaultValue, {
    [SELECTED_BOOK]: (state, action) => action.payload.seriesNumber,
    [BOOK_SERIES_NUMBER_MODIFY]: (state, action) => parseFloatWhenThruthyOrNullWhenFalsy(action.payload),
    [CLEAR_BOOK_DATA]: () => defaultValue,
});

export default bookSeriesNumberReducer;
