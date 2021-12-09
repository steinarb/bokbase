import { takeLatest, select, put } from 'redux-saga/effects';
import { LOCATION_CHANGE } from 'connected-react-router';
import {
    BOOKS_REQUEST,
    SELECT_BOOK,
    SELECT_AUTHOR,
    SELECT_PUBLISHER,
    ACCOUNTS_REQUEST,
} from '../reduxactions';

function* locationChange(action) {
    const { location = {} } = action.payload || {};
    const { pathname = '' } = location;

    if (pathname === '/') {
        yield select(state => state.username);
        yield put(ACCOUNTS_REQUEST());
    }
    if (pathname === '/books') {
        const username = yield select(state => state.loginresult.user.username);
        if (username) {
            yield put(BOOKS_REQUEST(username));
        }
    }
    if (pathname === '/books/add') {
        const bookId = yield select(state => state.bookId);
        if (bookId) {
            yield put(SELECT_BOOK());
        }
    }
    if (pathname === '/authors/add') {
        const authorId = yield select(state => state.authorId);
        if (authorId) {
            yield put(SELECT_AUTHOR());
        }
    }
    if (pathname === '/publishers/add') {
        const publisherId = yield select(state => state.publisherId);
        if (publisherId) {
            yield put(SELECT_PUBLISHER());
        }
    }
}

export default function* locationSaga() {
    yield takeLatest(LOCATION_CHANGE, locationChange);
}
