# Actividad 1: Desarrollo de Front-end con React - "Relatos de Papel"

## 📌 Descripción del Proyecto
Este proyecto forma parte del desarrollo transversal del máster, enfocado en la plataforma **Relatos de Papel**. En esta primera etapa, se ha desarrollado una interfaz de usuario (Front-end) completa, reactiva y funcional utilizando el ecosistema de React, sentando las bases para la futura integración con microservicios y motores de búsqueda.

## 🎯 Objetivos Académicos Cumplidos

* **Desarrollo Front-end**: Implementación de una interfaz moderna utilizando HTML5, CSS3, JavaScript (ES6+) y React.
* **Arquitectura de Componentes**: Estructuración del proyecto en componentes reutilizables y modulares.
* **Gestión de Estado Complejo**: Uso de **Context API** para la gestión global del carrito de compras y la disponibilidad de datos de libros.
* **Enrutamiento SPA**: Configuración de rutas dinámicas y navegación programática con `react-router-dom`.

## 🛠️ Implementación Técnica del Checkout

Siguiendo las directrices de la actividad para el proceso de compra satisfactorio, se ha implementado la vista `CheckoutView` con el siguiente flujo lógico:

1.  **Resumen de Compra**: Visualización de productos, cantidades y cálculo automático del total.
2.  **Formulario de Pago**: Captura de datos de tarjeta de crédito con validaciones de interfaz.
3.  **Lógica de Finalización (Orden estricto)**:
    -   `alert()`: Notificación inmediata de éxito del pedido.
    -   `clearCart()`: Vaciado del estado global del carrito.
    -   `Maps('/home')`: Redirección del usuario al catálogo principal.

## 📂 Estructura del Repositorio

```text
src/
├── components/     # Componentes de UI (Header, Cart, BookCard)
├── context/        # Lógica de BookContext (Estado Global)
├── data/           # Datos fuente (booksData.js)
├── router/         # Configuración de BookRouter y navegación
├── views/          # Vistas de la aplicación (Landing, Home, Detail, Checkout)
└── App.js          # Configuración del Provider y BrowserRouter