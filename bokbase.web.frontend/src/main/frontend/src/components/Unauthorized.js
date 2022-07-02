import React from 'react';
import { Redirect } from 'react-router';
import { connect } from 'react-redux';
import {
    LOGOUT_REQUEST,
} from '../reduxactions';
import Container from './bootstrap/Container';


function Unauthorized(props) {
    const { loginresult, text, onLogout } = props;
    const user = (loginresult || {}).user || {};
    const username = user;
    if (!loginresult.success) {
        return <Redirect to="/login" />;
    }

    return (
        <div>
            <nav className="navbar navbar-light bg-light">
                <a className="btn btn-primary left-align-cell" href="../.."><span className="oi oi-chevron-left" title="chevron left" aria-hidden="true"></span>&nbsp;{text.gohome}!</a>
                <h1>{text.noaccess}</h1>
                <div className="col-sm-2"></div>
            </nav>
            <Container>
                <p>{text.hi} {username}! {text.noaccessmessage1}</p>
                <p>{text.noaccessmessage2}</p>
                <form onSubmit={ e => { e.preventDefault(); }}>
                    <div className="form-group row">
                        <div className="col-5"/>
                        <div className="col-7">
                            <button className="btn btn-primary" onClick={onLogout}>{text.logout}</button>
                        </div>
                    </div>
                </form>
            </Container>
        </div>
    );
}

const mapStateToProps = state => {
    const { loginresult } = state;
    const text = state.displayTexts;
    return {
        loginresult,
        text,
    };
};

const mapDispatchToProps = dispatch => {
    return {
        onLogout: () => dispatch(LOGOUT_REQUEST()),
    };
};

export default connect(mapStateToProps, mapDispatchToProps)(Unauthorized);
