import { Route, Routes as Switch } from 'react-router-dom';
import TestScreen from './component/TestScreen';
import Layout from './layout/Layout';
import MainPage from './pages/MainPage';
import LoginPage from './pages/LoginPage';
import TestListPage from './pages/TestListPage';
import SignUpPage from './pages/SignUpPage';
import AnswerList from './component/AnswerList';

function App() {
  return (
    <div className="App">
      <Switch>
        <Route element={<Layout />}>
          <Route path="/" element={<MainPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/test" element={<TestListPage />} />
          <Route path="/test/:subject" element={<TestScreen />} />
          <Route path="/answer/:id" element={<AnswerList />} />
          <Route path="/signup" element={<SignUpPage />} />
        </Route>
      </Switch>
    </div>
  );
}

export default App;
