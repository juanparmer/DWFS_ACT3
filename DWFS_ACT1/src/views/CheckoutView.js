import React, { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { BookContext } from "../components/BookContext";

export const CheckoutView = () => {
    const { cart, clearCart } = useContext(BookContext);
    const navigate = useNavigate();

    // Dentro de CheckoutView
    const totalPrice = cart.reduce((total, item) => {
        const price = item.price || 19.99; // <--- El seguro de vida
        return total + (price * item.quantity);
    }, 0);

    const handleSubmit = (e) => {
        e.preventDefault();
        // Orden solicitado: Alerta -> Vaciar -> Redirigir
        alert("¡Pedido realizado con éxito! Tu libro está en camino.");
        clearCart();
        navigate("/home");
    };

    if (cart.length === 0) {
        return (
            <div className="container center">
                <h2>No hay productos para pagar</h2>
                <button className="btn-checkout" onClick={() => navigate("/home")}>Volver a la tienda</button>
            </div>
        );
    }

    return (
        <div className="container">
            <h1 className="center" style={{ marginBottom: '30px' }}>Finalizar Compra</h1>

            <div className="checkout-layout" style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '40px' }}>

                {/* RESUMEN DE PRODUCTOS */}
                <div className="checkout-summary">
                    <h3>Resumen del Pedido</h3>
                    <div className="cart-items">
                        {cart.map(item => {
                            const itemPrice = item.price || 19.99; // <--- Definimos la constante aquí
                            
                            return (
                                <div key={item.id} className="cart-item" style={{ flexDirection: 'row', justifyContent: 'space-between' }}>
                                    <div>
                                        <p style={{ margin: 0, fontWeight: 'bold' }}>{item.title}</p>
                                        {/* Mostramos el precio correcto */}
                                        <small>{item.quantity} ud. x {itemPrice.toFixed(2)}€</small>
                                    </div>
                                    {/* El subtotal de la línea ahora será correcto */}
                                    <span style={{ fontWeight: 'bold' }}>{(itemPrice * item.quantity).toFixed(2)}€</span>
                                </div>
                            );
                        })}
                    </div>
                    <div className="cart-total" style={{ marginTop: '20px', padding: '10px', borderTop: '2px solid #eee' }}>
                        <span>Total a pagar:</span>
                        <span className="cart-total__amount">{totalPrice.toFixed(2)}€</span>
                    </div>
                </div>

                {/* FORMULARIO DE PAGO */}
                <div className="checkout-form-container">
                    <h3 style={{ marginBottom: '20px' }}>Datos de Pago</h3>
                    <form onSubmit={handleSubmit} className="checkout-form">

                        <input
                            className="checkout-input"
                            type="text"
                            placeholder="Nombre del titular"
                            required
                        />

                        <input
                            className="checkout-input"
                            type="text"
                            placeholder="Número de Tarjeta (16 dígitos)"
                            maxLength="16"
                            required
                        />

                        <div className="checkout-row">
                            <input
                                className="checkout-input"
                                type="text"
                                placeholder="MM/AA"
                                maxLength="5"
                                required
                            />
                            <input
                                className="checkout-input"
                                type="text"
                                placeholder="CVV"
                                maxLength="3"
                                required
                            />
                        </div>

                        <button type="submit" className="btn-checkout">
                            CONFIRMAR PAGO DE {totalPrice.toFixed(2)}€
                        </button>
                    </form>
                </div>
            </div>
        </div>
    );
};