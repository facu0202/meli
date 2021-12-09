#  Meetup Manager
Repo for the meetup manger api. Aplicación desarrollada para Santander Tecnología.

## Ejecutar con 
Clonar el repo y dentro del directorio del repo ejecutar:

cd meetup

./mvnw spring-boot:run


## Swagger url
http://localhost:9290/swagger-ui.html#/

## Data base url
http://localhost:9290/h2-console/login.jsp

## Back-end Tech Challenge 

Tenemos un proveedor de cerveza que nos vende caja de 6 unidades de birra.
El problema es que si hace entre 20 y 24 grados, se toma una birra por persona, si se hace menos de 20 grados se toman 0.75, si hace más de 24 se toman 2 por persona.
Siempre se espera que sobre y no que falte.


1- Como admin se quiere saber cuantas birras se deben comprar para un meetup.

2- Como admin y como usuario quiero conocer la temperatura para saber si va a hacer calor o no.

3- Como usuario y como administrador se quiere recibir notificaciones de las meetup.

4- Como admin quiero crear meet para invitar personas.

5- Como usuario quiero inscribirme en una meetup para asistir.

6- Como usuario quiero hacer check-in en una meetup para avisar que estuve ahí. 

## Funcionalidad implementada 

Login con usuario y password

Registro de nuevos usuarios

Confirmación de registro

Creación de nuevas meetup

Registro en meetup

Confirmación de registro

Notificaciones de meetup a administradores

Notificaciones de meetup a usuarios

Cálculo de temperatura para una meetup

Cálculo de cantidad de cerveza para una meetup

Cerrado de meetup antiguas.

## Consideraciones del cálculo de cerveza

En el desafío se dio el siguiente enunciado para el cálculo:
Una persona toma 0.75 si hace menos de 20 grados, 1 si hace entre 20 y 24 y 2 si la temperatura es mayor a 24.
Ahora bien, en esta implementación todo decimal se redondea al entero superior en la cantidad que bebe cada asistente, esto se debe al siguiente análisis:
 
 Si vienen 2 personas un día de frío indica que tomarán 0.75, 0.75 * 2 es 1.5.
 Comprariamos 2 cervezas.
 
 
 Si vienen 3 personas un día de frío, sumaria 2.25. Compramos 3 cervezas.
 
 Si vienen 4 personas un día de frío, sumaria 3, y en este caso deberían compartir 3 cervezas entre 4 personas.
 
Por ese razonamiento y teniendo en cuenta que se planteó que más vale que sobre y no que falte, si alguien toma 0.75 en realidad tomará 1, si toma 1.25 sería 2, y así.


## Descripción tecnología utilizada 
Desarrolle una aplicación con spring boot, utiliza JWT para seguridad y H2 como base de datos.
Se agregó cache a ciertos servicios para mejorar la carga a servicios externos.
La integración con api externa se desarrolló con RestTemplate con política básica de reintentos y recuperación.
Se utilizó fox para que se auto genere la documentación swagger.


##Testing

Se incluyeron test Junit

En **../postman/postman_collection.json** se provee una colección postman que permite probar todas las funciones con la seguridad correspondiente. 
La misma se puede probar utilizando newman o desde posman.

Utiliza varias variables globales, que crea de forma automática, pero se requiere configurar:
 
**SANTANDER_BASE = localhost:9290**

Incluí también en los archivos para newman la configuración de las variables globales, lo pueden ejecutar con:

**newman run postman_collection.json -g enviroment.json -n 10 --bail newman**

Se realizaron 100 iteraciones:



![Alt text](img/imagen_result_test.png?raw=true "Resultado de test")




