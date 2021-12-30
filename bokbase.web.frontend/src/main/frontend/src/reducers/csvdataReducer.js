import { createReducer } from '@reduxjs/toolkit';
import {
    BOOKS_RECEIVE,
    BOOK_UPDATE_RECEIVE,
    BOOK_REMOVE_RECEIVE,
    BOOK_ADD_RECEIVE,
} from '../reduxactions';
import { formatLocalDate } from './reducerCommon';

const csvdataReducer = createReducer([], {
    [BOOKS_RECEIVE]: (state, action) => transformToCsv(action.payload),
    [BOOK_UPDATE_RECEIVE]: (state, action) => transformToCsv(action.payload),
    [BOOK_REMOVE_RECEIVE]: (state, action) => transformToCsv(action.payload),
    [BOOK_ADD_RECEIVE]: (state, action) => transformToCsv(action.payload),
});

export default csvdataReducer;

function transformToCsv(books) {
    const rowsOfArrays = books.map(b => {
        const dateAdded = formatLocalDate(b.publishedDate);
        const dateFinished = formatLocalDate(b.finishedReadDate);
        return [
            b.bookId,
            b.title,
            b.authorName,
            b.isbn13,
            b.rating,
            b.bookshelf,
            b.review,
            dateAdded,
            dateFinished,
        ];
    });

    return [
        [
            'id',
            'title',
            'author',
            'ISBN13',
            'rating',
            'shelf',
            'review',
            'added',
            'finished',
        ],
        ...rowsOfArrays
    ];
}
