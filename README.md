En esta sección se describe en detalle el desarrollo de la aplicación web Elkar-Ekin. Además, se explica qué tecnologías se han empleado y su justificación. La sección se divide en dos apartados: el desarrollo Backend y Frontend.

11.1.	Desarrollo Backend

En este apartado se explicarán las principales funcionalidades que ofrece la aplicación a los distintos perfiles de usuario. Como se ha explicado en el apartado de “Análisis y diseño del producto”, esta plataforma está dirigida a tres tipos de usuario principales: administrador, cliente y voluntario. Estos perfiles tienen unas funcionalidades comunes y otras específicas. A continuación, se muestra un resumen de la estructura que sigue la aplicación en cuanto a las direcciones:

Funcionalidades generales de la aplicación:
1.	Login: 
Una vez abierta la aplicación web, un usuario no logueado tiene la opción de iniciar sesión. Para gestionar toda la seguridad de la web se ha definido una clase llamada SecurityConfig en el cual se establece, entre otros parámetros, el handler de login. 
A pesar de que el framework de Spring tenga implementado un login, para la aplicación se ha creado la clase CustomUserHandler para gestionar los tres perfiles y sus permisos. Una vez que el usuario completa los campos, es ese handler el que se encarga de reenviarlo al controlador adecuado. 
2
2.	Visualización de perfil
Una vez que los usuarios se hayan identificado, una de las opciones que se ofrece es la de visualizar el perfil. Asimismo, los usuarios podrán actualizar sus datos personales. 



Para llevar a cabo esta funcionalidad, el primer paso es obtener el usuario desde la sesión. Una vez hecho esto, se guarda el usuario en el modelo y se reenvía a la página de vista para la correcta visualización.

3.	Chat
Otra de las funcionalidades generales que ofrece la aplicación Elkar-Ekin es la de mandar mensajes mediante un chat. Siendo uno de los principales objetivos el crear una red de voluntariado, se considera que esta herramienta puede servir para facilitar la comunicación entre los usuarios.

La funcionalidad del Chat está basada en WebSockets, las cuales hacen posible la comunicación bidireccional entre clientes y servidores. En la conexión con WebSockets primero se establece una conexión mediante el handshake. Una vez abierta la conexión, tanto el servidor como cliente pueden mandar datos hasta que la conexión se vuelva a cerrar. A continuación, se muestra la estructura que sigue el chat en la web:
 
Ilustración 11.2 Estructura del chat de la aplicación
Como se puede ver en la imagen, el usuario se suscribe a una cola que tiene su propia ID. Así, cuando otro usuario quiera mandar un mensaje a una ID concreta se puede identificar la cola a la que tiene que redirigir el mensaje. 

En la ilustración, el entorno que gestiona todas las colas es el WebSocket. Para poder enviar datos como mensajes (objetos de tipo ChatMessage) es necesario hacer una conversión. Esto sucede, porque el WebSocket serializa todo el contenido antes de enviarlo y en este proceso las referencias de tipo Lazy (las que se han utilizado para relacionar objetos entre sí) puede causar problemas. Para solucionar ese problema, se han empleado Data Transfer Objects (DTO) en los cuales solamente se guardan los identificadores de las relaciones. Así, se ha podido crear un chat one-to-one para todos los usuarios que tengan acceso a la aplicación Elkar-Ekin.

Funcionalidades específicas de la aplicación:
1.	Administradores
Los administradores de la aplicación se encargan de gestionar los usuarios y las tareas que se publican en la web. Así, en el caso de que algún usuario haga un mal uso de la aplicación, el administrador puede eliminar dicha cuenta. Por otro lugar, los administradores pueden crear noticias sobre los últimos eventos en la aplicación Elkar-Ekin. Estas noticias estarán visibles para todos los usuarios tanto no logueados como logueados; siendo estos últimos los únicos que podrán comentar en las noticias. 

Funcionalidad de CRUD de noticias:
Para desarrollar la funcionalidad de crear, visualizar, editar y borrar noticias se ha definido una clase llamada NewsItem. Además, el controlador NewsItemController será el controlador que gestione todas las peticiones relacionadas con las noticias.
 
Para que el administrador pueda crear y editar noticias, se ha implementado el editor de texto de CKEditor. Este editor basado en la web permite a los administradores, agregar estilo al texto, adjuntar archivos como imágenes y vídeos, etc. 
 
Ilustración 11.3 Pantalla para crear noticias en la aplicación
Todas las noticias creadas se visualizan ordenadas de forma decreciente según la fecha de creación de estas. En el caso de que un administrador quiera eliminar una noticia, tienen la opción hacerlo mediante los botones de acciones que se situan junto a cada NewsItem.

En cuanto a la visualización de las noticias, todos los usuarios tienen acceso a todas las noticias desde la página principal de Elkar-Ekin. En esta última pantalla podrán realizar búsquedas con palabras clave utilizando la barra de búsqueda.


2.	Clientes
En cuanto a las funcionalidades principales de los clientes, se encuentran el crear, editar, visualizar y eliminar las tareas. Para crear estas funcionalidades, al igual que con los NewsItems, se ha definido un modelo (Task) y una DTO (TaskDto). Este último, permite transportar datos entre diferentes partes de una aplicación, de manera eficiente y segura. En el caso de las tareas, se ha considerado imprescindible utilizar una DTO para poder asegurarnos de que todos los datos introducidos por el cliente sean correctos, antes de trasferirlos a la base de datos.

El cliente tiene la opción de rellenar una serie de campos sobre la tarea. Entre esos campos se encuentra la localidad (código postal, población, calle y proovincia) que siempre aparece completado con los datos personales del cliente. Además, el cliente puede agregar una descripción y detalles de fecha y hora. Finalmente, en el caso de que el cliente quisiera compartir la tarea con un voluntario específico tiene la opción de indicarlo en el último campo. 

4.	Voluntarios
En cuanto a los voluntarios, la aplicación Elkar-Ekin permite a estos inscribirse en tareas y así realizarlas. Para ello, los voluntarios pueden visualizar todas las tareas e incluso marcar las tareas como guardadas. Esta funcionalidad se ha llevado a cabo asociando las tareas y los usuarios con una anotación de JPA @ManyToMany. Mediante esta anotación el programa genera una nueva tabla en la base de datos con los identificadores de tarea y usuario.

Una vez que un voluntario se haya inscrito a una tarea, se visualizará en otra página de tareas inscritas y desaparecerá del listado general de las tareas. Esto permite una búsqueda más ágil en el apartado de tareas.  



11.2.	Desarrollo Frontend
En este apartado se explicarán los puntos más importantes del desarrollo Frontend de la aplicación Elkar-Ekin.

En cuanto al diseño de la página web, se ha utilizado una paleta de colores clara y suave mediante la implementación de variables globales, para facilitar su utilización y posibles cambios a futuro. Además, se ha utilizado la librería Bootstrap para facilitar el proceso de desarrollo. Para conseguir un estilo más personalizado, se ha modificado el código CSS proporcionado mediante la suma, modificación y eliminación de algunas de las clases.

La aplicación se adapta a varios equipos y navegadores, entre otros, Google Chrome, Mozilla Firefox y Opera GX. Para hacer la validación en estos navegadores, tanto en formato móvil como para pantallas más grandes, se han realizado estas acciones y cerciorado de que se realizan correctamente:

	                                                                Google Chrome	 Mozilla Firefox	Opera GX
Acceso a diferentes páginas	                                            Sí	           Sí	           Sí
Inicio de sesión	                                                      Sí	           Sí	           Sí
Registro	                                                              Sí	           Sí            Sí
Carga de iconos                                                       	Sí	           Sí          	 Sí
Visualización de animaciones y transiciones	                            Sí	           Sí	           Sí
Registro y guardado en tareas 	                                        Sí         	   Sí	           Sí
Creación, modificación, visualización y eliminación de tareas	          Sí	           Sí	           Sí
Creación, modificación, visualización y eliminación de noticias	        Sí	           Sí	           Sí
Modificación, visualización y eliminación de usuarios	                  Sí	           Sí	           Sí

La web se ha desarrollado siguiendo el enfoque mobile first, es decir, la priorización del desarrollo web para pantallas pequeñas, para progresivamente adaptarlo a pantallas más grandes. La página web se ha desarrollado para tres diferentes tamaños: teléfonos móviles (con una anchura menor a 768px), tabletas (con una anchura de entre 768px y 992px) y ordenadores, con anchura superior a la última. Se ha decidido usar estas medidas específicas para mantener la coherencia, ya que son también las utilizadas por la librería Bootstrap.

Para el diseño de las distintas páginas de HTML, se han implementado una serie de plantillas o layouts para así tener una estructura fija en todas las páginas. A continuación, se puede ver la estructura general de éstos:

Como se puede observar, se ha diseñado un layout básico en el que se han incluido entre otros los archivos CSS generales, el icono y la barra de idiomas. Después, se han creado cuatro layouts que son variaciones del layout general. Así, se ha conseguido crear vistas para distintos tipos de usuario con una estructura que puede ofrecer flexibilidad de cara al futuro.

Se ha hecho uso de la biblioteca de Java Thymeleaf para, entre otros:
•	Cargar fragmentos de código: En las páginas generales, como el index, no se ha utilizado el mismo archivo de HTML para todos los usuarios. En cambio, se ha creado un archivo o fragmento con el contenido de la página y se ha incluido a las páginas específicas de index de cada perfil. Así, cada perfil tendrá la barra de navegación que le corresponde junto con el contenido correspondiente.

•	Mostrar datos obtenidos en el procesamiento de Backend en diferentes archivos HTML para la visualización del usuario: Para poder visualizar contenido e información personalizada para cada usuario en diferentes páginas, se ha hecho uso de etiquetas propias de Thymeleaf para acceder a propiedades del modelo asignado en el Backend, así como mostrar mensajes de error al introducir datos inválidos.

•	Desarrollar la internalización de la página web en español, euskera e inglés: Se ha utilizado la etiqueta de texto de Thymeleaf para acceder y visualizar texto automáticamente desde los archivos de messages.properties.

•	Almacenar los datos introducidos en formularios, en objetos de transferencia de datos (DTO): Se ha utilizado la etiqueta de objeto de Thymeleaf para almacenar en un patrón de diseño DTO la información introducida en diferentes campos de un formulario, para así tener acceso a esta información desde el lado del servidor según sea necesario.

•	Mostrar, cambiar u ocultar parte del código dependiendo del rol del usuario: Con el fin de mostrar a los diferentes usuarios información diferente dependiendo de su rol en la aplicación web, se ha hecho uso de diferentes condicionales dentro de los archivos HTML. Por ejemplo, dependiendo del rol del usuario, el texto visualizado en la página de su perfil será diferente:

 
