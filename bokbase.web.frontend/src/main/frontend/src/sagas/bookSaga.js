import { takeLatest, select, put } from 'redux-saga/effects';
import { push } from 'connected-react-router';
import {
    SELECT_BOOK,
    SELECTED_BOOK,
    CLEAR_BOOK_DATA,
    SAVE_BOOK_BUTTON_CLICKED,
    BOOK_UPDATE_REQUEST,
    REMOVE_BOOK_BUTTON_CLICKED,
    BOOK_REMOVE_REQUEST,
    SAVE_ADD_BOOK_BUTTON_CLICKED,
    BOOK_ADD_REQUEST,
    BOOK_ADD_RECEIVE,
    CANCEL_ADD_BOOK_BUTTON_CLICKED,
} from '../reduxactions';

function* findBookFromBookId(action) {
    if (!action.payload) {
        yield put(CLEAR_BOOK_DATA());
        return;
    }

    const bookId = parseInt(action.payload);
    const books = yield select(state => state.books);
    const book = books.find(b => b.bookId === bookId);
    yield put(SELECTED_BOOK(book));
}

function* collectBook() {
    const bookId = yield select(state => state.bookId);
    const title = yield select(state => state.bookTitle);
    const subtitle = yield select(state => state.bookSubtitle);
    const isbn13 = yield select(state => state.bookIsbn13);
    const seriesId = yield select(state => state.bookSeriesId);
    const seriesNumber = yield select(state => state.bookSeriesNumber);
    const authorId = yield select(state => state.bookAuthorId);
    const rating = yield select(state => state.bookRating);
    const averageRating = yield select(state => state.bookAverageRating);
    const publisherId = yield select(state => state.bookPublisherId);
    const binding = yield select(state => state.bookBinding);
    const pages = yield select(state => state.bookPages);
    const publishedDate = yield select(state => state.bookPublishedDate);
    const finishedReadDate = yield select(state => state.bookFinishedReadDate);
    const bookshelf = yield select(state => state.bookBookshelf);
    const book = {
        bookId,
        title,
        subtitle,
        isbn13,
        seriesId,
        seriesNumber,
        authorId,
        rating,
        averageRating,
        publisherId,
        binding,
        pages,
        publishedDate,
        finishedReadDate,
        bookshelf,
    };

    return book;
}

function* collectAndUpdateBook() {
    const book = yield collectBook();
    yield put(BOOK_UPDATE_REQUEST(book));
}

export function* findBookIdAndRemoveBook() {
    const bookId = yield select(state => state.bookId);
    const book = {
        bookId,
    };

    yield put(BOOK_REMOVE_REQUEST(book));
}

function* collectAndAddBook() {
    const book = yield collectBook();
    yield put(BOOK_ADD_REQUEST(book));
}

function* selectAddedBookAndMoveToBooks(action) {
    yield put(SELECT_BOOK(action.payload.addedBookId));
    yield put(push('/books'));
}

export function* cancelAddBook() {
    yield put(SELECT_BOOK());
    yield put(push('/books'));
}

export default function* bookSaga() {
    yield takeLatest(SELECT_BOOK, findBookFromBookId);
    yield takeLatest(SAVE_BOOK_BUTTON_CLICKED, collectAndUpdateBook);
    yield takeLatest(REMOVE_BOOK_BUTTON_CLICKED, findBookIdAndRemoveBook);
    yield takeLatest(SAVE_ADD_BOOK_BUTTON_CLICKED, collectAndAddBook);
    yield takeLatest(BOOK_ADD_RECEIVE, selectAddedBookAndMoveToBooks);
    yield takeLatest(CANCEL_ADD_BOOK_BUTTON_CLICKED, cancelAddBook);
}
