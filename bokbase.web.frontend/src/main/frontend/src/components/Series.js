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
    SELECT_SERIES,
    SAVE_SERIES_BUTTON_CLICKED,
    REMOVE_SERIES_BUTTON_CLICKED,
    SERIES_NAME_MODIFY,
} from '../reduxactions';
import Locale from './Locale';


function Series(props) {
    const {
        text,
        series,
        seriesModified,
        seriesId,
        seriesName,
        onSeriesSelect,
        onSeriesSave,
        onSeriesRemove,
        onSeriesNameModify,
    } = props;

    return (
        <div>
            <nav className="navbar navbar-light bg-light">
                <StyledLinkLeft to="/">&nbsp;{text.toTheBookshelf}</StyledLinkLeft>
                <h1>{text.modifySeries}</h1>
                <Locale />
            </nav>
            <Container>
                <div className="row">&nbsp;</div>
                <div className="row text-center">
                    <div className="col-4">
                        <button type="button" disabled={!seriesModified} className="btn btn-primary" onClick={onSeriesSave}>{text.saveSeries}</button>
                    </div>
                    <div className="col-4">
                        <button type="button" className="btn btn-primary" onClick={onSeriesRemove}>{text.removeSeries}</button>
                    </div>
                    <div className="col-4">
                        <Link className="btn btn-primary" to="/series/add">{text.addSeries}</Link>
                    </div>
                </div>
            </Container>
            <form onSubmit={ e => { e.preventDefault(); }}>
                <Container>
                    <div className="row">&nbsp;</div>
                    <FormRow>
                        <FormLabel htmlFor="users">{text.selectSeries}</FormLabel>
                        <FormField>
                            <select id="users" className="form-control" value={seriesId} onChange={onSeriesSelect}>
                                <option key="seriesNull" value=""></option>
                                {series.map((val) => <option key={'series' + val.seriesId} value={val.seriesId}>{val.name}</option>)}
                            </select>
                        </FormField>
                    </FormRow>
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
        loginresultat,
        series,
        seriesModified,
        seriesId,
        seriesName,
    } = state;
    const text = state.displayTexts;
    return {
        text,
        loginresultat,
        series,
        seriesModified,
        seriesId: emptyStringWhenFalsy(seriesId),
        seriesName: emptyStringWhenFalsy(seriesName),
    };
}

function mapDispatchToProps(dispatch) {
    return {
        onSeriesSelect: (e) => dispatch(SELECT_SERIES(e.target.value)),
        onSeriesSave: () => dispatch(SAVE_SERIES_BUTTON_CLICKED()),
        onSeriesRemove: () => dispatch(REMOVE_SERIES_BUTTON_CLICKED()),
        onSeriesNameModify: (e) => dispatch(SERIES_NAME_MODIFY(e.target.value)),
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(Series);
