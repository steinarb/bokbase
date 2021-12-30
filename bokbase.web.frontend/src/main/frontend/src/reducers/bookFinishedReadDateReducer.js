import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_BOOK,
    BOOK_FINISHED_READ_DATE_MODIFY,
    CLEAR_BOOK_DATA,
} from '../reduxactions';
import { transformJavaLocalDateToESDate } from './reducerCommon';

const defaultValue=null;

const bookFinishedReadDateReducer = createReducer(defaultValue, {
    [SELECTED_BOOK]: (state, action) => transformJavaLocalDateToESDate(action.payload.finishedReadDate),
    [BOOK_FINISHED_READ_DATE_MODIFY]: (state, action) => action.payload,
    [CLEAR_BOOK_DATA]: () => defaultValue,
});

export default bookFinishedReadDateReducer;
