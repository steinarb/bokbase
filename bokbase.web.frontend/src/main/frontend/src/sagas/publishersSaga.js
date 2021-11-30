import { takeLatest, call, put } from 'redux-saga/effects';
import axios from 'axios';
import {
    PUBLISHERS_REQUEST,
    PUBLISHERS_RECEIVE,
    PUBLISHERS_ERROR,
    PUBLISHER_ADD_REQUEST,
    PUBLISHER_ADD_RECEIVE,
    PUBLISHER_ADD_ERROR,
    PUBLISHER_UPDATE_REQUEST,
    PUBLISHER_UPDATE_RECEIVE,
    PUBLISHER_UPDATE_ERROR,
    PUBLISHER_REMOVE_REQUEST,
    PUBLISHER_REMOVE_RECEIVE,
    PUBLISHER_REMOVE_ERROR,
    SELECT_PUBLISHER,
} from '../reduxactions';

function getPublishers() {
    return axios.get('/api/publishers');
}

function* receivePublishersResultat() {
    try {
        const response = yield call(getPublishers);
        const publishersresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(PUBLISHERS_RECEIVE(publishersresult));
    } catch (error) {
        yield put(PUBLISHERS_ERROR(error));
    }
}

function postPublisherAdd(publisherToAdd) {
    return axios.post('/api/publisher/add', publisherToAdd);
}

function* receiveAddPublisherResultat(action) {
    try {
        const publisherToAdd = action.payload;
        const response = yield call(postPublisherAdd, publisherToAdd);
        const addpublisherresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(PUBLISHER_ADD_RECEIVE(addpublisherresult));
    } catch (error) {
        yield put(PUBLISHER_ADD_ERROR(error));
    }
}

function postPublisherUpdate(publisherToUpdate) {
    return axios.post('/api/publisher/update', publisherToUpdate);
}

function* receiveUpdatePublisherResultat(action) {
    try {
        const publisherToUpdate = action.payload;
        const response = yield call(postPublisherUpdate, publisherToUpdate);
        const updatepublisherresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(PUBLISHER_UPDATE_RECEIVE(updatepublisherresult));
    } catch (error) {
        yield put(PUBLISHER_UPDATE_ERROR(error));
    }
}

function postPublisherRemove(publisherToRemove) {
    return axios.post('/api/publisher/remove', publisherToRemove);
}

function* receiveRemovePublisherResultat(action) {
    try {
        const publisherToRemove = action.payload;
        const response = yield call(postPublisherRemove, publisherToRemove);
        const removepublisherresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(PUBLISHER_REMOVE_RECEIVE(removepublisherresult));
        yield put(SELECT_PUBLISHER());
    } catch (error) {
        yield put(PUBLISHER_REMOVE_ERROR(error));
    }
}

export default function* publishersSaga() {
    yield takeLatest(PUBLISHERS_REQUEST, receivePublishersResultat);
    yield takeLatest(PUBLISHER_ADD_REQUEST, receiveAddPublisherResultat);
    yield takeLatest(PUBLISHER_UPDATE_REQUEST, receiveUpdatePublisherResultat);
    yield takeLatest(PUBLISHER_REMOVE_REQUEST, receiveRemovePublisherResultat);
}
