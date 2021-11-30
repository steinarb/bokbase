import { createAction } from '@reduxjs/toolkit';

export const USERNAME_MODIFY = createAction('USERNAME_MODIFY');
export const PASSWORD_MODIFY = createAction('PASSWORD_MODIFY');

export const LOGIN_REQUEST = createAction('LOGIN_REQUEST');
export const LOGIN_RECEIVE = createAction('LOGIN_RECEIVE');
export const LOGIN_ERROR = createAction('LOGIN_ERROR');

export const LOGOUT_REQUEST = createAction('LOGOUT_REQUEST');
export const LOGOUT_RECEIVE = createAction('LOGOUT_RECEIVE');
export const LOGOUT_ERROR = createAction('LOGOUT_ERROR');

export const LOGINSTATE_REQUEST = createAction('LOGINSTATE_REQUEST');
export const LOGINSTATE_RECEIVE = createAction('LOGINSTATE_RECEIVE');
export const LOGINSTATE_ERROR = createAction('LOGINSTATE_ERROR');

export const ACCOUNTS_REQUEST = createAction('ACCOUNTS_REQUEST');
export const ACCOUNTS_RECEIVE = createAction('ACCOUNTS_RECEIVE');
export const ACCOUNTS_ERROR = createAction('ACCOUNTS_ERROR');

export const DEFAULT_LOCALE_REQUEST = createAction('DEFAULT_LOCALE_REQUEST');
export const DEFAULT_LOCALE_RECEIVE = createAction('DEFAULT_LOCALE_RECEIVE');
export const DEFAULT_LOCALE_ERROR = createAction('DEFAULT_LOCALE_ERROR');
export const UPDATE_LOCALE = createAction('UPDATE_LOCALE');
export const AVAILABLE_LOCALES_REQUEST = createAction('AVAILABLE_LOCALES_REQUEST');
export const AVAILABLE_LOCALES_RECEIVE = createAction('AVAILABLE_LOCALES_RECEIVE');
export const AVAILABLE_LOCALES_ERROR = createAction('AVAILABLE_LOCALES_ERROR');
export const DISPLAY_TEXTS_REQUEST = createAction('DISPLAY_TEXTS_REQUEST');
export const DISPLAY_TEXTS_RECEIVE = createAction('DISPLAY_TEXTS_RECEIVE');
export const DISPLAY_TEXTS_ERROR = createAction('DISPLAY_TEXTS_ERROR');

export const BOOKS_REQUEST = createAction('BOOKS_REQUEST');
export const BOOKS_RECEIVE = createAction('BOOKS_RECEIVE');
export const BOOKS_ERROR = createAction('BOOKS_ERROR');
export const BOOK_ADD_REQUEST = createAction('BOOK_ADD_REQUEST');
export const BOOK_ADD_RECEIVE = createAction('BOOK_ADD_RECEIVE');
export const BOOK_ADD_ERROR = createAction('BOOK_ADD_ERROR');
export const BOOK_UPDATE_REQUEST = createAction('BOOK_UPDATE_REQUEST');
export const BOOK_UPDATE_RECEIVE = createAction('BOOK_UPDATE_RECEIVE');
export const BOOK_UPDATE_ERROR = createAction('BOOK_UPDATE_ERROR');
export const BOOK_REMOVE_REQUEST = createAction('BOOK_REMOVE_REQUEST');
export const BOOK_REMOVE_RECEIVE = createAction('BOOK_REMOVE_RECEIVE');
export const BOOK_REMOVE_ERROR = createAction('BOOK_REMOVE_ERROR');

export const AUTHORS_REQUEST = createAction('AUTHORS_REQUEST');
export const AUTHORS_RECEIVE = createAction('AUTHORS_RECEIVE');
export const AUTHORS_ERROR = createAction('AUTHORS_ERROR');
export const AUTHOR_ADD_REQUEST = createAction('AUTHOR_ADD_REQUEST');
export const AUTHOR_ADD_RECEIVE = createAction('AUTHOR_ADD_RECEIVE');
export const AUTHOR_ADD_ERROR = createAction('AUTHOR_ADD_ERROR');
export const AUTHOR_UPDATE_REQUEST = createAction('AUTHOR_UPDATE_REQUEST');
export const AUTHOR_UPDATE_RECEIVE = createAction('AUTHOR_UPDATE_RECEIVE');
export const AUTHOR_UPDATE_ERROR = createAction('AUTHOR_UPDATE_ERROR');
export const AUTHOR_REMOVE_REQUEST = createAction('AUTHOR_REMOVE_REQUEST');
export const AUTHOR_REMOVE_RECEIVE = createAction('AUTHOR_REMOVE_RECEIVE');
export const AUTHOR_REMOVE_ERROR = createAction('AUTHOR_REMOVE_ERROR');

export const PUBLISHERS_REQUEST = createAction('PUBLISHERS_REQUEST');
export const PUBLISHERS_RECEIVE = createAction('PUBLISHERS_RECEIVE');
export const PUBLISHERS_ERROR = createAction('PUBLISHERS_ERROR');
export const PUBLISHER_ADD_REQUEST = createAction('PUBLISHER_ADD_REQUEST');
export const PUBLISHER_ADD_RECEIVE = createAction('PUBLISHER_ADD_RECEIVE');
export const PUBLISHER_ADD_ERROR = createAction('PUBLISHER_ADD_ERROR');
export const PUBLISHER_UPDATE_REQUEST = createAction('PUBLISHER_UPDATE_REQUEST');
export const PUBLISHER_UPDATE_RECEIVE = createAction('PUBLISHER_UPDATE_RECEIVE');
export const PUBLISHER_UPDATE_ERROR = createAction('PUBLISHER_UPDATE_ERROR');
export const PUBLISHER_REMOVE_REQUEST = createAction('PUBLISHER_REMOVE_REQUEST');
export const PUBLISHER_REMOVE_RECEIVE = createAction('PUBLISHER_REMOVE_RECEIVE');
export const PUBLISHER_REMOVE_ERROR = createAction('PUBLISHER_REMOVE_ERROR');

export const SERIES_REQUEST = createAction('SERIES_REQUEST');
export const SERIES_RECEIVE = createAction('SERIES_RECEIVE');
export const SERIES_ERROR = createAction('SERIES_ERROR');
export const SERIES_ADD_REQUEST = createAction('SERIES_ADD_REQUEST');
export const SERIES_ADD_RECEIVE = createAction('SERIES_ADD_RECEIVE');
export const SERIES_ADD_ERROR = createAction('SERIES_ADD_ERROR');
export const SERIES_UPDATE_REQUEST = createAction('SERIES_UPDATE_REQUEST');
export const SERIES_UPDATE_RECEIVE = createAction('SERIES_UPDATE_RECEIVE');
export const SERIES_UPDATE_ERROR = createAction('SERIES_UPDATE_ERROR');
export const SERIES_REMOVE_REQUEST = createAction('SERIES_REMOVE_REQUEST');
export const SERIES_REMOVE_RECEIVE = createAction('SERIES_REMOVE_RECEIVE');
export const SERIES_REMOVE_ERROR = createAction('SERIES_REMOVE_ERROR');

export const SELECT_BOOK = createAction('SELECT_BOOK');
export const SELECTED_BOOK = createAction('SELECTED_BOOK');
export const CLEAR_BOOK_DATA = createAction('CLEAR_BOOK_DATA');
export const SAVE_BOOK_BUTTON_CLICKED = createAction('SAVE_BOOK_BUTTON_CLICKED');
export const REMOVE_BOOK_BUTTON_CLICKED = createAction('REMOVE_BOOK_BUTTON_CLICKED');
export const SAVE_ADD_BOOK_BUTTON_CLICKED = createAction('SAVE_ADD_BOOK_BUTTON_CLICKED');
export const CANCEL_ADD_BOOK_BUTTON_CLICKED = createAction('CANCEL_ADD_BOOK_BUTTON_CLICKED');
export const BOOK_BOOKSHELF_SELECT = createAction('BOOK_BOOKSHELF_SELECT');
export const BOOK_AUTHOR_SELECT = createAction('BOOK_AUTHOR_SELECT');
export const BOOK_TITLE_MODIFY = createAction('BOOK_TITLE_MODIFY');
export const BOOK_SUBTITLE_MODIFY = createAction('BOOK_SUBTITLE_MODIFY');
export const BOOK_SERIES_SELECT = createAction('BOOK_SERIES_SELECT');
export const BOOK_SERIES_NUMBER_MODIFY = createAction('BOOK_SERIES_NUMBER_MODIFY');
export const BOOK_PUBLISHER_SELECT = createAction('BOOK_PUBLISHER_SELECT');
export const BOOK_YEAR_PUBLISHED_MODIFY = createAction('BOOK_YEAR_PUBLISHED_MODIFY');
export const BOOK_PAGES_MODIFY = createAction('BOOK_PAGES_MODIFY');
export const BOOK_BINDING_MODIFY = createAction('BOOK_BINDING_MODIFY');
export const BOOK_YEAR_READ_MODIFY = createAction('BOOK_YEAR_READ_MODIFY');
export const BOOK_MONTH_READ_MODIFY = createAction('BOOK_MONTH_READ_MODIFY');
export const BOOK_RATING_MODIFY = createAction('BOOK_RATING_MODIFY');
export const BOOK_AVERAGE_RATING_MODIFY = createAction('BOOK_AVERAGE_RATING_MODIFY');

export const SELECT_AUTHOR = createAction('SELECT_AUTHOR');
export const SELECTED_AUTHOR = createAction('SELECTED_AUTHOR');
export const CLEAR_AUTHOR_DATA = createAction('CLEAR_AUTHOR_DATA');
export const SAVE_AUTHOR_BUTTON_CLICKED = createAction('SAVE_AUTHOR_BUTTON_CLICKED');
export const REMOVE_AUTHOR_BUTTON_CLICKED = createAction('REMOVE_AUTHOR_BUTTON_CLICKED');
export const SAVE_ADD_AUTHOR_BUTTON_CLICKED = createAction('SAVE_ADD_AUTHOR_BUTTON_CLICKED');
export const CANCEL_ADD_AUTHOR_BUTTON_CLICKED = createAction('CANCEL_ADD_AUTHOR_BUTTON_CLICKED');
export const AUTHOR_FIRSTNAME_MODIFY = createAction('AUTHOR_FIRSTNAME_MODIFY');
export const AUTHOR_LASTNAME_MODIFY = createAction('AUTHOR_LASTNAME_MODIFY');

export const SELECT_PUBLISHER = createAction('SELECT_PUBLISHER');
export const SELECTED_PUBLISHER = createAction('SELECTED_PUBLISHER');
export const CLEAR_PUBLISHER_DATA = createAction('CLEAR_PUBLISHER_DATA');
export const SAVE_PUBLISHER_BUTTON_CLICKED = createAction('SAVE_PUBLISHER_BUTTON_CLICKED');
export const REMOVE_PUBLISHER_BUTTON_CLICKED = createAction('REMOVE_PUBLISHER_BUTTON_CLICKED');
export const SAVE_ADD_PUBLISHER_BUTTON_CLICKED = createAction('SAVE_ADD_PUBLISHER_BUTTON_CLICKED');
export const CANCEL_ADD_PUBLISHER_BUTTON_CLICKED = createAction('CANCEL_ADD_PUBLISHER_BUTTON_CLICKED');
export const PUBLISHER_NAME_MODIFY = createAction('PUBLISHER_NAME_MODIFY');

export const SELECT_SERIES = createAction('SELECT_SERIES');
export const SELECTED_SERIES = createAction('SELECTED_SERIES');
export const CLEAR_SERIES_DATA = createAction('CLEAR_SERIES_DATA');
export const SAVE_SERIES_BUTTON_CLICKED = createAction('SAVE_SERIES_BUTTON_CLICKED');
export const REMOVE_SERIES_BUTTON_CLICKED = createAction('REMOVE_SERIES_BUTTON_CLICKED');
export const SAVE_ADD_SERIES_BUTTON_CLICKED = createAction('SAVE_ADD_SERIES_BUTTON_CLICKED');
export const CANCEL_ADD_SERIES_BUTTON_CLICKED = createAction('CANCEL_ADD_SERIES_BUTTON_CLICKED');
export const SERIES_NAME_MODIFY = createAction('SERIES_NAME_MODIFY');
