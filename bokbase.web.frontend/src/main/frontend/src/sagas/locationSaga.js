import { takeLatest, select, put } from 'redux-saga/effects';
import { LOCATION_CHANGE } from 'connected-react-router';
import {
    BOOKS_REQUEST,
    SELECT_BOOK,
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
}

export default function* locationSaga() {
    yield takeLatest(LOCATION_CHANGE, locationChange);
}
