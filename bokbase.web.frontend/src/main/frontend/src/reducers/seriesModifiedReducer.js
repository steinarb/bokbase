import { createReducer } from '@reduxjs/toolkit';
import {
    SELECT_SERIES,
    CLEAR_SERIES_DATA,
    SERIES_NAME_MODIFY,
} from '../reduxactions';

const seriesModifiedReducer = createReducer(false, {
    [SELECT_SERIES]: () => false,
    [CLEAR_SERIES_DATA]: () => false,
    [SERIES_NAME_MODIFY]: () => true,
});

export default seriesModifiedReducer;
