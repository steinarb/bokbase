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
    SELECT_PUBLISHER,
    SAVE_PUBLISHER_BUTTON_CLICKED,
    REMOVE_PUBLISHER_BUTTON_CLICKED,
    PUBLISHER_NAME_MODIFY,
} from '../reduxactions';
import Locale from './Locale';


function Publishers(props) {
    const {
        text,
        publishers,
        publisherModified,
        publisherId,
        publisherName,
        onPublisherSelect,
        onPublisherAdd,
        onPublisherRemove,
        onPublisherNameModify,
    } = props;

    return (
        <div>
            <nav className="navbar navbar-light bg-light">
                <StyledLinkLeft to="/">&nbsp;{text.toTheBookshelf}</StyledLinkLeft>
                <h1>{text.modifyPublisher}</h1>
                <Locale />
            </nav>
            <Container>
                <div className="row">&nbsp;</div>
                <div className="row text-center">
                    <div className="col-4">
                        <button type="button" disabled={!publisherModified} className="btn btn-primary" onClick={onPublisherAdd}>{text.savePublisher}</button>
                    </div>
                    <div className="col-4">
                        <button type="button" className="btn btn-primary" onClick={onPublisherRemove}>{text.removePublisher}</button>
                    </div>
                    <div className="col-4">
                        <Link className="btn btn-primary" to="/publishers/add">{text.addPublisher}</Link>
                    </div>
                </div>
            </Container>
            <form onSubmit={ e => { e.preventDefault(); }}>
                <Container>
                    <div className="row">&nbsp;</div>
                    <FormRow>
                        <FormLabel htmlFor="users">{text.selectPublisher}</FormLabel>
                        <FormField>
                            <select id="users" className="form-control" value={publisherId} onChange={onPublisherSelect}>
                                <option key="publisherNull" value=""></option>
                                {publishers.map((val) => <option key={'publisher' + val.publisherId} value={val.publisherId}>{val.name}</option>)}
                            </select>
                        </FormField>
                    </FormRow>
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
        publishers,
        publisherModified,
        publisherId,
        publisherName,
    } = state;
    const text = state.displayTexts;
    return {
        text,
        loginresultat,
        publishers,
        publisherModified,
        publisherId: emptyStringWhenFalsy(publisherId),
        publisherName: emptyStringWhenFalsy(publisherName),
    };
}

function mapDispatchToProps(dispatch) {
    return {
        onPublisherSelect: (e) => dispatch(SELECT_PUBLISHER(e.target.value)),
        onPublisherAdd: () => dispatch(SAVE_PUBLISHER_BUTTON_CLICKED()),
        onPublisherRemove: () => dispatch(REMOVE_PUBLISHER_BUTTON_CLICKED()),
        onPublisherNameModify: (e) => dispatch(PUBLISHER_NAME_MODIFY(e.target.value)),
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(Publishers);
