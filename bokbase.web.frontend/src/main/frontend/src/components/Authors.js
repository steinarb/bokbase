import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { Link } from 'react-router-dom';
import { emptyStringWhenFalsy } from './componentsCommon';
import Container from './bootstrap/Container';
import StyledLinkLeft from './bootstrap/StyledLinkLeft';
import FormRow from './bootstrap/FormRow';
import FormLabel from './bootstrap/FormLabel';
import FormField from './bootstrap/FormField';
import {
    SELECT_AUTHOR,
    SAVE_AUTHOR_BUTTON_CLICKED,
    REMOVE_AUTHOR_BUTTON_CLICKED,
    AUTHOR_FIRSTNAME_MODIFY,
    AUTHOR_LASTNAME_MODIFY,
} from '../reduxactions';
import Locale from './Locale';


export default function Authors() {
    const text = useSelector(state => state.displayTexts);
    const authors = useSelector(state => state.authors);
    const authorModified = useSelector(state => state.authorModified);
    const authorId = useSelector(state => emptyStringWhenFalsy(state.authorId));
    const authorFirstname = useSelector(state => emptyStringWhenFalsy(state.authorFirstname));
    const authorLastname = useSelector(state => emptyStringWhenFalsy(state.authorLastname));
    const dispatch = useDispatch();

    return (
        <div>
            <nav className="navbar navbar-light bg-light">
                <StyledLinkLeft to="/">&nbsp;{text.toTheBookshelf}</StyledLinkLeft>
                <h1>{text.modifyAuthor}</h1>
                <Locale />
            </nav>
            <Container>
                <div className="row">&nbsp;</div>
                <div className="row text-center">
                    <div className="col-4">
                        <button type="button" disabled={!authorModified} className="btn btn-primary" onClick={() => dispatch(SAVE_AUTHOR_BUTTON_CLICKED())}>{text.saveAuthor}</button>
                    </div>
                    <div className="col-4">
                        <button type="button" className="btn btn-primary" onClick={() => dispatch(REMOVE_AUTHOR_BUTTON_CLICKED())}>{text.removeAuthor}</button>
                    </div>
                    <div className="col-4">
                        <Link className="btn btn-primary" to="/authors/add">{text.addAuthor}</Link>
                    </div>
                </div>
            </Container>
            <form onSubmit={ e => { e.preventDefault(); }}>
                <Container>
                    <div className="row">&nbsp;</div>
                    <FormRow>
                        <FormLabel htmlFor="users">{text.selectAuthor}</FormLabel>
                        <FormField>
                            <select id="users" className="form-control" value={authorId} onChange={e => dispatch(SELECT_AUTHOR(e.target.value))}>
                                <option key="authorNull" value=""></option>
                                {authors.map((val) => <option key={'author' + val.authorId} value={val.authorId}>{val.firstname + ' ' + val.lastname}</option>)}
                            </select>
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="firstname">{text.firstname}</FormLabel>
                        <FormField>
                            <input id="firstname" className="form-control" type="text" value={authorFirstname} onChange={e => dispatch(AUTHOR_FIRSTNAME_MODIFY(e.target.value))} />
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="lastname">{text.lastname}</FormLabel>
                        <FormField>
                            <input id="lastname" className="form-control" type="text" value={authorLastname} onChange={e => dispatch(AUTHOR_LASTNAME_MODIFY(e.target.value))} />
                        </FormField>
                    </FormRow>
                </Container>
            </form>
        </div>
    );
}
