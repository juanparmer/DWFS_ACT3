import React, { useState } from 'react';
import { BrowserRouter } from 'react-router-dom';
import { BookContext } from './components/BookContext';
import { BookRouter } from './router/BookRouter';
import { booksData } from './data/booksData';

function App() {
  const [cart, setCart] = useState([]);
  const [isCartOpen, setIsCartOpen] = useState(false);

  const addToCart = (book) => {
    setCart(prevCart => {
      const existingItem = prevCart.find(item => item.id === book.id);
      if (existingItem) {
        return prevCart.map(item => 
          item.id === book.id 
            ? { ...item, quantity: item.quantity + 1 }
            : item
        );
      }
      return [...prevCart, { ...book, quantity: 1 }];
    });
  };

  const clearCart = () => {
    setCart([]);
  };

  const removeFromCart = (bookId) => {
    setCart(prevCart => prevCart.filter(item => item.id !== bookId));
  };

  const updateQuantity = (bookId, newQuantity) => {
    setCart(prevCart => 
      prevCart.map(item => 
        item.id === bookId 
          ? { ...item, quantity: newQuantity }
          : item
      )
    );
  };

  const toggleCart = () => {
    setIsCartOpen(!isCartOpen);
  };

  return (
    <BookContext.Provider value={{ booksData, cart, addToCart, removeFromCart, updateQuantity, clearCart}}>
      <BrowserRouter>
        <BookRouter toggleCart={toggleCart} isCartOpen={isCartOpen} />
      </BrowserRouter>
    </BookContext.Provider>
  );
}

export default App;