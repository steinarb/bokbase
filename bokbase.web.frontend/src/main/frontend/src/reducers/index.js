import { combineReducers } from 'redux';
import { connectRouter } from 'connected-react-router';
import username from './usernameReducer';
import password from './passwordReducer';
import loginresultat from './loginresultatReducer';
import accounts from './accountsReducer';
import locale from './localeReducer';
import availableLocales from './availableLocalesReducer';
import displayTexts from './displayTextsReducer';
import errors from './errorsReducer';
import books from './booksReducer';
import authors from './authorsReducer';
import publishers from './publishersReducer';
import series from './seriesReducer';
import bookId from './bookIdReducer';
import bookModified from './bookModifiedReducer';
import bookBookshelf from './bookBookshelfReducer';
import bookTitle from './bookTitleReducer';
import bookSubtitle from './bookSubtitleReducer';
import bookAuthorId from './bookAuthorIdReducer';
import bookSeriesId from './bookSeriesIdReducer';
import bookSeriesNumber from './bookSeriesNumberReducer';
import bookPublisherId from './bookPublisherIdReducer';
import bookYearPublished from './bookYearPublishedReducer';
import bookPages from './bookPagesReducer';
import bookBinding from './bookBindingReducer';
import bookYearRead from './bookYearReadReducer';
import bookMonthRead from './bookMonthReadReducer';
import bookRating from './bookRatingReducer';
import bookAverageRating from './bookAverageRatingReducer';
import authorId from './authorIdReducer';
import authorModified from './authorModifiedReducer';
import authorFirstname from './authorFirstnameReducer';
import authorLastname from './authorLastnameReducer';
import publisherId from './publisherIdReducer';
import publisherModified from './publisherModifiedReducer';
import publisherName from './publisherNameReducer';
import seriesId from './seriesIdReducer';
import seriesModified from './seriesModifiedReducer';
import seriesName from './seriesNameReducer';

export default (history) => combineReducers({
    router: connectRouter(history),
    books,
    authors,
    publishers,
    series,
    bookId,
    bookModified,
    bookBookshelf,
    bookAuthorId,
    bookTitle,
    bookSubtitle,
    bookSeriesId,
    bookSeriesNumber,
    bookPublisherId,
    bookYearPublished,
    bookPages,
    bookBinding,
    bookYearRead,
    bookMonthRead,
    bookRating,
    bookAverageRating,
    authorId,
    authorModified,
    authorFirstname,
    authorLastname,
    publisherId,
    publisherModified,
    publisherName,
    seriesId,
    seriesModified,
    seriesName,
    username,
    password,
    loginresultat,
    accounts,
    locale,
    availableLocales,
    displayTexts,
    errors,
});
