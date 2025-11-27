// src/main.jsx (Đã sửa)

import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App.jsx';
import './index.css';
import { Provider } from 'react-redux';
import { store } from './store/store';
// Xóa mọi import liên quan đến Router (như BrowserRouter) ở đây

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <Provider store={store}>
            {/* KHÔNG bọc App bằng <BrowserRouter> ở đây */}
            <App />
        </Provider>
    </React.StrictMode>,
)