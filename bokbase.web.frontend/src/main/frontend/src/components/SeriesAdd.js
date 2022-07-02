import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { emptyStringWhenFalsy } from './componentsCommon';
import Container from './bootstrap/Container';
import StyledLinkLeft from './bootstrap/StyledLinkLeft';
import FormRow from './bootstrap/FormRow';
import FormLabel from './bootstrap/FormLabel';
import FormField from './bootstrap/FormField';
import {
    SAVE_ADD_SERIES_BUTTON_CLICKED,
    CANCEL_ADD_SERIES_BUTTON_CLICKED,
    SERIES_NAME_MODIFY,
} from '../reduxactions';
import Locale from './Locale';


export function SeriesAdd() {
    const text = useSelector(state => state.displayTexts);
    const seriesModified = useSelector(state => state.seriesModified);
    const seriesName = useSelector(state => emptyStringWhenFalsy(state.seriesName));
    const dispatch = useDispatch();

    return (
        <div>
            <nav className="navbar navbar-light bg-light">
                <StyledLinkLeft to="/series">&nbsp;{text.toModifySeries}</StyledLinkLeft>
                <h1>{text.addSeries}</h1>
                <Locale />
            </nav>
            <Container>
                <div className="row">&nbsp;</div>
                <div className="row text-center">
                    <div className="col-4">
                        <button type="button" className="btn btn-primary" onClick={() => dispatch(SAVE_ADD_SERIES_BUTTON_CLICKED())} disabled={!seriesModified}>{text.saveSeries}</button>
                    </div>                    <div className="col-4">
                        <button type="button" className="btn btn-primary" onClick={() => dispatch(CANCEL_ADD_SERIES_BUTTON_CLICKED())}>{text.cancel}</button>
                    </div>
                </div>
            </Container>
            <form onSubmit={ e => { e.preventDefault(); }}>
                <Container>
                    <div className="row">&nbsp;</div>
                    <FormRow>
                        <FormLabel htmlFor="name">{text.seriesName}</FormLabel>
                        <FormField>
                            <input id="name" className="form-control" type="text" value={seriesName} onChange={e => dispatch(SERIES_NAME_MODIFY(e.target.value))} />
                        </FormField>
                    </FormRow>
                </Container>
            </form>
        </div>
    );
}
