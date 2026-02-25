import React from 'react';
import ReactDOM from 'react-dom/client';
import App from './App';
import './styles/home.css';
import './styles/cart.css';
import './styles/bookDetail.css';
import './styles/footer.css';
import './styles/checkout.css'; 

const root = ReactDOM.createRoot(document.getElementById('root'));
root.render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
);