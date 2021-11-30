import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_BOOK,
    BOOK_PUBLISHER_SELECT,
    CLEAR_BOOK_DATA,
} from '../reduxactions';
import { parseIntWhenThruthyOrNullWhenFalsy } from './reducerCommon';

const defaultValue=null;

const bookPublisherIdReducer = createReducer(defaultValue, {
    [SELECTED_BOOK]: (state, action) => action.payload.publisherId,
    [BOOK_PUBLISHER_SELECT]: (state, action) => parseIntWhenThruthyOrNullWhenFalsy(action.payload),
    [CLEAR_BOOK_DATA]: () => defaultValue,
});

export default bookPublisherIdReducer;
