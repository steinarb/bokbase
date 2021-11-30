import { takeLatest, select, put } from 'redux-saga/effects';
import { push } from 'connected-react-router';
import {
    SELECT_AUTHOR,
    SELECTED_AUTHOR,
    CLEAR_AUTHOR_DATA,
    SAVE_AUTHOR_BUTTON_CLICKED,
    AUTHOR_UPDATE_REQUEST,
    REMOVE_AUTHOR_BUTTON_CLICKED,
    AUTHOR_REMOVE_REQUEST,
    SAVE_ADD_AUTHOR_BUTTON_CLICKED,
    AUTHOR_ADD_REQUEST,
    AUTHOR_ADD_RECEIVE,
    CANCEL_ADD_AUTHOR_BUTTON_CLICKED,
} from '../reduxactions';

function* findAuthorFromAuthorId(action) {
    if (!action.payload) {
        yield put(CLEAR_AUTHOR_DATA());
        return;
    }

    const authorId = parseInt(action.payload);
    const authors = yield select(state => state.authors);
    const author = authors.find(b => b.authorId === authorId);
    yield put(SELECTED_AUTHOR(author));
}

function* collectAuthor() {
    const authorId = yield select(state => state.authorId);
    const firstname = yield select(state => state.authorFirstname);
    const lastname = yield select(state => state.authorLastname);
    const author = {
        authorId,
        firstname,
        lastname,
    };

    return author;
}

function* collectAndUpdateAuthor() {
    const author = yield collectAuthor();
    yield put(AUTHOR_UPDATE_REQUEST(author));
}

export function* findAuthorIdAndRemoveAuthor() {
    const authorId = yield select(state => state.authorId);
    const author = {
        authorId,
    };

    yield put(AUTHOR_REMOVE_REQUEST(author));
}

function* collectAndAddAuthor() {
    const author = yield collectAuthor();
    yield put(AUTHOR_ADD_REQUEST(author));
}

function* selectAddedAuthorAndMoveToAuthors(action) {
    yield put(SELECT_AUTHOR(action.payload.addedAuthorId));
    yield put(push('/authors'));
}

export function* cancelAddAuthor() {
    yield put(SELECT_AUTHOR());
    yield put(push('/authors'));
}

export default function* authorSaga() {
    yield takeLatest(SELECT_AUTHOR, findAuthorFromAuthorId);
    yield takeLatest(SAVE_AUTHOR_BUTTON_CLICKED, collectAndUpdateAuthor);
    yield takeLatest(REMOVE_AUTHOR_BUTTON_CLICKED, findAuthorIdAndRemoveAuthor);
    yield takeLatest(SAVE_ADD_AUTHOR_BUTTON_CLICKED, collectAndAddAuthor);
    yield takeLatest(AUTHOR_ADD_RECEIVE, selectAddedAuthorAndMoveToAuthors);
    yield takeLatest(CANCEL_ADD_AUTHOR_BUTTON_CLICKED, cancelAddAuthor);
}
