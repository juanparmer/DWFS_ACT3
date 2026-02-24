# Caso de Uso: API Books – Catálogo de Libros

## Descripción

API REST para la gestión de un catálogo de libros.  
Permite crear, buscar, consultar, actualizar y eliminar libros como recursos independientes.

La API soporta filtros de búsqueda dinámicos y actualizaciones completas o parciales de los recursos.  
El diseño se centra en los principios REST y no contempla detalles de implementación interna.

---

## Recursos Identificados

- **/books**  
  Representa la colección de libros del catálogo.

- **/books/{id}**  
  Representa un libro específico identificado de forma única.

---

## Operaciones

### Buscar libros

| Método HTTP | URI | Query Params | Cuerpo de la Petición | Cuerpo de la Respuesta (JSON) | Códigos de Respuesta |
|------------|-----|--------------|-----------------------|-------------------------------|----------------------|
| GET | /books | `title` (opcional)<br>`author` (opcional)<br>`category` (opcional)<br>`publicationDate` (opcional)<br>`isbn` (opcional)<br>`rating` (opcional)<br>`stock` (opcional) | - | `[ { "id": 1, "title": "Libro", "author": "Autor", "isbn": "978-84-123456-7-8", "publicationDate": "2023-01-01", "rating": 4.5, "stock": 10, "visible": true } ]` | 200 Correcto |


---

### Crear un libro

| Método HTTP | URI | Query Params | Cuerpo de la Petición (JSON) | Cuerpo de la Respuesta (JSON) | Códigos de Respuesta |
|------------|-----|--------------|------------------------------|-------------------------------|----------------------|
| POST | /books | - | `{ "title": "Ejemplo", "author": "Autor", "category": "Ficción", "visible": true }` | `{ "id": 1, "title": "Ejemplo", "author": "Autor", "category": "Ficción", "visible": true }` | 201 Creado<br>400 Solicitud incorrecta |

---

### Consultar un libro por ID

| Método HTTP | URI | Query Params | Cuerpo de la Petición | Cuerpo de la Respuesta (JSON) | Códigos de Respuesta |
|------------|-----|--------------|-----------------------|-------------------------------|----------------------|
| GET | /books/{id} | - | - | `{ "id": 1, "title": "Ejemplo", "author": "Autor", "category": "Ficción", "visible": true }` | 200 Correcto<br>404 No encontrado |

---

### Actualizar un libro (reemplazo completo)

| Método HTTP | URI | Query Params | Cuerpo de la Petición (JSON) | Cuerpo de la Respuesta (JSON) | Códigos de Respuesta |
|------------|-----|--------------|------------------------------|-------------------------------|----------------------|
| PUT | /books/{id} | - | `{ "title": "Nuevo título", "author": "Nuevo autor", "category": "Drama", "visible": false }` | `{ "id": 1, "title": "Nuevo título", "author": "Nuevo autor", "category": "Drama", "visible": false }` | 200 Correcto<br>400 Solicitud incorrecta<br>404 No encontrado |

---

### Actualización parcial de un libro

| Método HTTP | URI | Query Params | Cuerpo de la Petición (JSON) | Cuerpo de la Respuesta (JSON) | Códigos de Respuesta |
|------------|-----|--------------|------------------------------|-------------------------------|----------------------|
| PATCH | /books/{id} | - | `{ "visible": false }` | `{ "id": 1, "title": "Ejemplo", "author": "Autor", "category": "Ficción", "visible": false }` | 200 Correcto<br>404 No encontrado |

---

### Eliminar un libro

| Método HTTP | URI | Query Params | Cuerpo de la Petición | Cuerpo de la Respuesta | Códigos de Respuesta |
|------------|-----|--------------|-----------------------|-----------------------|----------------------|
| DELETE | /books/{id} | - | - | - | 204 Sin contenido<br>404 No encontrado |

---

