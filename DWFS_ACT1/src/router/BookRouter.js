import React from "react";
import { Route, Routes, useLocation } from "react-router-dom";
import { LandingView } from "../views/LandingView";
import { HomeView } from "../views/HomeView";
import { BookDetailView } from "../views/BookDetailView";
import { Header } from "../components/Header";
import { Cart } from "../components/Cart";
import { Footer } from "../components/Footer";
import { CheckoutView } from "../views/CheckoutView";

export const BookRouter = ({ toggleCart, isCartOpen }) => {
    // Obtener la ubicación actual para detectar en qué página estamos
    const location = useLocation();
    
    // Verificar si estamos en la página de inicio (Landing)
    const isLandingPage = location.pathname === '/';

    return (
        <>
            {/* Renderizar Header y Carrito solo si NO estamos en la Landing */}
            {!isLandingPage && <Header onCartClick={toggleCart} />}
            {!isLandingPage && <Cart isOpen={isCartOpen} toggleCart={toggleCart} />}
            
            {/* Definición de todas las rutas de la aplicación */}
            <Routes>
                {/* Ruta principal - Página de bienvenida- Landing Page */}
                <Route path="/" element={<LandingView />} />
                
                {/* Ruta del catálogo de libros */}
                <Route path="/home" element={<HomeView />} />
                
                {/* Ruta de detalle de un libro específico (recibe id como parámetro) */}
                <Route path="/book/:id" element={<BookDetailView />} />

                {/* Checkout */}
                <Route path="/checkout" element={<CheckoutView />} /> 
            </Routes>

            {/* Footer visible en todas las páginas excepto Landing */}
            {!isLandingPage && <Footer />}
        </>
    );
};