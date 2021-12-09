import { takeLatest, call, put } from 'redux-saga/effects';
import axios from 'axios';
import {
    BOOKS_REQUEST,
    BOOKS_RECEIVE,
    BOOKS_ERROR,
    BOOK_ADD_REQUEST,
    BOOK_ADD_RECEIVE,
    BOOK_ADD_ERROR,
    BOOK_UPDATE_REQUEST,
    BOOK_UPDATE_RECEIVE,
    BOOK_UPDATE_ERROR,
    BOOK_REMOVE_REQUEST,
    BOOK_REMOVE_RECEIVE,
    BOOK_REMOVE_ERROR,
    LOGINSTATE_RECEIVE,
    SELECT_BOOK,
} from '../reduxactions';

function getBooks(username) {
    return axios.get('/api/books/' + username);
}

function* receiveBooksResultat(action) {
    try {
        const response = yield call(getBooks, action.payload);
        const booksresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(BOOKS_RECEIVE(booksresult));
    } catch (error) {
        yield put(BOOKS_ERROR(error));
    }
}

function postBookAdd(bookToAdd) {
    return axios.post('/api/book/add', bookToAdd);
}

function* receiveAddBookResultat(action) {
    try {
        const bookToAdd = action.payload;
        const response = yield call(postBookAdd, bookToAdd);
        const addbookresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(BOOK_ADD_RECEIVE(addbookresult));
    } catch (error) {
        yield put(BOOK_ADD_ERROR(error));
    }
}

function postBookUpdate(bookToUpdate) {
    return axios.post('/api/book/update', bookToUpdate);
}

function* receiveUpdateBookResultat(action) {
    try {
        const bookToUpdate = action.payload;
        const response = yield call(postBookUpdate, bookToUpdate);
        const updatebookresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(BOOK_UPDATE_RECEIVE(updatebookresult));
        yield put(SELECT_BOOK(bookToUpdate.bookId));
    } catch (error) {
        yield put(BOOK_UPDATE_ERROR(error));
    }
}

function postBookRemove(bookToRemove) {
    return axios.post('/api/book/remove', bookToRemove);
}

function* receiveRemoveBookResultat(action) {
    try {
        const bookToRemove = action.payload;
        const response = yield call(postBookRemove, bookToRemove);
        const removebookresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(BOOK_REMOVE_RECEIVE(removebookresult));
        yield put(SELECT_BOOK());
    } catch (error) {
        yield put(BOOK_REMOVE_ERROR(error));
    }
}

function* getBooksWhenLoginstateArrives(action) {
    const username = action.payload.user.username;
    if (username) {
        yield put(BOOKS_REQUEST(username));
    }
}

export default function* booksSaga() {
    yield takeLatest(BOOKS_REQUEST, receiveBooksResultat);
    yield takeLatest(BOOK_ADD_REQUEST, receiveAddBookResultat);
    yield takeLatest(BOOK_UPDATE_REQUEST, receiveUpdateBookResultat);
    yield takeLatest(BOOK_REMOVE_REQUEST, receiveRemoveBookResultat);
    yield takeLatest(LOGINSTATE_RECEIVE, getBooksWhenLoginstateArrives);
}
