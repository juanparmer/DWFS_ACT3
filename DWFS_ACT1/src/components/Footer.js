import React from "react";
import { Link } from "react-router-dom";

export const Footer = () => {
    const currentYear = new Date().getFullYear();

    return (
        <footer className="footer">
            <div className="footer__content">
                <div className="footer__section">
                    <h3>Relatos de Papel</h3>
                    <p>Tu librería digital de confianza</p>
                </div>

                <div className="footer__section">
                    <h4>Enlaces rápidos</h4>
                    <ul className="footer__links">
                        <li><Link to="/home">Catálogo</Link></li>
                    </ul>
                </div>

                <div className="footer__section">
                    <h4>Contacto</h4>
                    <p>📧 info@relatosdepapel.com</p>
                    <p>📞 +34 912 345 678</p>
                    <p>📍 Madrid, España</p>
                </div>
            </div>

            <div className="footer__bottom">
                <p>&copy; {currentYear} Relatos de Papel. Todos los derechos reservados.</p>
            </div>
        </footer>
    );
};
