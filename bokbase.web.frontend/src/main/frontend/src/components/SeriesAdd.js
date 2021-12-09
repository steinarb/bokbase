import React from 'react';
import { connect } from 'react-redux';
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


function SeriesAdd(props) {
    const {
        text,
        seriesModified,
        seriesName,
        onSaveClicked,
        onCancelClicked,
        onSeriesNameModify,
    } = props;

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
                        <button type="button" className="btn btn-primary" onClick={onSaveClicked} disabled={!seriesModified}>{text.saveSeries}</button>
                    </div>                    <div className="col-4">
                        <button type="button" className="btn btn-primary" onClick={onCancelClicked}>{text.cancel}</button>
                    </div>
                </div>
            </Container>
            <form onSubmit={ e => { e.preventDefault(); }}>
                <Container>
                    <div className="row">&nbsp;</div>
                    <FormRow>
                        <FormLabel htmlFor="name">{text.seriesName}</FormLabel>
                        <FormField>
                            <input id="name" className="form-control" type="text" value={seriesName} onChange={onSeriesNameModify} />
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
        seriesModified,
        seriesName,
    } = state;
    const text = state.displayTexts;
    return {
        text,
        loginresult,
        seriesModified,
        seriesName: emptyStringWhenFalsy(seriesName),
    };
}

function mapDispatchToProps(dispatch) {
    return {
        onSaveClicked: () => dispatch(SAVE_ADD_SERIES_BUTTON_CLICKED()),
        onCancelClicked: () => dispatch(CANCEL_ADD_SERIES_BUTTON_CLICKED()),
        onSeriesNameModify: (e) => dispatch(SERIES_NAME_MODIFY(e.target.value)),
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(SeriesAdd);
