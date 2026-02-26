import React, { useState, useEffect, useCallback } from "react";
import { Book } from "../components/Book";
import axios from "axios";

export const HomeView = () => {
    const [searchTerm, setSearchTerm] = useState("");
    const [libroApi, setLibroApi] = useState([]);
    const [loading, setLoading] = useState(true);

    // Envolvemos la función en useCallback para que sea estable
    const fetchBooks = useCallback(async () => {
        setLoading(true);
        try {
            // Usamos la ruta relativa gracias al proxy de package.json
            // Si el término es vacío, vamos a /books directamente
            const url = searchTerm.trim() 
                ? `/books?title=${encodeURIComponent(searchTerm)}` 
                : "/books";

            const response = await axios.get(url);
            
            // Estructura según tu Postman: response.data.books
            const rawBooks = response.data.books || [];

            const processedBooks = rawBooks.map(book => ({
                ...book,
                // Si el precio es null o no existe, ponemos 19.99
                price: (book.price !== null && book.price !== undefined) ? book.price : 19.99
            }));

            setLibroApi(processedBooks);
        } catch (err) {
            console.error("Error en la carga:", err);
            setLibroApi([]);
        } finally {
            setLoading(false);
        }
    }, [searchTerm]); // Se recrea si cambia el buscador

    // Se ejecuta al montar el componente Y cuando cambia el searchTerm
    useEffect(() => {
        fetchBooks();
    }, [fetchBooks]);

    return (
        <div className="container">
            <div className="search-bar center">
                <input
                    type="text"
                    className="search-input"
                    placeholder="Buscar por título de libro..."
                    value={searchTerm}
                    onChange={(e) => setSearchTerm(e.target.value)}
                />
            </div>

            <div className="book-grid">
                {loading ? (
                    <p className="center">Cargando catálogo...</p>
                ) : libroApi.length > 0 ? (
                    libroApi.map((book) => (
                        <Book key={book.id} book={book} />
                    ))
                ) : (
                    <div className="center">
                        <p>No se encontraron libros para "{searchTerm}".</p>
                        <button className="btn-retry" onClick={fetchBooks}>
                            Reintentar Carga
                        </button>
                    </div>
                )}
            </div>
        </div>
    );
};