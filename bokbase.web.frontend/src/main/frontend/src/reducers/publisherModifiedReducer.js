import { createReducer } from '@reduxjs/toolkit';
import {
    SELECT_PUBLISHER,
    CLEAR_PUBLISHER_DATA,
    PUBLISHER_NAME_MODIFY,
} from '../reduxactions';

const publisherModifiedReducer = createReducer(false, {
    [SELECT_PUBLISHER]: () => false,
    [CLEAR_PUBLISHER_DATA]: () => false,
    [PUBLISHER_NAME_MODIFY]: () => true,
});

export default publisherModifiedReducer;
