import { createReducer } from '@reduxjs/toolkit';
import {
    SELECTED_BOOK,
    BOOK_SUBTITLE_MODIFY,
    CLEAR_BOOK_DATA,
} from '../reduxactions';
import { nullWhenFalsy } from './reducerCommon';

const defaultValue=null;

const bookSubtitleReducer = createReducer(defaultValue, {
    [SELECTED_BOOK]: (state, action) => action.payload.subtitle,
    [BOOK_SUBTITLE_MODIFY]: (state, action) => nullWhenFalsy(action.payload),
    [CLEAR_BOOK_DATA]: () => defaultValue,
});

export default bookSubtitleReducer;
