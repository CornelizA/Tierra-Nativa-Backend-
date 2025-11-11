
# ✈️  Tierra Nativa - Backend API.

El backend es el servicio **RESTful** desarrollado con **Spring Boot** para gestionar la lógica de negocio, persistencia de datos y servir los endpoints para la aplicación de paquetes de viaje.

---


## ⚙️ Tecnologías

### ☕ Stack Principal
- **Java** (`^21`)
- **Spring Boot** (`^3.5.6`)
- **Spring Data JPA**  (Persistencia)
- **Spring Boot Starter Web**  (Controladores REST)
- **Lombok**  (Generación de código boilerplate)
- **H2 Database** (Base de Datos en memoria para desarrollo/testing)

### 🛠️ Herramientas de Desarrollo

- **Maven** (Gestión de dependencias)
- **JUnit 5 / Mockito** (Testing)

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
El proyecto está configurado para ejecutarse directamente con el plugin de Spring Boot, utilizando H2 como base de datos en memoria (por defecto).
```
Bash
# Construir, compilar e instalar dependencias
./mvnw clean install

# Correr la aplicación
./mvnw spring-boot:run
```
El Backend estará disponible en http://localhost:8080.

### 📬 Endpoints de la API REST


| Método | Endpoint | Descripción                                                   | Acceso |
| :--- | :--- |:--------------------------------------------------------------| :--- |
| GET | `/paquetes` | Obtiene el listado público de todos los paquetes.             | Público |
| GET | `/paquetes/admin` | Obtiene el listado completo para la administración.           | Admin |
| GET | `/paquetes/{id}` | Detalle de un paquete por ID.                                 | Público |
| GET | `/paquetes/categoria/{category}` | Filtra paquetes por categoría.                                | Público |
| POST | `/paquetes` | Registra un nuevo paquete de viaje.                           | Admin |
| PUT | `/paquetes` | Actualiza un paquete existente (requiere el ID en el cuerpo). | Admin |
| DELETE | `/paquetes/{id}` | Elimina un paquete por ID.                                    | Admin |


## 🧪 Testing

El proyecto incluye pruebas unitarias y de integración, además de la configuración para generar documentación de la API.
```
Bash
./mvnw test
```

## 👤 Autores
@CornelizA

## 📞 Soporte
¿Encontraste un bug o tienes una sugerencia?

- 🐛 Reportar bug

- 📧 Email: ariannaesthefani@gmail.com