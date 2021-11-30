import React from 'react';
import { connect } from 'react-redux';
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


function AuthorsAdd(props) {
    const {
        text,
        authorModified,
        authorFirstname,
        authorLastname,
        onAddClicked,
        onCancelClicked,
        onAuthorFirstnameModify,
        onAuthorLastnameModify,
    } = props;

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
                        <button type="button" className="btn btn-primary" onClick={onAddClicked} disabled={!authorModified}>{text.saveAuthor}</button>
                    </div>
                    <div className="col-4">
                        <button type="button" className="btn btn-primary" onClick={onCancelClicked}>{text.cancel}</button>
                    </div>
                </div>
            </Container>
            <form onSubmit={ e => { e.preventDefault(); }}>
                <Container>
                    <div className="row">&nbsp;</div>
                    <FormRow>
                        <FormLabel htmlFor="firstname">{text.firstname}</FormLabel>
                        <FormField>
                            <input id="firstname" className="form-control" type="text" value={authorFirstname} onChange={onAuthorFirstnameModify} />
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="lastname">{text.lastname}</FormLabel>
                        <FormField>
                            <input id="lastname" className="form-control" type="text" value={authorLastname} onChange={onAuthorLastnameModify} />
                        </FormField>
                    </FormRow>
                </Container>
            </form>
        </div>
    );
}

function mapStateToProps(state) {
    const {
        loginresultat,
        authorModified,
        authorFirstname,
        authorLastname,
    } = state;
    const text = state.displayTexts;
    return {
        text,
        loginresultat,
        authorModified,
        authorFirstname: emptyStringWhenFalsy(authorFirstname),
        authorLastname: emptyStringWhenFalsy(authorLastname),
    };
}

function mapDispatchToProps(dispatch) {
    return {
        onAddClicked: () => dispatch(SAVE_ADD_AUTHOR_BUTTON_CLICKED()),
        onCancelClicked: () => dispatch(CANCEL_ADD_AUTHOR_BUTTON_CLICKED()),
        onAuthorFirstnameModify: (e) => dispatch(AUTHOR_FIRSTNAME_MODIFY(e.target.value)),
        onAuthorLastnameModify: (e) => dispatch(AUTHOR_LASTNAME_MODIFY(e.target.value)),
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(AuthorsAdd);
