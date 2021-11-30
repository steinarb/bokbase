import { takeLatest, call, put, select } from 'redux-saga/effects';
import { push } from 'connected-react-router';
import axios from 'axios';
import {
    SERIES_REQUEST,
    SERIES_RECEIVE,
    SERIES_ERROR,
    SERIES_ADD_REQUEST,
    SERIES_ADD_RECEIVE,
    SERIES_ADD_ERROR,
    SERIES_UPDATE_REQUEST,
    SERIES_UPDATE_RECEIVE,
    SERIES_UPDATE_ERROR,
    SERIES_REMOVE_REQUEST,
    SERIES_REMOVE_RECEIVE,
    SERIES_REMOVE_ERROR,
    SELECT_SERIES,
    SELECTED_SERIES,
    CLEAR_SERIES_DATA,
    SAVE_SERIES_BUTTON_CLICKED,
    REMOVE_SERIES_BUTTON_CLICKED,
    SAVE_ADD_SERIES_BUTTON_CLICKED,
    CANCEL_ADD_SERIES_BUTTON_CLICKED,
} from '../reduxactions';

function getSeries() {
    return axios.get('/api/series');
}

function* receiveSeriesResultat() {
    try {
        const response = yield call(getSeries);
        const seriesresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(SERIES_RECEIVE(seriesresult));
    } catch (error) {
        yield put(SERIES_ERROR(error));
    }
}

function postSeriesAdd(seriesToAdd) {
    return axios.post('/api/series/add', seriesToAdd);
}

function* receiveAddSeriesResultat(action) {
    try {
        const seriesToAdd = action.payload;
        const response = yield call(postSeriesAdd, seriesToAdd);
        const seriesresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(SERIES_ADD_RECEIVE(seriesresult));
    } catch (error) {
        yield put(SERIES_ADD_ERROR(error));
    }
}

function postSeriesUpdate(seriesToUpdate) {
    return axios.post('/api/series/update', seriesToUpdate);
}

function* receiveUpdateSeriesResultat(action) {
    try {
        const seriesToUpdate = action.payload;
        const response = yield call(postSeriesUpdate, seriesToUpdate);
        const seriesresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(SERIES_UPDATE_RECEIVE(seriesresult));
    } catch (error) {
        yield put(SERIES_UPDATE_ERROR(error));
    }
}

function postSeriesRemove(seriesToRemove) {
    return axios.post('/api/series/remove', seriesToRemove);
}

function* receiveRemoveSeriesResultat(action) {
    try {
        const seriesToRemove = action.payload;
        const response = yield call(postSeriesRemove, seriesToRemove);
        const seriesresult = (response.headers['content-type'] === 'application/json') ? response.data : {};
        yield put(SERIES_REMOVE_RECEIVE(seriesresult));
    } catch (error) {
        yield put(SERIES_REMOVE_ERROR(error));
    }
}

function* findSeriesFromSeriesId(action) {
    if (!action.payload) {
        yield put(CLEAR_SERIES_DATA());
        return;
    }

    const seriesId = parseInt(action.payload);
    const series = yield select(state => state.series);
    const seriesInstance = series.find(b => b.seriesId === seriesId);
    yield put(SELECTED_SERIES(seriesInstance));
}

function* collectSeries() {
    const seriesId = yield select(state => state.seriesId);
    const name = yield select(state => state.seriesName);
    const series = {
        seriesId,
        name,
    };

    return series;
}

function* collectAndUpdateSeries() {
    const series = yield collectSeries();
    yield put(SERIES_UPDATE_REQUEST(series));
}

export function* findSeriesIdAndRemoveSeries() {
    const seriesId = yield select(state => state.seriesId);
    const series = {
        seriesId,
    };

    yield put(SERIES_REMOVE_REQUEST(series));
}

function* collectAndAddSeries() {
    const series = yield collectSeries();
    yield put(SERIES_ADD_REQUEST(series));
}

function* selectAddedSeriesAndMoveToSeries(action) {
    yield put(SELECT_SERIES(action.payload.addedSeriesId));
    yield put(push('/series'));
}

export function* cancelAddSeries() {
    yield put(SELECT_SERIES());
    yield put(push('/series'));
}

export default function* seriesSaga() {
    yield takeLatest(SERIES_REQUEST, receiveSeriesResultat);
    yield takeLatest(SERIES_ADD_REQUEST, receiveAddSeriesResultat);
    yield takeLatest(SERIES_UPDATE_REQUEST, receiveUpdateSeriesResultat);
    yield takeLatest(SERIES_REMOVE_REQUEST, receiveRemoveSeriesResultat);
    yield takeLatest(SELECT_SERIES, findSeriesFromSeriesId);
    yield takeLatest(SAVE_SERIES_BUTTON_CLICKED, collectAndUpdateSeries);
    yield takeLatest(REMOVE_SERIES_BUTTON_CLICKED, findSeriesIdAndRemoveSeries);
    yield takeLatest(SAVE_ADD_SERIES_BUTTON_CLICKED, collectAndAddSeries);
    yield takeLatest(SERIES_ADD_RECEIVE, selectAddedSeriesAndMoveToSeries);
    yield takeLatest(CANCEL_ADD_SERIES_BUTTON_CLICKED, cancelAddSeries);
}
