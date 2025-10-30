Proyecto de Notas (Full-Stack con Spring Boot y Angular)

Este proyecto es una aplicación web completa (full-stack) para tomar notas, que incluye funcionalidad de registro, inicio de sesión (JWT), gestión de notas CRUD y la opción de compartir notas con otros usuarios.

Utiliza un backend de API REST construido con Java y Spring Boot, y un frontend de "Single Page Application" (SPA) construido con Angular.

El diseño del proyecto permite que el frontend compilado sea servido directamente por el backend de Spring Boot, creando una aplicación unificada que se ejecuta desde un único puerto.

Tecnologías Utilizadas

Backend (API REST):

Java (JDK 21+): Lenguaje de programación.

Spring Boot (v3.5+): Framework principal.

Spring Security: Gestión de seguridad y autenticación.

JSON Web Tokens (JWT): Mecanismo de autenticación Stateless.

Spring Data JPA: Capa de persistencia para la base de datos.

MySQL: Base de datos relacional.

Lombok: Para reducir el código repetitivo (Getters, Setters, DTOs).

Frontend (Cliente - SPA):

Angular (v17+): Framework para la interfaz de usuario (UI).

TypeScript: Lenguaje de programación.

Angular Router: Gestión de navegación del lado del cliente (SPA).

jwt-decode: Utilidad para leer el JWT en el cliente.

SCSS / CSS

1. Prerrequisitos

Asegúrate de tener instalado y configurado lo siguiente:

Java JDK (versión 21 o superior)

Maven (Gestor de dependencias de Java)

Node.js y npm (para el frontend de Angular)

MySQL Server (Base de datos local o remota)

2. Configuración del Backend (Java / Spring Boot)

El backend expone la API en el prefijo /api (ej. /api/auth/login).

A. Configuración de la Base de Datos y JWT

Abre el archivo src/main/resources/application.properties y configura las credenciales de tu base de datos y la clave secreta de JWT:

# Configuración de la Base de Datos
spring.datasource.url=jdbc:mysql://localhost:3306/notes_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update

# ¡CRUCIAL! Clave para firmar y validar JWT
# Esta clave debe ser larga, compleja y no compartirse.
jwt.secret-key=TuClaveSecretaSuperLargaYComplejaParaJWT


(Nota: Si no incluyes jwt.secret-key, tu aplicación fallará al iniciar).

B. Funcionalidad Clave del Backend

Seguridad: Utiliza JwtAuthFilter.java para interceptar todas las peticiones a /api/** y validar el token antes de permitir el acceso.

Enrutamiento SPA: SpaRoutingController.java asegura que las URL de Angular (ej. /notes/1) se redirijan siempre al index.html correcto, evitando errores 404 al refrescar la página.

3. Configuración del Frontend (Angular)

A. Gestión de URLs (Proxy)

Durante el desarrollo, el servidor de Angular utiliza un proxy para evitar errores de CORS.

Asegúrate de que tu archivo proxy.conf.json esté en la raíz del proyecto.

Al iniciar, el servidor de Angular redirige las peticiones de /api/** a http://localhost:8080.

B. Interceptor de Seguridad

token.interceptor.ts inyecta automáticamente el token JWT (guardado en localStorage) en el encabezado Authorization: Bearer <token> de todas las peticiones salientes a /api/.

4. Ejecución del Proyecto (Desarrollo)

Para una experiencia completa y conectada:

Iniciar Backend (API):
Abra una terminal en la carpeta notes-api (donde está el pom.xml) y ejecute:

mvn spring-boot:run


Iniciar Frontend (SPA con Proxy):
Abra otra terminal en la carpeta notes-frontend y ejecute:

# Este comando activa el proxy para conectar al puerto 8080
ng serve --proxy-config proxy.conf.json
