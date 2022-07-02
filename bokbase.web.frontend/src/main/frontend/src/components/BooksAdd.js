import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import DatePicker from 'react-date-picker';
import { emptyStringWhenFalsy } from './componentsCommon';
import Container from './bootstrap/Container';
import StyledLinkLeft from './bootstrap/StyledLinkLeft';
import FormRow from './bootstrap/FormRow';
import FormLabel from './bootstrap/FormLabel';
import FormField from './bootstrap/FormField';
import {
    SAVE_ADD_BOOK_BUTTON_CLICKED,
    CANCEL_ADD_BOOK_BUTTON_CLICKED,
    BOOK_BOOKSHELF_SELECT,
    BOOK_AUTHOR_SELECT,
    BOOK_TITLE_MODIFY,
    BOOK_ISBN13_MODIFY,
    BOOK_SUBTITLE_MODIFY,
    BOOK_SERIES_SELECT,
    BOOK_SERIES_NUMBER_MODIFY,
    BOOK_PUBLISHER_SELECT,
    BOOK_PUBLISHED_DATE_MODIFY,
    BOOK_PAGES_MODIFY,
    BOOK_BINDING_MODIFY,
    BOOK_FINISHED_READ_DATE_MODIFY,
    BOOK_RATING_MODIFY,
    BOOK_AVERAGE_RATING_MODIFY,
} from '../reduxactions';
import Locale from './Locale';


export default function BooksAdd() {
    const text = useSelector(state => state.displayTexts);
    const authors = useSelector(state => state.authors);
    const series = useSelector(state => state.series);
    const publishers = useSelector(state => state.publishers);
    const bookModified = useSelector(state => state.bookModified);
    const bookBookshelf = useSelector(state => emptyStringWhenFalsy(state.bookBookshelf));
    const bookAuthorId = useSelector(state => emptyStringWhenFalsy(state.bookAuthorId));
    const bookTitle = useSelector(state => emptyStringWhenFalsy(state.bookTitle));
    const bookSubtitle = useSelector(state => emptyStringWhenFalsy(state.bookSubtitle));
    const bookIsbn13 = useSelector(state => emptyStringWhenFalsy(state.bookIsbn13));
    const bookSeriesId = useSelector(state => emptyStringWhenFalsy(state.bookSeriesId));
    const bookSeriesNumber = useSelector(state => emptyStringWhenFalsy(state.bookSeriesNumber));
    const bookPublisherId = useSelector(state => emptyStringWhenFalsy(state.bookPublisherId));
    const bookPublishedDate = useSelector(state => state.bookPublishedDate);
    const bookPages = useSelector(state => emptyStringWhenFalsy(state.bookPages));
    const bookBinding = useSelector(state => emptyStringWhenFalsy(state.bookBinding));
    const bookFinishedReadDate = useSelector(state => state.bookFinishedReadDate);
    const bookRating = useSelector(state => emptyStringWhenFalsy(state.bookRating));
    const bookAverageRating = useSelector(state => emptyStringWhenFalsy(state.bookAverageRating));
    const locale = useSelector(state => state.locale.replace('_', '-'));
    const dispatch = useDispatch();

    return (
        <div>
            <nav className="navbar navbar-light bg-light">
                <StyledLinkLeft to="/books">&nbsp;{text.toModifyBook}</StyledLinkLeft>
                <h1>{text.addBook}</h1>
                <Locale />
            </nav>
            <Container>
                <div className="row">&nbsp;</div>
                <div className="row text-center">
                    <div className="col-4">
                        <button type="button" className="btn btn-primary" onClick={() => dispatch(SAVE_ADD_BOOK_BUTTON_CLICKED())} disabled={!bookModified}>{text.saveBook}</button>
                    </div>
                    <div className="col-4">
                        <button type="button" className="btn btn-primary" onClick={() => dispatch(CANCEL_ADD_BOOK_BUTTON_CLICKED())}>{text.cancel}</button>
                    </div>
                </div>
            </Container>
            <form onSubmit={ e => { e.preventDefault(); }}>
                <Container>
                    <div className="row">&nbsp;</div>
                    <FormRow>
                        <FormLabel htmlFor="book_bookshelf">{text.bookshelf}</FormLabel>
                        <FormField>
                            <select id="users" className="form-control" value={bookBookshelf} onChange={e => dispatch(BOOK_BOOKSHELF_SELECT(e.target.value))}>
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
                            <select id="users" className="form-control" value={bookAuthorId} onChange={e => dispatch(BOOK_AUTHOR_SELECT(e.target.value))}>
                                <option key="authorNull" value=""></option>
                                {authors.map((val) => <option key={'author' + val.authorId} value={val.authorId}>{val.firstname + ' ' + val.lastname}</option>)}
                            </select>
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="title">{text.title}</FormLabel>
                        <FormField>
                            <input id="title" className="form-control" type="text" value={bookTitle} onChange={e => dispatch(BOOK_TITLE_MODIFY(e.target.value))} />
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="subtitle">{text.subtitle}</FormLabel>
                        <FormField>
                            <input id="subtitle" className="form-control" type="text" value={bookSubtitle} onChange={e => dispatch(BOOK_SUBTITLE_MODIFY(e.target.value))} />
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="isbn13">{text.isbn13}</FormLabel>
                        <FormField>
                            <input id="isbn13" className="form-control" type="text" value={bookIsbn13} onChange={e => dispatch(BOOK_ISBN13_MODIFY(e.target.value))} />
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="series">{text.series}</FormLabel>
                        <FormField>
                            <select id="users" className="form-control" value={bookSeriesId} onChange={e => dispatch(BOOK_SERIES_SELECT(e.target.value))}>
                                <option key="seriesNull" value=""></option>
                                {series.map((val) => <option key={'series' + val.seriesId} value={val.seriesId}>{val.name}</option>)}
                            </select>
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="seriesNumber">{text.seriesNumber}</FormLabel>
                        <FormField>
                            <input id="seriesNumber" className="form-control" type="text" value={bookSeriesNumber} onChange={e => dispatch(BOOK_SERIES_NUMBER_MODIFY(e.target.value))} />
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="publishers">{text.publisher}</FormLabel>
                        <FormField>
                            <select id="users" className="form-control" value={bookPublisherId} onChange={e => dispatch(BOOK_PUBLISHER_SELECT(e.target.value))}>
                                <option key="publisherNull" value=""></option>
                                {publishers.map((val) => <option key={'publisher' + val.publisherId} value={val.publisherId}>{val.name}</option>)}
                            </select>
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="publishedDate">{text.publishedDate}</FormLabel>
                        <FormField>
                            <DatePicker id="publishedDate" locale={locale} value={bookPublishedDate} onChange={d => dispatch(BOOK_PUBLISHED_DATE_MODIFY(d))}/>
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="pages">{text.pages}</FormLabel>
                        <FormField>
                            <input id="pages" className="form-control" type="text" value={bookPages} onChange={e => dispatch(BOOK_PAGES_MODIFY(e.target.value))} />
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="binding">{text.binding}</FormLabel>
                        <FormField>
                            <select id="binding" className="form-control" value={bookBinding} onChange={e => dispatch(BOOK_BINDING_MODIFY(e.target.value))}>
                                <option key="bindingNull" value=""></option>
                                <option key="Hardcover" value="Hardcover">{text.hardcover}</option>
                                <option key="Paperback" value="Paperback">{text.paperback}</option>
                            </select>
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="finishedReadDate">{text.finishedReadDate}</FormLabel>
                        <FormField>
                            <DatePicker id="finishedReadDate" locale={locale} value={bookFinishedReadDate} onChange={d => dispatch(BOOK_FINISHED_READ_DATE_MODIFY(d))}/>
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="rating">{text.rating}</FormLabel>
                        <FormField>
                            <input id="rating" className="form-control" type="text" value={bookRating} onChange={e => dispatch(BOOK_RATING_MODIFY(e.target.value))} />
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="averageRating">{text.averageRating}</FormLabel>
                        <FormField>
                            <input id="averageRating" className="form-control" type="text" value={bookAverageRating} onChange={e => dispatch(BOOK_AVERAGE_RATING_MODIFY(e.target.value))} />
                        </FormField>
                    </FormRow>
                </Container>
            </form>
        </div>
    );
}
