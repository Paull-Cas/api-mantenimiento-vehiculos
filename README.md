# API de mantenimiento de vehículos

API REST desarrollada en un **proyecto académico grupal de Ingeniería de Sistemas** para gestionar clientes, vehículos, talleres, mecánicos, mantenimientos, órdenes de trabajo y pagos.

## Funcionalidades

- Gestión de clientes naturales y jurídicos, vehículos, talleres y mecánicos.
- Registro de órdenes, asignación de mecánicos y seguimiento de estados.
- Registro de pagos asociados a las órdenes de trabajo.
- Componentes de registro e inicio de sesión con Spring Security y JWT.
- Exportación RDF y consultas SPARQL con Apache Jena y Fuseki.
- Colección de Postman para revisar y probar los endpoints.

## Herramientas

| Tecnología | Versión o uso |
| --- | --- |
| Java | 25 |
| Spring Boot | 3.5.6 |
| Spring Web, Spring Data JPA, Spring Security y Bean Validation | Versiones administradas por Spring Boot |
| Auth0 java-jwt | 4.5.0 |
| Apache Jena | 4.10.0 |
| MySQL | Persistencia relacional |
| Maven y Postman | Construcción y pruebas manuales |

## Arquitectura y organización

El backend utiliza **arquitectura por capas** en una aplicación Spring Boot:

```text
src/main/java/.../
├── web/            Controladores REST
├── dtos/           Datos de entrada y salida
├── mappers/        Conversión entre entidades y DTOs
├── services/       Lógica de negocio e integración semántica
├── repositories/   Acceso a datos con JPA
├── entities/       Modelo del dominio
├── enums/          Estados y tipos
├── exceptions/     Excepciones del dominio
├── config/         Configuración de seguridad
└── util/           Utilidades JWT
```

## Ejecución local

Requisitos: JDK 25, MySQL y Maven o el wrapper incluido. Las funciones semánticas utilizan Apache Jena Fuseki local; sus rutas están definidas en los servicios de integración.

Configurar estas variables en el entorno o en el IDE:

| Variable | Contenido |
| --- | --- |
| `SPRING_DATASOURCE_URL` | URL JDBC de la base MySQL local |
| `SPRING_DATASOURCE_USERNAME` | Usuario de desarrollo |
| `SPRING_DATASOURCE_PASSWORD` | Contraseña de desarrollo |
| `SECURITY_JWT_KEY_PRIVATE` | Clave HMAC propia del entorno de prueba, de al menos 32 bytes |

El archivo `.env.example` enumera las variables. Spring Boot no carga un archivo `.env` automáticamente: hay que exportarlas o configurarlas en el IDE.

En Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

En Linux/macOS:

```sh
./mvnw spring-boot:run
```

La API utiliza el puerto 8080. La configuración académica usa `ddl-auto: update` y no incluye carga automática de registros personales o cuentas de ejemplo.

## Revisar y probar el código

1. Explorar `web/` para conocer rutas, métodos y validaciones.
2. Seguir cada operación hacia `services/`, `mappers/` y `repositories/`.
3. Importar `Proyecto API Mantenimiento Vehiculos  Completo.postman_collection.json` en Postman, ajustar las URLs locales y crear registros ficticios propios.
4. Para las solicitudes de autenticación, definir `demo_password` y `jwt_token` en un entorno local de Postman. Los roles requeridos deben existir en la base de desarrollo.

| Método | Ruta | Operación |
| --- | --- | --- |
| POST | `/auth/sign-up` | Registrar un usuario |
| POST | `/auth/log-in` | Iniciar sesión |
| GET | `/api/vehiculos/Vehiculos` | Listar vehículos |
| GET | `/api/vehiculos/Vehiculo/{id}` | Consultar un vehículo |
| POST | `/api/vehiculos/Vehiculo` | Crear un vehículo |
| GET | `/api/taller` | Listar talleres |
| GET | `/api/mecanicos` | Listar mecánicos |
| GET | `/api/mantenimiento-vehiculos` | Listar mantenimientos |
| GET | `/api/semantica/catalogo` | Consultar el catálogo semántico |

## Participación y estado

Desarrollé esta API como parte de un equipo en el curso Software III. El historial conserva las contribuciones de **Paull-Cas, DanielaM202, Jhordan27 y Dany7mx**.

Es una entrega académica disponible para revisar el código. La configuración actual permite todas las rutas (`permitAll`); los componentes JWT están implementados, pero la autorización completa de endpoints está pendiente. Utilizar únicamente en un entorno local de pruebas.
