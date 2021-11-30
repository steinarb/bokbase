import { createReducer } from '@reduxjs/toolkit';
import {
    PUBLISHERS_RECEIVE,
    PUBLISHER_UPDATE_RECEIVE,
    PUBLISHER_REMOVE_RECEIVE,
    PUBLISHER_ADD_RECEIVE,
} from '../reduxactions';

const publishersReducer = createReducer([], {
    [PUBLISHERS_RECEIVE]: (state, action) => action.payload,
    [PUBLISHER_UPDATE_RECEIVE]: (state, action) => action.payload,
    [PUBLISHER_REMOVE_RECEIVE]: (state, action) => action.payload,
    [PUBLISHER_ADD_RECEIVE]: (state, action) => action.payload.publishers,
});

export default publishersReducer;
