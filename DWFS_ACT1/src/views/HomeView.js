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

    useEffect(() =>{
        // Voy a usar la direccion del microservicio ya no encuentro lo del gateway
        axios.get("http://localhost:8088/books")
        .then((response) => {
            setLibroApi(response.data.books);
            setLoading(false);
            console.log(response)
            
        })
        .catch((err) => {
            setError(err.message);
            setLoading(false)
        });
    }, [])

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
                        <Book key={book.id} book={book} />
                    ))
                ) : (
                    <p className="center">No se encontraron libros con ese título.</p>
                )}
            </div>
        </div>
    );
};