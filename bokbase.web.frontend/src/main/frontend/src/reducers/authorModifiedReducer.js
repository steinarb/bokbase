import { createReducer } from '@reduxjs/toolkit';
import {
    SELECT_AUTHOR,
    CLEAR_AUTHOR_DATA,
    AUTHOR_FIRSTNAME_MODIFY,
    AUTHOR_LASTNAME_MODIFY,
} from '../reduxactions';

const authorModifiedReducer = createReducer(false, {
    [SELECT_AUTHOR]: () => false,
    [CLEAR_AUTHOR_DATA]: () => false,
    [AUTHOR_FIRSTNAME_MODIFY]: () => true,
    [AUTHOR_LASTNAME_MODIFY]: () => true,
});

export default authorModifiedReducer;
