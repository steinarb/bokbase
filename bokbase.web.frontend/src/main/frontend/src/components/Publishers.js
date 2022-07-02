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
    SELECT_PUBLISHER,
    SAVE_PUBLISHER_BUTTON_CLICKED,
    REMOVE_PUBLISHER_BUTTON_CLICKED,
    PUBLISHER_NAME_MODIFY,
} from '../reduxactions';
import Locale from './Locale';


export default function Publishers() {
    const text = useSelector(state => state.displayTexts);
    const publishers = useSelector(state => state.publishers);
    const publisherModified = useSelector(state => state.publisherModified);
    const publisherId = useSelector(state => emptyStringWhenFalsy(state.publisherId));
    const publisherName = useSelector(state => emptyStringWhenFalsy(state.publisherName));
    const dispatch = useDispatch();

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
                        <button type="button" disabled={!publisherModified} className="btn btn-primary" onClick={() => dispatch(SAVE_PUBLISHER_BUTTON_CLICKED())}>{text.savePublisher}</button>
                    </div>
                    <div className="col-4">
                        <button type="button" className="btn btn-primary" onClick={() => dispatch(REMOVE_PUBLISHER_BUTTON_CLICKED())}>{text.removePublisher}</button>
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
                            <select id="users" className="form-control" value={publisherId} onChange={e => dispatch(SELECT_PUBLISHER(e.target.value))}>
                                <option key="publisherNull" value=""></option>
                                {publishers.map((val) => <option key={'publisher' + val.publisherId} value={val.publisherId}>{val.name}</option>)}
                            </select>
                        </FormField>
                    </FormRow>
                    <FormRow>
                        <FormLabel htmlFor="name">{text.publisherName}</FormLabel>
                        <FormField>
                            <input id="name" className="form-control" type="text" value={publisherName} onChange={e => dispatch(PUBLISHER_NAME_MODIFY(e.target.value))} />
                        </FormField>
                    </FormRow>
                </Container>
            </form>
        </div>
    );
}
