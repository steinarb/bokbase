import React from 'react';
import { connect } from 'react-redux';
import { CSVLink } from "react-csv";
import Container from './bootstrap/Container';
import StyledLinkRight from './bootstrap/StyledLinkRight';
import { LOGOUT_REQUEST } from '../reduxactions';
import Locale from './Locale';


function Home(props) {
    const {
        text,
        csvdata,
        onLogout,
    } = props;

    return (
        <div>
            <nav className="navbar navbar-light bg-light">
                <h1>{text.theBookshelf}</h1>
                <Locale />
            </nav>
            <Container>
                <CSVLink data={csvdata}><span className="oi oi-spreadsheet"></span>&nbsp;{text.downloadGoodreadsCsv}!</CSVLink>
            </Container>
            <Container>
                <StyledLinkRight to="/books">{text.books}</StyledLinkRight>
                <StyledLinkRight to="/authors">{text.authors}</StyledLinkRight>
                <StyledLinkRight to="/publishers">{text.publishers}</StyledLinkRight>
                <StyledLinkRight to="/series">{text.series_plural}</StyledLinkRight>
                <p><button className="btn btn-primary" onClick={onLogout}>{text.logout}</button></p>
            </Container>
        </div>
    );
}

function mapStateToProps(state) {
    const text = state.displayTexts;
    const csvdata = state.csvdata;
    return {
        text,
        csvdata,
    };
}

function mapDispatchToProps(dispatch) {
    return {
        onLogout: () => dispatch(LOGOUT_REQUEST()),
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(Home);
