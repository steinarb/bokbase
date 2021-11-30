import { takeLatest, call, put } from 'redux-saga/effects';
import axios from 'axios';
import {
    AUTHORS_REQUEST,
    AUTHORS_RECEIVE,
    AUTHORS_ERROR,
    AUTHOR_ADD_REQUEST,
    AUTHOR_ADD_RECEIVE,
    AUTHOR_ADD_ERROR,
    AUTHOR_UPDATE_REQUEST,
    AUTHOR_UPDATE_RECEIVE,
    AUTHOR_UPDATE_ERROR,
    AUTHOR_REMOVE_REQUEST,
    AUTHOR_REMOVE_RECEIVE,
    AUTHOR_REMOVE_ERROR,
    SELECT_AUTHOR,
} from '../reduxactions';

function getAuthors() {
    return axios.get('/api/authors');
}

function* receiveAuthorsResultat() {
    try {
        const response = yield call(getAuthors);
        const authorsresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(AUTHORS_RECEIVE(authorsresult));
    } catch (error) {
        yield put(AUTHORS_ERROR(error));
    }
}

function postAuthorAdd(authorToAdd) {
    return axios.post('/api/author/add', authorToAdd);
}

function* receiveAddAuthorResultat(action) {
    try {
        const authorToAdd = action.payload;
        const response = yield call(postAuthorAdd, authorToAdd);
        const addauthorresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(AUTHOR_ADD_RECEIVE(addauthorresult));
    } catch (error) {
        yield put(AUTHOR_ADD_ERROR(error));
    }
}

function postAuthorUpdate(authorToUpdate) {
    return axios.post('/api/author/update', authorToUpdate);
}

function* receiveUpdateAuthorResultat(action) {
    try {
        const authorToUpdate = action.payload;
        const response = yield call(postAuthorUpdate, authorToUpdate);
        const updateauthorresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(AUTHOR_UPDATE_RECEIVE(updateauthorresult));
        yield put(SELECT_AUTHOR(authorToUpdate.authorId));
    } catch (error) {
        yield put(AUTHOR_UPDATE_ERROR(error));
    }
}

function postAuthorRemove(authorToRemove) {
    return axios.post('/api/author/remove', authorToRemove);
}

function* receiveRemoveAuthorResultat(action) {
    try {
        const authorToRemove = action.payload;
        const response = yield call(postAuthorRemove, authorToRemove);
        const removeauthorresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(AUTHOR_REMOVE_RECEIVE(removeauthorresult));
        yield put(SELECT_AUTHOR());
    } catch (error) {
        yield put(AUTHOR_REMOVE_ERROR(error));
    }
}

export default function* authorsSaga() {
    yield takeLatest(AUTHORS_REQUEST, receiveAuthorsResultat);
    yield takeLatest(AUTHOR_ADD_REQUEST, receiveAddAuthorResultat);
    yield takeLatest(AUTHOR_UPDATE_REQUEST, receiveUpdateAuthorResultat);
    yield takeLatest(AUTHOR_REMOVE_REQUEST, receiveRemoveAuthorResultat);
}
