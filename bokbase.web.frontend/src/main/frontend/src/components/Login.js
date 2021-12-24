import React from 'react';
import { connect } from 'react-redux';
import { Redirect } from 'react-router';
import {
    USERNAME_MODIFY,
    PASSWORD_MODIFY,
    LOGIN_REQUEST,
} from '../reduxactions';
import LoginMessage from './LoginMessage';

function Login(props) {
    const { username, password, loginresult, text, onUsernameEndre, onPasswordEndre, onSendLogin } = props;
    if (loginresult.success) {
        const originalRequestUrl = loginresult.originalRequestUrl || '/';
        return (<Redirect to={originalRequestUrl} />);
    }

    return (
        <div className="Login">
            <header>
                <div className="pb-2 mt-4 mb-2 border-bottom bg-light">
                    <h1>Bokbase login</h1>
                    <p id="messagebanner"></p>
                </div>
            </header>
            <div className="container">
                <LoginMessage/>
                <form onSubmit={e => { e.preventDefault(); }}>
                    <div className="form-group row">
                        <label htmlFor="username" className="col-form-label col-3 mr-2">{text.username}:</label>
                        <div className="col-8">
                            <input id="username" className="form-control" type="text" name="username" value={username} onChange={e => onUsernameEndre(e.target.value)} />
                        </div>
                    </div>
                    <div className="form-group row">
                        <label htmlFor="password" className="col-form-label col-3 mr-2">{text.password}:</label>
                        <div className="col-8">
                            <input id="password" className="form-control" type="password" name="password" value={password} onChange={e => onPasswordEndre(e.target.value)}/>
                        </div>
                    </div>
                    <div className="form-group row">
                        <div className="offset-xs-3 col-xs-9">
                            <input className="btn btn-primary" type="submit" value="Login" onClick={() => onSendLogin(username, password)}/>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    );
}

function mapStateToProps(state) {
    const { username, password, loginresult } = state;
    const text = state.displayTexts;
    return {
        username,
        password,
        loginresult,
        text,
    };
}

function mapDispatchToProps(dispatch) {
    return {
        onUsernameEndre: (username) => dispatch(USERNAME_MODIFY(username)),
        onPasswordEndre: (password) => dispatch(PASSWORD_MODIFY(password)),
        onSendLogin: (username, password) => dispatch(LOGIN_REQUEST({ username, password })),
    };
}

export default connect(mapStateToProps, mapDispatchToProps)(Login);
