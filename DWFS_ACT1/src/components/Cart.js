import React, { useContext } from "react";
import { BookContext } from "./BookContext";
import { useNavigate } from "react-router-dom";

export const Cart = ({ isOpen, toggleCart }) => {
    // const { cart, removeFromCart, updateQuantity } = useContext(BookContext);
    const { cart, removeFromCart, updateQuantity, clearCart } = useContext(BookContext);
    const navigate = useNavigate();

    const totalPrice = cart.reduce((total, item) => total + (item.price * item.quantity), 0);

    const handleCheckout = () => {
        if (cart.length === 0) {
            alert("El carrito está vacío");
            return;
        }

        // alert("¡Pedido realizado con éxito! Gracias por su compra.");
        // clearCart();
        // toggleCart();
        // navigate("/home");

        toggleCart(); // Cerrar el panel lateral
        navigate("/checkout");
    };

    const handleQuantityChange = (bookId, newQuantity) => {
        if (newQuantity < 1) {
            removeFromCart(bookId);
        } else {
            updateQuantity(bookId, newQuantity);
        }
    };

    return (
        <>
            <div className={`cart-overlay ${isOpen ? 'cart-overlay--active' : ''}`} onClick={toggleCart}></div>
            <div className={`cart-panel ${isOpen ? 'cart-panel--open' : ''}`}>
                <div className="cart-panel__header">
                    <h2>🛒 Mi Carrito</h2>
                    <button className="cart-panel__close" onClick={toggleCart}>✕</button>
                </div>

                <div className="cart-panel__content">
                    {cart.length === 0 ? (
                        <div className="cart-empty">
                            <p>Tu carrito está vacío</p>
                            <span className="cart-empty__icon">📚</span>
                        </div>
                    ) : (
                        <>
                            <div className="cart-items">
                                {cart.map((item) => (
                                    <div key={item.id} className="cart-item">
                                        <div className="cart-item__info">
                                            <h4 className="cart-item__title">{item.title}</h4>
                                            <p className="cart-item__author">{item.author}</p>
                                            <span className="cart-item__price">{item.price}€</span>
                                        </div>

                                        <div className="cart-item__actions">
                                            <div className="cart-item__quantity">
                                                <button 
                                                    className="quantity-btn"
                                                    onClick={() => handleQuantityChange(item.id, item.quantity - 1)}
                                                >
                                                    −
                                                </button>
                                                <span className="quantity-value">{item.quantity}</span>
                                                <button 
                                                    className="quantity-btn"
                                                    onClick={() => handleQuantityChange(item.id, item.quantity + 1)}
                                                >
                                                    +
                                                </button>
                                            </div>

                                            <button 
                                                className="cart-item__remove"
                                                onClick={() => removeFromCart(item.id)}
                                                title="Eliminar del carrito"
                                            >
                                                🗑️
                                            </button>
                                        </div>

                                        <div className="cart-item__subtotal">
                                            Subtotal: {(item.price * item.quantity).toFixed(2)}€
                                        </div>
                                    </div>
                                ))}
                            </div>

                            <div className="cart-panel__footer">
                                <div className="cart-total">
                                    <span className="cart-total__label">Total:</span>
                                    <span className="cart-total__amount">{totalPrice.toFixed(2)}€</span>
                                </div>
                                <button className="btn-checkout" onClick={handleCheckout}>
                                    Proceder al Checkout
                                </button>
                            </div>
                        </>
                    )}
                </div>
            </div>
        </>
    );
};
