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
- **Usuario Registrado:** Acceso a reservas y perfil personal.
- **Admin:** Control total sobre el inventario (CRUD de paquetes, categorías, características y gestión de roles).

Nota: Para los endpoints protegidos, se debe incluir el header Authorization: Bearer <JWT_TOKEN>.

---

## 📬 Endpoints de la API REST

### 📦 Gestión de Paquetes

Gestión del catálogo principal de experiencias turísticas.

| Método | Endpoint          | Descripción                                         | Acceso  |
|:-------|:------------------|:----------------------------------------------------|:--------|
| GET    | `/paquetes`       | Obtiene el listado público de todos los paquetes.   | Público |
| GET    | `/paquetes/admin` | Obtiene el listado completo para la administración. | Admin   |
| GET    | `/paquetes/{id}`  | Detalle de un paquete por ID.                       | Público |
| POST   | `/paquetes`       | Crea un nuevo paquete de viaje.                     | Admin   |
| PUT    | `/paquetes/{id}`  | Editar paquetes de viaje.                           | Admin   |
| DELETE | `/paquetes/{id}`  | Elimina un paquete de viaje.                        | Admin   |

### 🏷️ Categorías

Organización de los paquetes por tipo de experiencia.

| Método | Endpoint                        | Descripción                               | Acceso  |
|:-------|:--------------------------------|:------------------------------------------|:--------|
| GET    | `/categorias/public`            | Listado de categorías disponibles.        | Público |
| GET    | `/categorias/categoria/{title}` | Obtiene paquetes filtrados por categoría. | Público |
| GET    | `/categorias`                   | Listado de categorías para admin.         | Admin   |
| POST   | `/categorias`                   | Crea una nueva categoría.                 | Admin   |
| PUT    | `/categorias`                   | Editar categoría.                         | Admin   |
| DELETE | `/categorias/{id}`              | Elimina una categoría.                    | Admin   |

### 🏷️ Características

Gestión de servicios y atributos.

| Método | Endpoint                  | Descripción                             | Acceso  |
|:-------|:--------------------------|:----------------------------------------|:--------|
| GET    | `/caracteristicas/public` | Listado de características disponibles. | Público |
| GET    | `/caracteristicas`        | Listado de características para admin.  | Admin   |
| POST   | `/caracteristicas`        | Crea una nueva característica.          | Admin   |
| PUT    | `/caracteristicas`        | Editar característica.                  | Admin   |
| DELETE | `/caracteristicas/{id}`   | Elimina una característica.             | Admin   |

### 👥 Autenticación y Usuarios

Permite la gestión de acceso a la plataforma. El registro incluye un flujo de verificación por correo electrónico.

| Método | Endpoint             | Descripción                                   | Acceso  |
|:-------|:---------------------|:----------------------------------------------|:--------|
| POST   | `/auth/register`     | Registro de nuevos usuarios.                  | Público |
| POST   | `/auth/login`        | Autenticación de usuarios y emisión de JWT.   | Público |
| POST   | `/auth/resend-email` | Reenvía el correo de bienvenida/confirmación. | Público |
| GET    | `/auth/verify-email` | Activa la cuenta mediante token de correo.    | Público |
| GET    | `/auth/admin/test`   | Test de validación de rol Admin               | Admin   |

### 👥 Administración de Usuarios

Gestión de cuentas y niveles de privilegio.

| Método | Endpoint      | Descripción                                | Acceso |
|:-------|:--------------|:-------------------------------------------|:-------|
| GET    | `/admin`      | Listado de usuarios.                       | Admin  |
| PUT    | `/admin/role` | Editar role de de usuario (Admin/Usuario). | Admin  |

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