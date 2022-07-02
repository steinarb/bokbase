import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { emptyStringWhenFalsy } from './componentsCommon';
import Container from './bootstrap/Container';
import StyledLinkLeft from './bootstrap/StyledLinkLeft';
import FormRow from './bootstrap/FormRow';
import FormLabel from './bootstrap/FormLabel';
import FormField from './bootstrap/FormField';
import {
    SAVE_ADD_AUTHOR_BUTTON_CLICKED,
    CANCEL_ADD_AUTHOR_BUTTON_CLICKED,
    AUTHOR_FIRSTNAME_MODIFY,
    AUTHOR_LASTNAME_MODIFY,
} from '../reduxactions';
import Locale from './Locale';


export default function AuthorsAdd() {
    const text = useSelector(state => state.displayTexts);
    const authorModified = useSelector(state => state.authorModified);
    const authorFirstname = useSelector(state => emptyStringWhenFalsy(state.authorFirstname));
    const authorLastname = useSelector(state => emptyStringWhenFalsy(state.authorLastname));
    const dispatch = useDispatch();

    return (
        <div>
            <nav className="navbar navbar-light bg-light">
                <StyledLinkLeft to="/authors">&nbsp;{text.toModifyAuthor}</StyledLinkLeft>
                <h1>{text.addAuthor}</h1>
                <Locale />
            </nav>
            <Container>
                <div className="row">&nbsp;</div>
                <div className="row text-center">
                    <div className="col-4">
                        <button type="button" className="btn btn-primary" onClick={() => dispatch(SAVE_ADD_AUTHOR_BUTTON_CLICKED())} disabled={!authorModified}>{text.saveAuthor}</button>
                    </div>
                    <div className="col-4">
                        <button type="button" className="btn btn-primary" onClick={() => dispatch(CANCEL_ADD_AUTHOR_BUTTON_CLICKED())}>{text.cancel}</button>
                    </div>
                </div>
            </Container>
            <form onSubmit={ e => { e.preventDefault(); }}>
                <Container>
                    <div className="row">&nbsp;</div>
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
