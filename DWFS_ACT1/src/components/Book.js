import React from "react";
import { useNavigate } from "react-router-dom";

export const Book = ({ book }) => {
    const navigate = useNavigate();

    const handleClick = () => {
        navigate(`/book/${book.id}`);
    };

    return (
        <div className="book-card" onClick={handleClick}>
            <h3 className="book-card__title">{book.title}</h3>
            <p className="book-card__author">{book.author}</p>
            <span className="book-card__price">{book.price || 19.99}€</span>
            
        </div>
    );
};
