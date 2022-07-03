import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_BOOK,
    BOOK_FINISHED_READ_DATE_MODIFY,
    CLEAR_BOOK_DATA,
} from '../reduxactions';
import { transformJavaLocalDateToESDate } from './reducerCommon';

const initialValue = new Date().toISOString();

const bookFinishedReadDateReducer = createReducer(initialValue, {
    [SELECTED_BOOK]: (state, action) => transformJavaLocalDateToESDate(action.payload.finishedReadDate),
    [BOOK_FINISHED_READ_DATE_MODIFY]: (state, action) => action.payload + 'T' + state.split('T')[1],
    [CLEAR_BOOK_DATA]: () => new Date().toISOString(),
});

export default bookFinishedReadDateReducer;
