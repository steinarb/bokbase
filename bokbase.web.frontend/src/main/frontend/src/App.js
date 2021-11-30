import React, { Component } from 'react';
import { Switch, Route } from 'react-router-dom';
import { ConnectedRouter as Router } from 'connected-react-router';
import './App.css';
import Home from './components/Home';
import Books from './components/Books';
import BooksAdd from './components/BooksAdd';
import Authors from './components/Authors';
import AuthorsAdd from './components/AuthorsAdd';
import Publishers from './components/Publishers';
import PublishersAdd from './components/PublishersAdd';
import Series from './components/Series';
import SeriesAdd from './components/SeriesAdd';
import Login from './components/Login';
import Unauthorized from './components/Unauthorized';

class App extends Component {
    render() {
        const { history, basename } = this.props;

        return (
            <Router history={history} basename={basename}>
                <Switch>
                    <Route exact path="/" component={Home} />
                    <Route exact path="/books" component={Books} />
                    <Route exact path="/books/add" component={BooksAdd} />
                    <Route exact path="/authors" component={Authors} />
                    <Route exact path="/authors/add" component={AuthorsAdd} />
                    <Route exact path="/publishers" component={Publishers} />
                    <Route exact path="/publishers/add" component={PublishersAdd} />
                    <Route exact path="/series" component={Series} />
                    <Route exact path="/series/add" component={SeriesAdd} />
                    <Route exact path="/login" component={Login} />
                    <Route exact path="/unauthorized" component={Unauthorized} />
                </Switch>
            </Router>
        );
    }
}

export default App;
