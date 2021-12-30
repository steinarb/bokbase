import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_BOOK,
    BOOK_PUBLISHED_DATE_MODIFY,
    CLEAR_BOOK_DATA,
} from '../reduxactions';
import { transformJavaLocalDateToESDate } from './reducerCommon';

const defaultValue=null;

const bookPublishedDateReducer = createReducer(defaultValue, {
    [SELECTED_BOOK]: (state, action) => transformJavaLocalDateToESDate(action.payload.publishedDate),
    [BOOK_PUBLISHED_DATE_MODIFY]: (state, action) => action.payload,
    [CLEAR_BOOK_DATA]: () => defaultValue,
});

export default bookPublishedDateReducer;
