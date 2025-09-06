#  Guía del Proyecto - Prueba Técnica

**Autor:** Alejandro Quintero Alzate
**Fecha:** [06/09/2025]

Este README contiene toda la información necesaria para **comprender, configurar y ejecutar el proyecto** desarrollado en la prueba técnica. No incluye código ni archivos, solo instrucciones y normas.

---

##  Contenido

- [Objetivo del Proyecto](#-objetivo-del-proyecto)
- [Normas y Buenas Prácticas](#-normas-y-buenas-prácticas)
- [Prerrequisitos](#-prerrequisitos)
- [Pasos para levantar el proyecto](#-pasos-para-levantar-el-proyecto)
- [Postman y pruebas de servicios](#-postman-y-pruebas-de-servicios)
- [Credenciales de prueba](#-credenciales-de-prueba)
- [Comandos útiles](#-comandos-útiles)
- [Endpoints del Backend](#-endpoints-del-backend)
- [Notas finales](#-notas-finales)

---

##  Objetivo del Proyecto

El proyecto consiste en implementar un sistema **fullstack** con **Java (Spring Boot)** en el backend y **Angular** en el frontend.  
El objetivo es demostrar:

- Conocimiento en **desarrollo backend y frontend**.
- Manejo de **APIs REST** y **servicios web**.
- Implementación de **pruebas y documentación** para consumo de servicios.

---

##  Normas y Buenas Prácticas

1. Seguir las buenas prácticas de **codificación en Java y Angular**.
2. Mantener la consistencia en nombres de variables y funciones.
3. Documentar correctamente los endpoints y funcionalidades.
4. Probar siempre los servicios con **Postman** antes de entregar.
5. No subir credenciales sensibles al repositorio público.

---

## ️ Prerrequisitos

Antes de ejecutar el proyecto, se requiere:

- **Java** versión 21
- **Maven** versión 4.0
- **Node.js** versión 18+
- **Angular CLI** versión 19
- Base de datos compatible con el proyecto (PostgreSQL 14)
- **Postman** para probar los servicios web

---

##  Pasos para levantar el proyecto

1. **Clonar los repositorios** del frontend y backend.

- Backend: [playlist-backend](https://github.com/4lejandroquintero/playlist-backend)
- Frontend: [playlist-front](https://github.com/4lejandroquintero/playlist-front)
 
2. **Instalar dependencias**:
    - Backend: `mvn clean install`
    - Frontend: `npm install`
3. **Levantar los servidores**:
    - Backend: `mvn spring-boot:run`
    - Frontend: `ng serve -o`
4. Acceder a la aplicación en el navegador en los puertos configurados.

> Nota: Revisar los archivos de configuración que ya existen en el proyecto para asegurar la correcta conexión a la base de datos y servicios.
> Usar sus credenciales de postgres en el application.properties.

---

##  Postman y pruebas de servicios

1. Importar la colección de Postman: [Abrir colección en Postman](https://psaaa1.postman.co/workspace/My-Workspace~6ba26114-e8b3-4aa0-887a-cc63185bbf90/collection/42863393-cd49ce5c-d0df-48b0-acf9-d5fb3d876abd?action=share&source=copy-link&creator=42863393)

---

##  Credenciales de prueba

| Usuario        | Contraseña |
|----------------|------------|
| admin          | admin123   |
| alejo          | alejo123   |

---

##  Comandos útiles

- Backend:
    - `mvn clean install` → Compila el proyecto
    - `mvn spring-boot:run` → Ejecuta el backend

- Frontend:
    - `npm install` → Instala dependencias
    - `ng serve -o` → Levanta el frontend en modo desarrollo

- Logs:
    - Consultar logs del backend para depuración

---

##  Endpoints del Backend

Todos los endpoints están bajo la ruta base: `/lists`

| Método | Endpoint                        | Descripción                                           | Request Body / Params                          | Respuesta                  |
|--------|---------------------------------|-------------------------------------------------------|-----------------------------------------------|----------------------------|
| POST   | `/lists`                        | Crear una nueva playlist                              | `Playlist` JSON                                | Playlist creado            |
| POST   | `/lists/{listName}/songs`       | Agregar una canción a una playlist existente         | `Cancion` JSON                                 | Playlist actualizada       |
| GET    | `/lists`                        | Obtener todas las playlists                           | -                                             | Lista de playlists         |
| GET    | `/lists/{listName}`             | Obtener una playlist por su nombre                    | -                                             | Playlist encontrada        |
| GET    | `/lists/search?name={name}`     | Buscar playlists cuyo nombre contenga un texto       | Query param: `name`                            | Lista de playlists         |
| DELETE | `/lists/{listName}`             | Eliminar una playlist por su nombre                   | -                                             | 204 No Content             |

> ⚠ Nota: Todos los endpoints permiten **CORS** desde `http://localhost:4200` para que Angular pueda consumirlos sin problemas.

---

##  Notas finales

- Este README sirve como **guía de uso y referencia** para cualquier persona que desee levantar y probar el proyecto.
- Se recomienda **seguir los pasos en orden** para evitar errores de configuración.
- Mantener la documentación actualizada y ordenada facilita la revisión de la prueba técnica.

---

**¡Proyecto listo para ejecutar y revisar! **
