import { createReducer } from '@reduxjs/toolkit';
import {
    AUTHORS_RECEIVE,
    AUTHOR_UPDATE_RECEIVE,
    AUTHOR_REMOVE_RECEIVE,
    AUTHOR_ADD_RECEIVE,
} from '../reduxactions';

const authorsReducer = createReducer([], {
    [AUTHORS_RECEIVE]: (state, action) => action.payload,
    [AUTHOR_UPDATE_RECEIVE]: (state, action) => action.payload,
    [AUTHOR_REMOVE_RECEIVE]: (state, action) => action.payload,
    [AUTHOR_ADD_RECEIVE]: (state, action) => action.payload.authors,
});

export default authorsReducer;
