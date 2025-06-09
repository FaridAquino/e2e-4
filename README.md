[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/0_pV7_o9)
# 📋 Proyecto E2E - Entrega 4 Autorización y Autenticación

## Descripción 💡

Este laboratorio se centra en añadir la capa de autenticación y autorización a la aplicación de la entrega anterior.
Para ello utilizaremos Spring Security y JWT.

En esta entrega deberás:

- Proteger endpoints por roles.
- Implementar autenticación y autorización con JWT.
- Implementar el servicio de JWT para manipular los tokens.
- Crear los endpoints de login `/auth/login` y registro `/auth/register` en un controlador con el mapping `/auth`.

## Requerimientos 📋

Este repositorio tiene ya en el `pom.xml` las dependencias necesarias para trabajar con Spring Security y JWT.
Spring Security es el mismo y asegura la totalidad de los endpoints por defecto.
Por parte de JWT, se tiene la dependencia `java-jwt` de auth0, si estás familiarizado con otra implementación puedes
utilizarla, solo asegúrate que el funcionamiento en conjunto con Spring Security genere los resultados detallados aquí.

## Evaluación 🚀

En la evaluación de este laboratorio se interpretan tus configuraciones de Spring Security y JWT como una caja negra,
solo se evaluará el comportamiento de los endpoints. Es decir, tu solución debe rechazar peticiones no autorizadas y
solo debe permitir las autorizadas. Esto no significa que puedes hardcodear las respuestas, ya que los test hacen uso de
Spring Security por debajo.

Los test verifican las siguientes situaciones:

1. Usuario con un rol permitido pueda acceder a un endpoint definido para ese rol.
2. Usuario con un rol no permitido no puede acceder a un endpoint definido para un rol distinto.
3. Usuario no autenticado no pueda acceder a ningun endpoint protegido.
4. Retornar los errores adecuados cuando roles permitidos, pero que no sean dueños del recurso tratan de modificar un
   recurso ajeno.
5. Los endpoints `/auth/login` y `/auth/register` funcionen correctamente y generen un token JWT válido.

### Hints 🤓

- DTO para los endpoits de login y registro:

    - `/auth/login` request:
        ```json
        {
            "username": "admin",
            "password": "admin"
        }
        ```
    - `/auth/login` response:
        ```json
        {
            "token": "eyJhbGciOiJIU..."
        } 
        ```
    - `/auth/register` request:
        ```json
        {
          "firstName": "John",
          "lastName": "Doe",
          "email": "johndoe@example.com",
          "password": "mysecretpassword",
          "phone": "123-456-7890",
          "isDriver": true,
          "category": "X",
          "vehicle": {
              "brand": "Toyota",
              "model": "Camry",
              "licensePlate": "ABC124",
              "fabricationYear": 2018,
              "capacity": 5,
              "color": "Blue"
            }
        }
      ```
    - `/auth/register` response:
        ```json
        {
          "token": "eyJhb..."
        }
        ```


- En los `service`, se han dejadon métodos en los cuales se define el email del usuario como `String email = "email"`.
  En estos casos, es necesario acceder al correo electrónico del usuario autenticado utilizando Spring Security.
  Para manejar esto, así como lo mencionado en el punto 4 de la sección de Evaluación🚀, investige qué es
  `SecurityContext` y cómo funciona `SecurityContextHolder`.


- Respecto a este último punto, hay situaciones en las cuales una solicitud no será rechazada automáticamente
  si el usuario tiene un rol permitido. Por ende, se requiere implementar y manejar excepciones personalizadas,
  como se hizo 2 laboratorios atrás.

### Roles permitidos por endpoints

#### Auth Controller

- Todos los endpoints de este controlador están abiertos para cualquier usuario.

#### Driver Controller

| Endpoint                  | Roles Aceptados   |
|---------------------------|-------------------|
| GET /driver/{id}          | DRIVER, PASSENGER |
| GET /driver/me            | DRIVER            |
| DELETE /driver/me         | DRIVER            |
| PATCH /driver/me          | DRIVER            | 
| PATCH /driver/me/location | DRIVER            |
| PATCH /driver/me/car      | DRIVER            |

#### Passenger Controller

| Endpoint                                   | Roles Aceptados   |
|--------------------------------------------|-------------------|
| GET /passenger/{id}                        | PASSENGER, DRIVER |
| GET /passenger/me                          | PASSENGER         |
| DELETE /passenger/me                       | PASSENGER         |
| PATCH /passenger/me                        | PASSENGER         |
| POST /passenger/me/places                  | PASSENGER         |
| GET /passenger/me/places                   | PASSENGER         |
| DELETE /passenger/me/places/{coordinateId} | PASSENGER         |

#### Review Controller

| Endpoint            | Roles Aceptados |
|---------------------|-----------------|
| POST /review        | PASSENGER       |
| DELETE /review/{id} | PASSENGER       |

#### Ride Controller

Aquí tienes la tabla de los endpoints con los roles aceptados:

| Endpoint                         | Roles Aceptados   |
|----------------------------------|-------------------|
| POST /ride                       | PASSENGER         |
| PATCH /ride/assign/{rideId}      | DRIVER            |
| PATCH /ride/{id}/status/{status} | DRIVER, PASSENGER |
| GET /ride/me                     | PASSENGER         |
