import React from 'react';
import { useSelector, useDispatch } from 'react-redux';
import { CSVLink } from "react-csv";
import Container from './bootstrap/Container';
import StyledLinkRight from './bootstrap/StyledLinkRight';
import { LOGOUT_REQUEST } from '../reduxactions';
import Locale from './Locale';

export default function Home() {
    const text = useSelector(state => state.displayTexts);
    const csvdata = useSelector(state => state.csvdata);
    const dispatch = useDispatch();

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
                <p><button className="btn btn-primary" onClick={() => dispatch(LOGOUT_REQUEST())}>{text.logout}</button></p>
            </Container>
        </div>
    );
}
