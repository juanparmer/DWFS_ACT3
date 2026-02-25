import React from "react";
import useRedirection from "../hooks/useRedirection";

export const LandingView = () => {
    useRedirection("/home", 5000);

    return (
        <div className="landing">
            <h1 className="landing__title">Relatos de Papel</h1>
            <div className="landing__card">
                <p>Bienvenido a tu librería digital. Cargando catálogo...</p>
                <div className="loader"></div>
            </div>
        </div>
    );
};