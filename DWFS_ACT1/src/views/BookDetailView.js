import React, { useContext, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { BookContext } from "../components/BookContext";
import { useEffect } from "react";
import axios from "axios";

export const BookDetailView = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { booksData, addToCart } = useContext(BookContext);
    const [showConfirmation, setShowConfirmation] = useState(false);

    // Buscar el libro por ID
    const [book, setBook] = useState(null); // Cambiado de [] a null
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        setLoading(true); // Asegúrate de resetear el loading al empezar
        axios.get(`http://localhost:8088/books/${id}`)
        .then((response) => {
            // CAMBIO AQUÍ: Guarda 'response.data' directamente
            setBook(response.data); 
            setLoading(false);
        })
        .catch((err) => {
            setError(err.message);
            setLoading(false);
        });
    }, [id]); // Es buena práctica añadir 'id' para que refresque si el usuario cambia de URL

    // 1. Si está cargando, no mostramos nada o un spinner
        if (loading) return <div className="container">Cargando...</div>;

        // 2. Si ya no carga pero 'book' sigue siendo null o no tiene title
        if (!book || !book.title) {
            return (
                <div className="container">
                    <h2>Libro no encontrado</h2>
                    <button onClick={() => navigate('/home')}>Volver</button>
                </div>
            );
        }

    const handleAddToCart = () => {

        //Aqui habria que llamar a la api de carrito
        addToCart(book);
        setShowConfirmation(true);
        window.scrollTo({top: 0,behavior: 'smooth'});
        setTimeout(() => {
            setShowConfirmation(false);
        }, 2000);
    };

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
                            {book.description || 
                            `Descubre "${book.title}", una obra maestra de ${book.author} que te transportará 
                            a un mundo de literatura excepcional. Este libro es una lectura imprescindible 
                            para los amantes de la buena literatura.`}
                        </p>
                    </div>

                    <div className="book-detail__metadata">
                        <div className="metadata-item">
                            <span className="metadata-label">ISBN:</span>
                            <span className="metadata-value">{book.isbn || '978-84-XXXXX-XX-X'}</span>
                        </div>
                        <div className="metadata-item">
                            <span className="metadata-label">Editorial:</span>
                            <span className="metadata-value">{book.publisher || 'Editorial Planeta'}</span>
                        </div>
                        <div className="metadata-item">
                            <span className="metadata-label">Año:</span>
                            <span className="metadata-value">{book.year || '2023'}</span>
                        </div>
                        <div className="metadata-item">
                            <span className="metadata-label">Páginas:</span>
                            <span className="metadata-value">{book.pages || '350'}</span>
                        </div>
                    </div>

                    <div className="book-detail__purchase">
                        <span className="book-detail__price">{book.precio}€</span>
                        <button 
                            className="btn-add-to-cart" 
                            onClick={handleAddToCart}
                        >
                            🛒 Añadir al carrito
                        </button>
                    </div>

                    {showConfirmation && (
                        <div className="cart-confirmation">
                            ✓ Libro añadido al carrito
                        </div>
                    )}
                </div>
            </div>
        </div>
    );
};
