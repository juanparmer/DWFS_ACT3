import React, { useContext, useState, useEffect } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { BookContext } from "../components/BookContext";
import axios from "axios";

export const BookDetailView = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { addToCart } = useContext(BookContext);
    
    const [book, setBook] = useState(null);
    const [loading, setLoading] = useState(true);
    const [showConfirmation, setShowConfirmation] = useState(false);

    useEffect(() => {
        setLoading(true);
        // IMPORTANTE: Quitamos http://localhost:8088 para usar el proxy
        axios.get(`/books/${id}`) 
            .then((response) => {
                console.log("Datos del libro recibidos:", response.data);
                
                // Verificamos que los datos existan
                if (response.data) {
                    // Si el libro viene con price null, le ponemos el default
                    const libroProcesado = {
                        ...response.data,
                        price: response.data.price || 19.99
                    };
                    setBook(libroProcesado);
                } else {
                    setBook(null);
                }
                setLoading(false);
            })
            .catch((err) => {
                console.error("Error al obtener el libro:", err);
                setBook(null);
                setLoading(false);
            });
    }, [id]);

    const handleAddToCart = () => {
        addToCart(book);
        setShowConfirmation(true);
        window.scrollTo({top: 0, behavior: 'smooth'});
        setTimeout(() => setShowConfirmation(false), 2000);
    };

    if (loading) return <div className="container">Cargando...</div>;

    if (!book) {
        return (
            <div className="container center">
                <h2>Libro no encontrado</h2>
                <button className="btn-back" onClick={() => navigate('/home')}>Volver</button>
            </div>
        );
    }

    return (
        <div className="container">
            <button className="btn-back" onClick={() => navigate('/home')}>
                ← Volver al catálogo
            </button>

            <div className="book-detail">
                <div className="book-detail__image">
                    <div className="book-cover">
                        <span className="book-cover__text">📚</span>
                    </div>
                </div>

                <div className="book-detail__info">
                    <h1 className="book-detail__title">{book.title}</h1>
                    <h2 className="book-detail__author">Por {book.author}</h2>

                    <div className="book-detail__description">
                        <h3>Descripción</h3>
                        <p>
                            {book.description || `Descubre "${book.title}", una obra de ${book.author}...`}
                        </p>
                    </div>

                    <div className="book-detail__metadata">
                        <div className="metadata-item">
                            <span className="metadata-label">ISBN:</span>
                            <span className="metadata-value">{book.isbn}</span>
                        </div>
                        <div className="metadata-item">
                            <span className="metadata-label">Stock disponible:</span>
                            <span className="metadata-value">{book.stock} unidades</span>
                        </div>
                    </div>

                    <div className="book-detail__purchase">
                        {/* OJO: Verifica si tu API devuelve 'precio'. En el JSON anterior no venía */}
                        <span className="book-detail__price">{book.price || '19.99'}€</span>
                        <button className="btn-add-to-cart" onClick={handleAddToCart}>
                            🛒 Añadir al carrito
                        </button>
                    </div>

                    {showConfirmation && (
                        <div className="cart-confirmation">✓ Libro añadido</div>
                    )}
                </div>
            </div>
        </div>
    );
};