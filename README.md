📚 Relatos de Papel - Librería Digital
Relatos de Papel es una aplicación de comercio electrónico para libros construida con React. Permite a los usuarios explorar un catálogo dinámico, buscar libros por título, ver detalles específicos y gestionar un carrito de compras hasta el proceso de checkout.

🚀 Características
Catálogo Dinámico: Consumo de API REST en tiempo real desde el puerto 8088.

Búsqueda Avanzada: Filtrado de libros mediante parámetros de consulta (?title=).

Gestión de Carrito: Añadir, eliminar y actualizar cantidades con persistencia de datos.

Checkout Simulado: Formulario de pago completo con validación de campos.

Manejo de Errores: Sistema de "fallback" que asigna precios automáticos (19.99€) si la API devuelve valores nulos.

🛠️ Tecnologías Utilizadas
Frontend: React.js (Hooks, Context API, React Router v6).

HTTP Client: Axios para comunicación asíncrona.

Proxy: Configuración de desarrollo para evitar bloqueos de CORS.

🔧 Configuración e Instalación
1. Prerrequisitos
Tener instalado Node.js y asegurar que el servidor backend esté activo en http://localhost:8088.

2. Instalación
Bash
npm install
3. Configuración del Proxy
El archivo package.json debe incluir la siguiente línea para que las peticiones al backend funcionen correctamente:
"proxy": "http://127.0.0.1:8088"

4. Ejecución
Bash
npm start
📖 Estructura del Proyecto
/src/components: Componentes como Book.js, Cart.js y el BookContext.js.

/src/views: Vistas de HomeView, BookDetailView y CheckoutView.

/src/styles: Archivos CSS para el diseño visual.

⚠️ Notas Importantes
CORS: Si los libros no cargan a la primera, asegúrate de haber reiniciado el servidor de React después de añadir el proxy al package.json.

Precios: Se ha implementado una lógica de seguridad en el Frontend para que, en caso de que un libro no tenga precio definido en la base de datos, se muestre y calcule con un valor de 19.99€, evitando errores de NaN en el carrito.

Creado por el equipo de Relatos de Papel - 2024
