import { createReducer } from '@reduxjs/toolkit';
import {
    BOOKS_RECEIVE,
    BOOK_UPDATE_RECEIVE,
    BOOK_REMOVE_RECEIVE,
    BOOK_ADD_RECEIVE,
} from '../reduxactions';

const booksReducer = createReducer([], {
    [BOOKS_RECEIVE]: (state, action) => action.payload,
    [BOOK_UPDATE_RECEIVE]: (state, action) => action.payload,
    [BOOK_REMOVE_RECEIVE]: (state, action) => action.payload,
    [BOOK_ADD_RECEIVE]: (state, action) => action.payload.books,
});

export default booksReducer;
