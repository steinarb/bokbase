import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_BOOK,
    BOOK_AUTHOR_SELECT,
    CLEAR_BOOK_DATA,
} from '../reduxactions';
import { parseIntWhenThruthyOrNullWhenFalsy } from './reducerCommon';

const defaultValue=null;

const bookAuthorIdReducer = createReducer(defaultValue, {
    [SELECTED_BOOK]: (state, action) => action.payload.authorId,
    [BOOK_AUTHOR_SELECT]: (state, action) => parseIntWhenThruthyOrNullWhenFalsy(action.payload),
    [CLEAR_BOOK_DATA]: () => defaultValue,
});

export default bookAuthorIdReducer;
