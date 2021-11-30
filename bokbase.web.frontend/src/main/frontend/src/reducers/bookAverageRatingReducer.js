import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_BOOK,
    BOOK_AVERAGE_RATING_MODIFY,
    CLEAR_BOOK_DATA,
} from '../reduxactions';
import { parseIntWhenThruthyOrNullWhenFalsy } from './reducerCommon';

const defaultValue=null;

const bookAverageRatingReducer = createReducer(defaultValue, {
    [SELECTED_BOOK]: (state, action) => action.payload.averageRating,
    [BOOK_AVERAGE_RATING_MODIFY]: (state, action) => parseIntWhenThruthyOrNullWhenFalsy(action.payload),
    [CLEAR_BOOK_DATA]: () => defaultValue,
});

export default bookAverageRatingReducer;
