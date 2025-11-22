import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import './App.css'
import { useRoutes } from 'react-router-dom';
import routes from './routes';
import LoginPage from './auth/LoginPage';
import RegisterPage from './auth/RegisterPage';
import PrivateRoute from './auth/PrivateRoute';

function App() {
  const [count, setCount] = useState(0)
  const element = useRoutes(routes);

  return (
    <div className="min-h-screen bg-gray-100">
      {element}
    </div>
  );
}

export default App
