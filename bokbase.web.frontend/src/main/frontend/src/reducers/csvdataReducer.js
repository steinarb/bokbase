import { createReducer } from '@reduxjs/toolkit';
import {
    BOOKS_RECEIVE,
    BOOK_UPDATE_RECEIVE,
    BOOK_REMOVE_RECEIVE,
    BOOK_ADD_RECEIVE,
} from '../reduxactions';

const csvdataReducer = createReducer([], {
    [BOOKS_RECEIVE]: (state, action) => transformToCsv(action.payload),
    [BOOK_UPDATE_RECEIVE]: (state, action) => transformToCsv(action.payload),
    [BOOK_REMOVE_RECEIVE]: (state, action) => transformToCsv(action.payload),
    [BOOK_ADD_RECEIVE]: (state, action) => transformToCsv(action.payload),
});

export default csvdataReducer;

const month = [
    'January',
    'February',
    'March',
    'April',
    'May',
    'June',
    'July',
    'August',
    'September',
    'October',
    'November',
    'December'
];

function transformToCsv(books) {
    const rowsOfArrays = books.map(b => {
        return [
            b.title,
            b.subtitle,
            b.series,
            b.authorName,
            b.rating,
            b.averageRating,
            b.publisherName,
            b.binding,
            b.pages,
            b.yearPublished,
            month[b.monthRead - 1],
            b.monthRead,
            b.yearRead,
            b.bookshelf,
        ];
    });

    return [
        [
            'title',
            'subtitle',
            'series',
            'author',
            'my_rating',
            'avg_rating',
            'publisher',
            'binding',
            'pages',
            'year_published',
            'month',
            'month_read_num',
            'year_read',
            'bookshelf'
        ],
        ...rowsOfArrays
    ];
}
