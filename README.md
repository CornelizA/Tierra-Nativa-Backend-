# ✈️ Tierra Nativa - Backend API.

El backend es el servicio **RESTful** desarrollado con **Spring Boot** para gestionar la lógica de negocio, persistencia
de datos y servir los endpoints para la aplicación de paquetes de viaje.

---

## ⚙️ Tecnologías

### ☕ Stack Principal

- **Java** (`^21`)
- **Spring Boot** (`^3.5.6`)
- **Spring Security**  (Autenticación y Autorización)
- **JSON Web Token (JWT)**  (Seguridad basada en estados/tokens)
- **Spring Data JPA**  (Persistencia)
- **Spring Boot Starter Web**  (Controladores REST)
- **H2 Database** (Base de Datos en memoria para desarrollo/testing)

### 🛠️ Herramientas de Desarrollo

- **Maven** (Gestión de dependencias)
- **JUnit 5 / Mockito** (Testing)
- **Lombok**  (Generación de código boilerplate)

---

## 🚀 Instalación y Ejecución Local

### 🧩 Requisitos previos

- `Java 21+`
- `Maven`

### 📦 Cloná el repositorio

```bash
git clone [https://github.com/CornelizA/Tierra-Nativa-Backend-.git]
cd Tierra-Nativa
```

### 🛠️ Correr el Backend

El proyecto está configurado para ejecutarse directamente con el plugin de Spring Boot, utilizando H2 como base de datos
en memoria (por defecto).

```
Bash
# Construir, compilar e instalar dependencias
./mvnw clean install

# Correr la aplicación
./mvnw spring-boot:run
```

El Backend estará disponible en http://localhost:8080.

---

## 🔐 Seguridad y Roles

#### La API implementa seguridad mediante Spring Security y JWT.

- **Público:** Acceso a consulta de paquetes y categorías.
- **Usuario Registrado:** Acceso a reservas, perfil personal y preferencias.
- **Admin:** Control total sobre el inventario (CRUD de paquetes, categorías, características y gestión de roles).

Nota: Para los endpoints protegidos, se debe incluir el header Authorization: Bearer <JWT_TOKEN>.

---

## 📬 Endpoints de la API REST

### 📦 Gestión de Paquetes

Gestión del catálogo principal de experiencias turísticas.

| Método | Endpoint         | Descripción                               | Acceso  |
|:-------|:-----------------|:------------------------------------------|:--------|
| GET    | `/paquetes`      | Obtiene el listado de todos los paquetes. | Público |
| GET    | `/paquetes/{id}` | Detalle de un paquete por ID.             | Público |
| POST   | `/paquetes`      | Crea un nuevo paquete de viaje.           | Admin   |
| PUT    | `/paquetes/{id}` | Editar paquetes de viaje.                 | Admin   |
| DELETE | `/paquetes/{id}` | Elimina un paquete de viaje.              | Admin   |

### 🏷️ Categorías

Organización de los paquetes por tipo de experiencia.

| Método | Endpoint                        | Descripción                                   | Acceso  |
|:-------|:--------------------------------|:----------------------------------------------|:--------|
| GET    | `/categories`                   | Listado de categorías disponibles.            | Público |
| GET    | `/categories/categoria/{title}` | Obtiene paquetes filtrados por categoría.     | Público |
| POST   | `/categories`                   | Crea una nueva categoría.                     | Admin   |
| PUT    | `/categories`                   | Editar categoría.                             | Admin   |
| DELETE | `/categories/{id}`              | Elimina una categoría sin paquetes asociados. | Admin   |

### 🏷️ Características

Gestión de servicios y atributos.

| Método | Endpoint                | Descripción                             | Acceso  |
|:-------|:------------------------|:----------------------------------------|:--------|
| GET    | `/characteristics`      | Listado de características disponibles. | Público |
| POST   | `/characteristics`      | Crea una nueva característica.          | Admin   |
| PUT    | `/characteristics`      | Editar característica.                  | Admin   |
| DELETE | `/characteristics/{id}` | Elimina una característica.             | Admin   |

### 👥 Autenticación y Usuarios

Permite la gestión de acceso a la plataforma. El registro incluye un flujo de verificación por correo electrónico.

| Método | Endpoint             | Descripción                                   | Acceso  |
|:-------|:---------------------|:----------------------------------------------|:--------|
| POST   | `/auth/register`     | Registro de nuevos usuarios.                  | Público |
| POST   | `/auth/login`        | Autenticación de usuarios y emisión de JWT.   | Público |
| POST   | `/auth/resend-email` | Reenvía el correo de bienvenida/confirmación. | Público |
| GET    | `/auth/verify-email` | Activa la cuenta mediante token de correo.    | Público |
| GET    | `/auth/admin/test`   | Test de validación de rol Admin               | Admin   |

### ❤️ Favoritos

Persistencia de preferencias del usuario.

| Método | Endpoint                        | Descripción                                     | Acceso  |
|:-------|:--------------------------------|:------------------------------------------------|:--------|
| POST   | `/favorites/toggle/{packageId}` | Agrega o quita un paquete de la lista personal. | Usuario |
| GET    | `/favorites`                    | Obtiene los paquetes marcados por el usuario.   | Usuario |

### 💬 Comentarios y Puntuaciones

Validación social y feedback de experiencias finalizadas.

| Método | Endpoint                       | Descripción                                  | Acceso  |
|:-------|:-------------------------------|:---------------------------------------------|:--------|
| POST   | `/reviews`                     | Crea una reseña (requiere viaje finalizado). | Usuario |
| PUT    | `/reviews/{id}`                | Edita puntuación/comentario (Solo el autor). | Usuario |
| DELETE | `/reviews/{id}`                | Elimina una reseña (Autor o Admin).          | Usuario |
| GET    | `/reviews/package/{packageId}` | Lista todas las opiniones de un paquete.     | Público |

### 📅 Reservas

Gestión de transacciones y cronograma de viajes.

| Método | Endpoint                | Descripción                                 | Acceso  |
|:-------|:------------------------|:--------------------------------------------|:--------|
| POST   | `/bookings`             | Registra una nueva reserva de viaje.        | Usuario |
| GET    | `/bookings/my-bookings` | Obtiene el historial de viajes del usuario. | Usuario |

### 🔍 Búsqueda y Disponibilidad

Motor de consulta dinámica.

| Método | Endpoint  | Descripción                                                     | Acceso  |
|:-------|:----------|:----------------------------------------------------------------|:--------|
| GET    | `/search` | Filtra por palabra clave y disponibilidad de fechas (Check-in). | Público |

---

## 🧪 Testing

El proyecto incluye pruebas unitarias y de integración, además de la configuración para generar documentación de la API.

```
Bash
./mvnw test
```

---

## 👤 Autores

Arianna Corneliz - @CornelizA

## 📞 Soporte

¿Encontraste un bug o tienes una sugerencia?

- 🐛 Reportar bug

- 📧 Email: ariannaesthefani@gmail.com
