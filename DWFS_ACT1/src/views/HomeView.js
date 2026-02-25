import React, { useContext, useState } from "react";
import { BookContext } from "../components/BookContext";
import { Book } from "../components/Book";
import axios from "axios"
import { useEffect } from "react";

export const HomeView = () => {
    const { booksData } = useContext(BookContext);
    const [searchTerm, setSearchTerm] = useState("");

    const [libroApi, setLibroApi]  = useState([])
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        setLoading(true);
        
        const url = searchTerm 
            ? `http://localhost:8088/books?title=${searchTerm}` 
            : "http://localhost:8088/books";

        axios.get(url)
            .then((response) => {
                const librosDeLaApi = response.data.books || [];
                
                // Mapeamos los libros para asegurar que todos tengan precio
                const librosConPrecio = librosDeLaApi.map(book => ({
                    ...book,
                    precio: book.price || 19.99 // Si no existe book.precio, pone 19.99
                }));

                setLibroApi(librosConPrecio);
                setLoading(false);
            })
            .catch((err) => {
                console.error("Error buscando libros:", err);
                setLoading(false);
            });
    }, [searchTerm]);

    // Filtrado por título de libro
    const filteredBooks = booksData.filter(book =>
        book.title.toLowerCase().includes(searchTerm.toLowerCase())
    );

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
                {libroApi.length > 0 ? (
                    libroApi.map((book) => (
                        // Pasamos una copia del libro con el precio asegurado
                        <Book key={book.id} book={{...book, precio: book.precio || 19.99}} />
                    ))
                ) : (
                    <p className="center">No se encontraron libros.</p>
                )}
            </div>
        </div>
    );
};