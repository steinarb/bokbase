import { takeLatest, select, put } from 'redux-saga/effects';
import { push } from 'connected-react-router';
import {
    SELECT_PUBLISHER,
    SELECTED_PUBLISHER,
    CLEAR_PUBLISHER_DATA,
    SAVE_PUBLISHER_BUTTON_CLICKED,
    PUBLISHER_UPDATE_REQUEST,
    REMOVE_PUBLISHER_BUTTON_CLICKED,
    PUBLISHER_REMOVE_REQUEST,
    SAVE_ADD_PUBLISHER_BUTTON_CLICKED,
    PUBLISHER_ADD_REQUEST,
    PUBLISHER_ADD_RECEIVE,
    CANCEL_ADD_PUBLISHER_BUTTON_CLICKED,
} from '../reduxactions';

function* findPublisherFromPublisherId(action) {
    if (!action.payload) {
        yield put(CLEAR_PUBLISHER_DATA());
        return;
    }

    const publisherId = parseInt(action.payload);
    const publishers = yield select(state => state.publishers);
    const publisher = publishers.find(b => b.publisherId === publisherId);
    yield put(SELECTED_PUBLISHER(publisher));
}

function* collectPublisher() {
    const publisherId = yield select(state => state.publisherId);
    const name = yield select(state => state.publisherName);
    const publisher = {
        publisherId,
        name,
    };

    return publisher;
}

function* collectAndUpdatePublisher() {
    const publisher = yield collectPublisher();
    yield put(PUBLISHER_UPDATE_REQUEST(publisher));
}

export function* findPublisherIdAndRemovePublisher() {
    const publisherId = yield select(state => state.publisherId);
    const publisher = {
        publisherId,
    };

    yield put(PUBLISHER_REMOVE_REQUEST(publisher));
}

function* collectAndAddPublisher() {
    const publisher = yield collectPublisher();
    yield put(PUBLISHER_ADD_REQUEST(publisher));
}

function* selectAddedPublisherAndMoveToPublishers(action) {
    yield put(SELECT_PUBLISHER(action.payload.addedPublisherId));
    yield put(push('/publishers'));
}

export function* cancelAddPublisher() {
    yield put(SELECT_PUBLISHER());
    yield put(push('/publishers'));
}

export default function* publisherSaga() {
    yield takeLatest(SELECT_PUBLISHER, findPublisherFromPublisherId);
    yield takeLatest(SAVE_PUBLISHER_BUTTON_CLICKED, collectAndUpdatePublisher);
    yield takeLatest(REMOVE_PUBLISHER_BUTTON_CLICKED, findPublisherIdAndRemovePublisher);
    yield takeLatest(SAVE_ADD_PUBLISHER_BUTTON_CLICKED, collectAndAddPublisher);
    yield takeLatest(PUBLISHER_ADD_RECEIVE, selectAddedPublisherAndMoveToPublishers);
    yield takeLatest(CANCEL_ADD_PUBLISHER_BUTTON_CLICKED, cancelAddPublisher);
}
