import React from 'react';
import { connect } from 'react-redux';
import { emptyStringWhenFalsy } from './componentsCommon';
import Container from './bootstrap/Container';
import StyledLinkLeft from './bootstrap/StyledLinkLeft';
import FormRow from './bootstrap/FormRow';
import FormLabel from './bootstrap/FormLabel';
import FormField from './bootstrap/FormField';
import {
    SAVE_ADD_PUBLISHER_BUTTON_CLICKED,
    CANCEL_ADD_PUBLISHER_BUTTON_CLICKED,
    PUBLISHER_NAME_MODIFY,
} from '../reduxactions';
import Locale from './Locale';


function PublishersAdd(props) {
    const {
        text,
        publisherModified,
        publisherName,
        onAddClicked,
        onCancelClicked,
        onPublisherNameModify,
    } = props;

    return (
        <div>
            <nav className="navbar navbar-light bg-light">
                <StyledLinkLeft to="/publishers">&nbsp;{text.toModifyPublisher}</StyledLinkLeft>
                <h1>{text.addPublisher}</h1>
                <Locale />
            </nav>
            <Container>
                <div className="row">&nbsp;</div>
                <div className="row text-center">
                    <div className="col-4">
                        <button type="button" className="btn btn-primary" onClick={onAddClicked} disabled={!publisherModified}>{text.savePublisher}</button>
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
                        <FormLabel htmlFor="name">{text.publisherName}</FormLabel>
                        <FormField>
                            <input id="name" className="form-control" type="text" value={publisherName} onChange={onPublisherNameModify} />
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
        publisherModified,
        publisherName,
    } = state;
    const text = state.displayTexts;
    return {
        text,
        loginresultat,
        publisherModified,
        publisherName: emptyStringWhenFalsy(publisherName),
    };
}

function mapDispatchToProps(dispatch) {
    return {
        onAddClicked: () => dispatch(SAVE_ADD_PUBLISHER_BUTTON_CLICKED()),
        onCancelClicked: () => dispatch(CANCEL_ADD_PUBLISHER_BUTTON_CLICKED()),
        onPublisherNameModify: (e) => dispatch(PUBLISHER_NAME_MODIFY(e.target.value)),
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(PublishersAdd);
