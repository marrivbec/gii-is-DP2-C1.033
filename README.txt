# README.txt
#
# Copyright (C) 2012-2025 Rafael Corchuelo.
#
# In keeping with the traditional purpose of furthering education and research, it is
# the policy of the copyright owner to permit non-commercial use and redistribution of
# this software. It has been tested carefully, but it is not guaranteed for any particular
# purposes.  The copyright owner does not offer any warranties or representations, nor do
# they accept any liabilities with respect to them.

This is a starter project.  It is intended to be a core learning asset for the students
who have enrolled the Design and Testing subject of the Software Engineering curriculum of the 
University of Seville.  This project helps them start working on their new information system 
projects.

To get this project up and running, please follow the guideline in the theory/lab materials,
taking into account that you must link the appropriate version of the Acme-Framework excluding 
the following resources:

- **/fragments/**


-**/Board/**
https://github.com/users/marrivbec/projects/1

- **/Deciones de diseño/**
- **/Student#4/**
Haciendo referencia al foro, al mensaje [D02-S04-05] escrito por MARTA DE LA CALLE GONZÁLEZ escrito el "March 12, 2025 2:34:40 PM CET". Se ha implementado el atributo “last update moment” de los objetos de tipo “Tracking log” como readonly, el cual se actualiza con la fecha actual cada vez que se actualiza un tracking log. Se ha implementado así según la respuesta ofrecida por "RAFAEL CORCHUELO GIL" en la fecha "March 13, 2025 10:16:15 AM CET".
        "A1: no me queda claro a qué “fecha” se refiere.  Entiendo que se refiere Ud. a usar el atributo “last update moment” de los objetos de tipo “Tracking log”.  Esto no funcionaría dado que dicho atributo se actualiza cada vez que se actualiza un  tracking log.  Por ejemplo: imagine que ahora mismo tiene un par de tracking logs del estilo (2025/01/01 00:00, 25%, “vla, vla”) y (2025/01/01 00:01, 50%, “bla, bla”).  Dentro de un rato descubre que el texto del primero está mal y lo corrige, de forma que ahora sus tracking logs son los siguientes: (2025/01/01 00:01, 50%, “bla, bla”) y (2025/01/01 00:02, 25%, “bla, bla”).  Es decir, ha perdido Ud. el orden y la secuencia sería incorrecta, cuando realmente es correcta, sólo que no la está Ud. ordenando bien."

- **/Student#3/**
Haciendo referencia al foro, al mensaje "[D&T] Duda sobre el requisito individual 15 del S3" escrito por "ÁNGEL SÁNCHEZ RUIZ" el "6/27/25 1:01 AM":
En el requisito suplementario 15 se han implementado las estadísticas del dashboard teniendo en cuenta los meses del último año (los 12 meses anteriores a la fecha prestablecida por el Framework). Es decir todas las legs que cumplan la condición de que la propiedad scheduledArrival (entendiendo que los flightAssignment que han tenido en el último año son aquellos que han acabado y han tenido lugar en el mes de dicha propiedad) se encuentren en el intervalo de tiempo mencionado, se considerarán válidas para formar parte de las estadísticas. 

Haciendo referencia al foro. al mensaje "[C2-S03-R08 & R09] Duda sobre creación de activity logs" escrito por MANUEL JESUS NIZA COBO, el "6/26/25 12:42 AM". Tenemos en cuenta varias decisiones de diseño: 
- En el formulario de los flightAssignment se ha añadido información adicional sobre la leg seleccionada en dicho flightAssignment. En el formulario de creación no aparecerán dichos detalles para no sobrecargar de información la pantalla, unicamente en el show que será donde se muestren todos los detalles de la asignación.

- Por otro lado, los detalles del FlightCrewMember se reducirán a que solamente aparezca su nombre, ya que como se indica en el mensaje del foro "no me queda totalmente claro si se les sacará algún partido o si realmente serán una ayuda".

- Con respecto al listado de los flightAssignment completados (anteriores a la fecha establecida por el sistema), se ha implementado un conjunto de datos donde los flightAssignment pueden estar tanto publicados como sin publicar en dicho listado, esto conlleva a la decisón de diseño en la que los flightAssignment que están en la lista de completados se puedan publicar (ya que se asignaron cuando dicho flitghAssignment estaba planeado, y si no tenemos dicha posibilidad tendríamos flightAssignment en la lista de completados sin opción a ser publicados), siempre que cumpla con el resto de condiciones que nos pide el cliente.
En la lista de flightAssignment planeadas no se podrá asociar a una leg que ya haya ocurrido (por lo tanto ni llevar a cabo el método publish, el cual era requisito del cliente), esto se hace mostrando en el desplegable solamente aquellas leg que son futuras. 
Esta decisión se ha tomado debido a los requisitos ciertamente incompletos que expone el cliente, además de la sugerencia establecida en el mensaje:  "céntrese ahora mismo en que su implementación no cumpla las condiciones de suspenso" (haciendo referencia al mensaje "Sufficient conditions to earn grade F" ).

