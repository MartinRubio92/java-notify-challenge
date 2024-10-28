# Java Notification Processor Challenge

## Objetivo

El objetivo de este challenge es evaluar tus habilidades en Programación Orientada a Objetos (POO), uso de patrones de diseño, validaciones, manejo de errores y optimización de performance.

---

## Descripción del Challenge

Se te proporciona un código base para terminar de completar el desarrollo de un procesador de notificaciones. Este procesador interactúa con una API externa para enviar notificaciones a contactos especificados.

---

### API
Se pone a disposición una API que expone 3 endpoints necesarios
para realizar el challenge.

Se advierte que la API puede presentar eventualmente problemas temporales de congestion.

#### Endpoint: Obtener contactos
**url**: http://notify.showvlad.com/api/contact/:id  
**method**: GET  
**response**: {id: String, name: String, surname: String, email:String, phoneNumber: String}

#### Endpoint: Enviar notificacion SMS
**url**: http://notify.showvlad.com/api/notify/sms  
**method**: POST  
**body**: {destination: String, message: String}  
**response**: {status: String, uuid: String}

#### Endpoint: Enviar notificacion EMAIL
**url**: http://notify.showvlad.com/api/notify/email  
**method**: POST  
**body**: {destination: String, message: String}  
**response**: {status: String, uuid: String}

---

### Clases Proporcionadas

1. **ContactProvider** Implementa una integración básica a la API HTTP del proveedor de contactos.
    - **Método `getContact(id)`**: Obtiene los datos del contacto mediante su ID. Retorna el nombre, apellido, email y teléfono del contacto.

2. **SmsProvider** Implementa una integración básica a la API HTTP del proveedor de notificaciones SMS.
   - **Método `notify(destination, message)`**: Envía notificaciones de tipo SMS.

3. **EmailProvider** Implementa una integración básica a la API HTTP del proveedor de notificaciones Email.
   - **Método `notify(destination, message)`**: Envía notificaciones de tipo email.

4. **NotifyRepository** Provee una lista de notificaciones a procesar.
    - **Método `getNotifications()`**: Retorna una lista de notificaciones que deben ser procesadas. Cada notificación incluye  `contactId` (identificador de contacto), `type` (Tipo de notificación: email|sms) y `message` (mensaje a enviar).

5. **NotifyService** Se ocupa de procesar y enviar las notificaciones.
    - **Método `processNotifications()`**: Obtiene las notificaciones del `NotifyRepository`, las recorre e invoca al método `dispatchNotification()` para cada una. Además, contabiliza las notificaciones procesadas, las enviadas y la duración del procesamiento.
    - **Método `dispatchNotification(type, contactId, message)`**: Actualmente lanzará una excepción indicando que la implementación aún no está hecha. Parte de tu tarea será completar este método.

   
### Problemas Intencionales

El challenge incluye algunos problemas a considerar:

- Validación de datos
- Posibilidad de errores en integración HTTP
- Delay en integración HTTP

## Instrucciones

1. Descarga el código base proporcionado.
2. Implementa las clases, interfaces y patrones que creas conveniente para manejar los diferentes tipos de notificaciones.
3. Implementa la lógica necesaria en el método `dispatchNotification()` de la clase `NotifyService` para procesar cada notificación.
4. Ajusta la lógica necesaria en el método `processNotification()` de la clase `NotifyService` para mejorar la performance.
5. Asegúrate de manejar adecuadamente los errores y optimizar el rendimiento.
6. Realiza las pruebas necesarias para verificar el correcto funcionamiento de tu implementación.
7. Envía tu solución para su revisión.

## Requisitos

- Java 17 o superior.
- Maven para la gestión de dependencias.

### TEST

En la seccion de test hay una prueba que verificará:

- Si todas las notificaciones fueron procesadas.
- Si se enviaron las notificaciones esperadas.
- Si el tiempo de procesamiento es óptimo.


## Valoraciones

- Se valora que se puedan procesar todas las notificaciones
- Se valora realizar validaciones
- Se valora el manejo de errores
- Se valora que se envíen todas las notificaciones que cumplan los requisitos del proveedor
- Se valora contemplar problemas temporales de congestion del proveedor de sms/email
- Se valora la optimización de los tiempos de procesamiento (ej: Cache, Paralelización)
- Se valora la implementación de patrones de diseño (ej: Factory Method, Strategy, Singleton, etc)
- Se valora la creación de test adicionales


## Entrega

Por favor, sube tu solución a un repositorio público de GitHub y comparte el enlace para que pueda ser revisada.

---

¡Buena suerte y esperamos ver tus habilidades en acción!
