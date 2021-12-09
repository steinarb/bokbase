import React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { emptyStringWhenFalsy } from './componentsCommon';
import Container from './bootstrap/Container';
import StyledLinkLeft from './bootstrap/StyledLinkLeft';
import FormRow from './bootstrap/FormRow';
import FormLabel from './bootstrap/FormLabel';
import FormField from './bootstrap/FormField';
import {
    SELECT_BOOK,
    SAVE_BOOK_BUTTON_CLICKED,
    REMOVE_BOOK_BUTTON_CLICKED,
    BOOK_BOOKSHELF_SELECT,
    BOOK_AUTHOR_SELECT,
    BOOK_TITLE_MODIFY,
    BOOK_SUBTITLE_MODIFY,
    BOOK_SERIES_SELECT,
    BOOK_SERIES_NUMBER_MODIFY,
    BOOK_PUBLISHER_SELECT,
    BOOK_YEAR_PUBLISHED_MODIFY,
    BOOK_PAGES_MODIFY,
    BOOK_BINDING_MODIFY,
    BOOK_YEAR_READ_MODIFY,
    BOOK_MONTH_READ_MODIFY,
    BOOK_RATING_MODIFY,
    BOOK_AVERAGE_RATING_MODIFY,
} from '../reduxactions';
import Locale from './Locale';


function Books(props) {
    const {
        text,
        books,
        authors,
        series,
        publishers,
        bookModified,
        bookId,
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
        onBookSelect,
        onBookAdd,
        onBookRemove,
        onBookBookshelfSelect,
        onBookAuthorSelect,
        onBookTitleModify,
        onBookSubtitleModify,
        onBookSerieselect,
        onBookSeriesNumberModify,
        onBookPublisherSelect,
        onBookYearPublishedModify,
        onBookPagesModify,
        onBookBindingModify,
        onBookYearReadModify,
        onBookMonthReadModify,
        onBookRatingModify,
        onBookAverageRatingModify,
    } = props;

    return (
        <div>
            <nav className="navbar navbar-light bg-light">
                <StyledLinkLeft to="/">&nbsp;{text.toTheBookshelf}</StyledLinkLeft>
                <h1>{text.modifyBook}</h1>
                <Locale />
            </nav>
            <Container>
                <div className="row">&nbsp;</div>
                <div className="row text-center">
                    <div className="col-4">
                        <button type="button" disabled={!bookModified} className="btn btn-primary" onClick={onBookAdd}>{text.saveBook}</button>
                    </div>
                    <div className="col-4">
                        <button type="button" className="btn btn-primary" onClick={onBookRemove}>{text.removeBook}</button>
                    </div>
                    <div className="col-4">
                        <Link className="btn btn-primary" to="/books/add">{text.addBook}</Link>
                    </div>
                </div>
            </Container>
            <form onSubmit={ e => { e.preventDefault(); }}>
                <Container>
                    <div className="row">&nbsp;</div>
                    <FormRow>
                        <FormLabel htmlFor="users">{text.selectBook}</FormLabel>
                        <FormField>
                            <select id="users" className="form-control" value={bookId} onChange={onBookSelect}>
                                <option key="bookNull" value=""></option>
                                {books.map((val) => <option key={'book' + val.bookId} value={val.bookId}>{val.title}</option>)}
                            </select>
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="book_bookshelf">{text.bookshelf}</FormLabel>
                        <FormField>
                            <select id="users" className="form-control" value={bookBookshelf} onChange={onBookBookshelfSelect}>
                                <option key="bookshelfNull" value=""></option>
                                <option key="to-read" value="toRead">{text.toRead}</option>
                                <option key="currently-reading" value="currentlyReading">{text.currentlyReading}</option>
                                <option key="read" value="read">{text.read}</option>
                                <option key="quit-reading" value="quitReading">{text.quitReading}</option>
                            </select>
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="authors">{text.author}</FormLabel>
                        <FormField>
                            <select id="users" className="form-control" value={bookAuthorId} onChange={onBookAuthorSelect}>
                                <option key="authorNull" value=""></option>
                                {authors.map((val) => <option key={'author' + val.authorId} value={val.authorId}>{val.firstname + ' ' + val.lastname}</option>)}
                            </select>
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="title">{text.title}</FormLabel>
                        <FormField>
                            <input id="title" className="form-control" type="text" value={bookTitle} onChange={onBookTitleModify} />
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="subtitle">{text.subtitle}</FormLabel>
                        <FormField>
                            <input id="subtitle" className="form-control" type="text" value={bookSubtitle} onChange={onBookSubtitleModify} />
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="series">{text.series}</FormLabel>
                        <FormField>
                            <select id="users" className="form-control" value={bookSeriesId} onChange={onBookSerieselect}>
                                <option key="seriesNull" value=""></option>
                                {series.map((val) => <option key={'series' + val.seriesId} value={val.seriesId}>{val.name}</option>)}
                            </select>
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="seriesNumber">{text.seriesNumber}</FormLabel>
                        <FormField>
                            <input id="seriesNumber" className="form-control" type="text" value={bookSeriesNumber} onChange={onBookSeriesNumberModify} />
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="publishers">{text.publisher}</FormLabel>
                        <FormField>
                            <select id="users" className="form-control" value={bookPublisherId} onChange={onBookPublisherSelect}>
                                <option key="publisherNull" value=""></option>
                                {publishers.map((val) => <option key={'publisher' + val.publisherId} value={val.publisherId}>{val.name}</option>)}
                            </select>
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="yearPublished">{text.yearPublished}</FormLabel>
                        <FormField>
                            <input id="yearPublished" className="form-control" type="text" value={bookYearPublished} onChange={onBookYearPublishedModify} />
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="pages">{text.pages}</FormLabel>
                        <FormField>
                            <input id="pages" className="form-control" type="text" value={bookPages} onChange={onBookPagesModify} />
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="binding">{text.binding}</FormLabel>
                        <FormField>
                            <select id="binding" className="form-control" value={bookBinding} onChange={onBookBindingModify}>
                                <option key="bindingNull" value=""></option>
                                <option key="Hardcover" value="Hardcover">{text.hardcover}</option>
                                <option key="Paperback" value="Paperback">{text.paperback}</option>
                            </select>
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="yearRead">{text.yearRead}</FormLabel>
                        <FormField>
                            <input id="yearRead" className="form-control" type="text" value={bookYearRead} onChange={onBookYearReadModify} />
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="monthRead">{text.monthRead}</FormLabel>
                        <FormField>
                            <select id="monthRead" className="form-control" value={bookMonthRead} onChange={onBookMonthReadModify}>
                                <option key="monthReadNull" value=""></option>
                                <option key="january" value="1">{text.january}</option>
                                <option key="february" value="2">{text.february}</option>
                                <option key="march" value="3">{text.march}</option>
                                <option key="april" value="4">{text.april}</option>
                                <option key="may" value="5">{text.may}</option>
                                <option key="june" value="6">{text.june}</option>
                                <option key="july" value="7">{text.july}</option>
                                <option key="august" value="8">{text.august}</option>
                                <option key="september" value="9">{text.september}</option>
                                <option key="october" value="10">{text.october}</option>
                                <option key="november" value="11">{text.november}</option>
                                <option key="december" value="12">{text.december}</option>
                            </select>
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="rating">{text.rating}</FormLabel>
                        <FormField>
                            <input id="rating" className="form-control" type="text" value={bookRating} onChange={onBookRatingModify} />
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="averageRating">{text.averageRating}</FormLabel>
                        <FormField>
                            <input id="averageRating" className="form-control" type="text" value={bookAverageRating} onChange={onBookAverageRatingModify} />
                        </FormField>
                    </FormRow>
                </Container>
            </form>
        </div>
    );
}

function mapStateToProps(state) {
    const {
        loginresult,
        books,
        authors,
        series,
        publishers,
        bookModified,
        bookId,
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
    } = state;
    const text = state.displayTexts;
    return {
        text,
        loginresult,
        books,
        authors,
        series,
        publishers,
        bookModified,
        bookId: emptyStringWhenFalsy(bookId),
        bookBookshelf: emptyStringWhenFalsy(bookBookshelf),
        bookAuthorId: emptyStringWhenFalsy(bookAuthorId),
        bookTitle: emptyStringWhenFalsy(bookTitle),
        bookSubtitle: emptyStringWhenFalsy(bookSubtitle),
        bookSeriesId: emptyStringWhenFalsy(bookSeriesId),
        bookSeriesNumber: emptyStringWhenFalsy(bookSeriesNumber),
        bookPublisherId: emptyStringWhenFalsy(bookPublisherId),
        bookYearPublished: emptyStringWhenFalsy(bookYearPublished),
        bookPages: emptyStringWhenFalsy(bookPages),
        bookBinding: emptyStringWhenFalsy(bookBinding),
        bookYearRead: emptyStringWhenFalsy(bookYearRead),
        bookMonthRead: emptyStringWhenFalsy(bookMonthRead),
        bookRating: emptyStringWhenFalsy(bookRating),
        bookAverageRating: emptyStringWhenFalsy(bookAverageRating),
    };
}

function mapDispatchToProps(dispatch) {
    return {
        onBookSelect: (e) => dispatch(SELECT_BOOK(e.target.value)),
        onBookAdd: () => dispatch(SAVE_BOOK_BUTTON_CLICKED()),
        onBookRemove: () => dispatch(REMOVE_BOOK_BUTTON_CLICKED()),
        onBookBookshelfSelect: (e) => dispatch(BOOK_BOOKSHELF_SELECT(e.target.value)),
        onBookAuthorSelect: (e) => dispatch(BOOK_AUTHOR_SELECT(e.target.value)),
        onBookTitleModify: (e) => dispatch(BOOK_TITLE_MODIFY(e.target.value)),
        onBookSubtitleModify: (e) => dispatch(BOOK_SUBTITLE_MODIFY(e.target.value)),
        onBookSerieselect: (e) => dispatch(BOOK_SERIES_SELECT(e.target.value)),
        onBookSeriesNumberModify: (e) => dispatch(BOOK_SERIES_NUMBER_MODIFY(e.target.value)),
        onBookPublisherSelect: (e) => dispatch(BOOK_PUBLISHER_SELECT(e.target.value)),
        onBookYearPublishedModify:  (e) => dispatch(BOOK_YEAR_PUBLISHED_MODIFY(e.target.value)),
        onBookPagesModify: (e) => dispatch(BOOK_PAGES_MODIFY(e.target.value)),
        onBookBindingModify: (e) => dispatch(BOOK_BINDING_MODIFY(e.target.value)),
        onBookYearReadModify: (e) => dispatch(BOOK_YEAR_READ_MODIFY(e.target.value)),
        onBookMonthReadModify: (e) => dispatch(BOOK_MONTH_READ_MODIFY(e.target.value)),
        onBookRatingModify: (e) => dispatch(BOOK_RATING_MODIFY(e.target.value)),
        onBookAverageRatingModify: (e) => dispatch(BOOK_AVERAGE_RATING_MODIFY(e.target.value)),
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(Books);
