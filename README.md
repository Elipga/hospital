# PROYECTO FINAL “CURSO DE PROGRAMACIÓN PROFESIONAL DESDE 0”
Se desea implementar un sistema de citas en un hospital. Para ello tendremos a los pacientes, donde
guardaremos su nombre, dni y dirección. También tendremos al personal sanitario, que pueden ser
médicos o enfermeros. De ambos pretendemos guardar también el nombre, dni y número de
colegiado. De los médicos guardaremos además sus años de experiencia.
El sistema tiene que abrir automáticamente una ventana de tiempo para poder coger las citas
médicas por parte de los pacientes. El sistema funcionará así:
- Dado al día actual, el día mínimo para pedir una cita será el lunes de la semana siguiente y el día
máximo será el viernes. La franja horaria para pedir las citas será configurable para cada médico.
Por ejemplo:
* Si hoy es miércoles 2 de noviembre, se podrán pedir citas desde el lunes 7 de noviembre al
viernes 11 de noviembre.
* Si hoy es martes 8 de noviembre, se podrán pedir citas del lunes 14 de noviembre al viernes 18 de
noviembre.
La franja horaria en que se pueden pedir las citas, como ya he dicho, es configurable. Por ejemplo,
el doctor López puede visitar cada día de 12:00 a 18:00 mientras la doctora González puede visitar
cada día de 09:00 a 13:00. Con los enfermeros pasa lo mismo.
Se pide:
- Diagrama UML del sistema, explicando cada decisión que se tome.
- Un backend programado usando Spring Boot que dé soporte a las siguientes operaciones:
-  Alta de paciente.
-  Alta de médic@
-  Alta de enfermer@
-  Configurar horario de un médic@ o enfermer@ (la hora inicio y fin que se indique es
aplicable a toda la semana en la que se abre la ventana temporal)
-  Consultar huecos libres de un médic@ o enfermer@. Sólo se podrán obtener los días y horas
de la semana siguiente a la fecha actual ya que, como he dicho, es la única ventana temporal
con la que opera el sistema.
-  Asignar cita. Tiene que comprobar que, para el paciente, personal sanitario, hora y día
indicados, hay hueco libre. No se podrán asignar citas para la semana en curso, sólo a partir
del siguiente lunes a la fecha actual, como ya se ha indicado anteriormente.
-  Consultar citas de un paciente para un día determinado, ordenado por horas
-  Consultar las citas que tiene un médic@ para toda la semana, ordenado por días y horas.
-  BONUS, PARA MENCIÓN ESPECIAL EN LA NOTA: Consultar los médicos más
ocupados (los que tienen más citas) en la próxima ventana temporal. (es decir, entre el lunes
de la semana siguiente a hoy, y el viernes)
- Al programar el backend, hay que indicar qué decisiones se han tomado o qué cambios se han
realizado respecto al UML original y por qué.
- La API se tiene que poder documentar usando “Swagger” para Spring boot. Es tarea vuestra
aprender a usar esta herramienta.
- El UML del sistema y las decisiones de backend tomadas se entregarán en un PDF.
- El código, como siempre, en Github
FECHA DE ENTREGA: lunes 28 de noviembre.
Se valorará:
- Claridad de código.
- Correcta separación en capas.
- Gestión de excepciones
- Decisiones de diseño e implementación tomadas.
