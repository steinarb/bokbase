import 'regenerator-runtime/runtime';
import { fork, all } from "redux-saga/effects";
import loginSaga from './loginSaga';
import logoutSaga from './logoutSaga';
import loginstateSaga from './loginstateSaga';
import localeSaga from './localeSaga';
import defaultLocaleSaga from './defaultLocaleSaga';
import availableLocalesSaga from './availableLocalesSaga';
import displayTextsSaga from './displayTextsSaga';
import locationSaga from './locationSaga';
import accountsSaga from './accountsSaga';
import bookSaga from './bookSaga';
import booksSaga from './booksSaga';
import authorSaga from './authorSaga';
import authorsSaga from './authorsSaga';
import publisherSaga from './publisherSaga';
import publishersSaga from './publishersSaga';
import seriesSaga from './seriesSaga';

export default function* rootSaga() {
    yield all([
        fork(loginSaga),
        fork(logoutSaga),
        fork(loginstateSaga),
        fork(localeSaga),
        fork(defaultLocaleSaga),
        fork(availableLocalesSaga),
        fork(displayTextsSaga),
        fork(locationSaga),
        fork(accountsSaga),
        fork(bookSaga),
        fork(booksSaga),
        fork(authorSaga),
        fork(authorsSaga),
        fork(publisherSaga),
        fork(publishersSaga),
        fork(seriesSaga),
    ]);
}
