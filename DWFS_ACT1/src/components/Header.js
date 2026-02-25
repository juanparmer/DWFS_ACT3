import React, { useContext } from "react";
import { useNavigate } from "react-router-dom";
import { BookContext } from "./BookContext";

export const Header = ({ onCartClick }) => {
    const { cart } = useContext(BookContext);
    const navigate = useNavigate();
    
    // Calcular el total de items en el carrito
    const totalItems = cart.reduce((total, item) => total + item.quantity, 0);

    return (
        <header className="header">
            <h1 onClick={() => navigate('/home')}>Relatos de Papel</h1>
            <div className="cart-icon" onClick={onCartClick}>
                <span className="cart-icon__symbol">🛒</span>
                {totalItems > 0 && (
                    <span className="cart-icon__badge">{totalItems}</span>
                )}
            </div>
        </header>
    );
};