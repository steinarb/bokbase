import { createReducer } from '@reduxjs/toolkit';
import {
    SELECT_BOOK,
    CLEAR_BOOK_DATA,
    BOOK_BOOKSHELF_SELECT,
    BOOK_AUTHOR_SELECT,
    BOOK_TITLE_MODIFY,
    BOOK_SUBTITLE_MODIFY,
    BOOK_ISBN13_MODIFY,
    BOOK_SERIES_SELECT,
    BOOK_SERIES_NUMBER_MODIFY,
    BOOK_PUBLISHER_SELECT,
    BOOK_PUBLISHED_DATE_MODIFY,
    BOOK_PAGES_MODIFY,
    BOOK_BINDING_MODIFY,
    BOOK_FINISHED_READ_DATE_MODIFY,
    BOOK_RATING_MODIFY,
    BOOK_AVERAGE_RATING_MODIFY,
} from '../reduxactions';

const bookModifiedReducer = createReducer(false, {
    [SELECT_BOOK]: () => false,
    [CLEAR_BOOK_DATA]: () => false,
    [BOOK_BOOKSHELF_SELECT]: () => true,
    [BOOK_AUTHOR_SELECT]: () => true,
    [BOOK_TITLE_MODIFY]: () => true,
    [BOOK_SUBTITLE_MODIFY]: () => true,
    [BOOK_ISBN13_MODIFY]: () => true,
    [BOOK_SERIES_SELECT]: () => true,
    [BOOK_SERIES_NUMBER_MODIFY]: () => true,
    [BOOK_PUBLISHER_SELECT]: () => true,
    [BOOK_PUBLISHED_DATE_MODIFY]: () => true,
    [BOOK_PAGES_MODIFY]: () => true,
    [BOOK_BINDING_MODIFY]: () => true,
    [BOOK_FINISHED_READ_DATE_MODIFY]: () => true,
    [BOOK_RATING_MODIFY]: () => true,
    [BOOK_AVERAGE_RATING_MODIFY]: () => true,
});

export default bookModifiedReducer;
