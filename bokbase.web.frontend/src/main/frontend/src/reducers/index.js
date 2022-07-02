import { combineReducers } from 'redux';
import { connectRouter } from 'connected-react-router';
import loginresult from './loginresultReducer';
import accounts from './accountsReducer';
import locale from './localeReducer';
import availableLocales from './availableLocalesReducer';
import displayTexts from './displayTextsReducer';
import errors from './errorsReducer';
import books from './booksReducer';
import csvdata from './csvdataReducer';
import authors from './authorsReducer';
import publishers from './publishersReducer';
import series from './seriesReducer';
import bookId from './bookIdReducer';
import bookModified from './bookModifiedReducer';
import bookBookshelf from './bookBookshelfReducer';
import bookTitle from './bookTitleReducer';
import bookSubtitle from './bookSubtitleReducer';
import bookIsbn13 from './bookIsbn13Reducer';
import bookAuthorId from './bookAuthorIdReducer';
import bookSeriesId from './bookSeriesIdReducer';
import bookSeriesNumber from './bookSeriesNumberReducer';
import bookPublisherId from './bookPublisherIdReducer';
import bookPublishedDate from './bookPublishedDateReducer';
import bookPages from './bookPagesReducer';
import bookBinding from './bookBindingReducer';
import bookFinishedReadDate from './bookFinishedReadDateReducer';
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
    csvdata,
    authors,
    publishers,
    series,
    bookId,
    bookModified,
    bookBookshelf,
    bookAuthorId,
    bookTitle,
    bookSubtitle,
    bookIsbn13,
    bookSeriesId,
    bookSeriesNumber,
    bookPublisherId,
    bookPublishedDate,
    bookPages,
    bookBinding,
    bookFinishedReadDate,
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
    loginresult,
    accounts,
    locale,
    availableLocales,
    displayTexts,
    errors,
});
