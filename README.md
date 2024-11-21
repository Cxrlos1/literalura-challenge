# Literalura - Proyecto de Gestión de Libros en lenguaje Java
### Segundo desafío Alura para desarrolladores Backend

### Descripción
<P>Literalura es una aplicación desarrollada en Java con Spring Boot, diseñada para gestionar libros mediante la consulta a la API de Gutendex y almacenamiento en una base de datos PostgreSQL. La aplicación proporciona un conjunto de funcionalidades para buscar, almacenar y listar libros y autores.</P>

### Tecnologías utilizadas
- Java 17
- Spring Boot
- Maven
- PostgreSQL
- Jackson
- Gutendex API
- IntelliJ IDEA

### Estructura del Proyecto
El proyecto se compone de las siguientes entidades:
- `MenuPrincipal`: Es la clase principal que gestiona la interacción con el usuario a través de un menú en la consola. Permite al usuario seleccionar opciones para realizar operaciones específicas como buscar libros, listar autores, idiomas.
- `Libro`: Representa la entidad del libro y contiene información relacionada con cada libro registrado en la base de datos. Los atributos incluyen el título, el autor, el idioma, la fecha de nacimiento y fallecimiento del autor, y el número de descargas. Esta clase actúa como un modelo de datos para las operaciones relacionadas con los libros.
- `LibroService`: Es un servicio que contiene la lógica para la gestión de los libros. Incluye métodos para buscar libros a través de la API externa `Gutendex`, registrar libros en la base de datos, listar libros según idioma, y buscar autores que estaban vivos en un determinado año. Utiliza la clase `Libro` como modelo y se comunica con `LibroRepository` para realizar operaciones con la base de datos.
- `LibroRepository`: Es una interfaz que extiende `JpaRepository` y proporciona métodos para realizar operaciones en la base de datos para la entidad `Libro`. A través de esta interfaz, se pueden realizar consultas personalizadas como la búsqueda de libros por título y autor, o la filtración de libros por idioma.

### Funcionalidades y Uso de la Aplicación
La aplicación es interactiva y funciona desde la consola. Al iniciar, se muestra un menú con las opciones disponibles. El usuario puede seleccionar la funcionalidad deseada ingresando el número correspondiente. Al iniciar la aplicación se mostrará el siguiente menú:

![menu](https://github.com/user-attachments/assets/01f099a2-b175-4454-9496-07dbf437a7d6)


1. Buscar un libro por su título
   <P>El usuario puede buscar un libro ingresando el título. La aplicación consulta la API de Gutendex y muestra información       del primer libro encontrado. Si el libro no está registrado en la base de datos, se guarda automáticamente. Si el libro se     encuentra, se mostrará toda la información almacenada sobre él.</P>
   Ejemplo de información mostrada:

![op1](https://github.com/user-attachments/assets/f6599e3d-b083-4703-b1ce-dbb30914a2e7)

2. Listar todos los libros registrados
   <P>Se muestra una lista de todos los libros que están almacenados en la base de datos, con la siguiente información:</P> 

![op2](https://github.com/user-attachments/assets/89325c30-0636-4b60-a64a-c7c07b47bdd7)

3. Listar autores registrados
   <P>Se muestra una lista con todos los autores registrados en la base de datos.</P>

![op3](https://github.com/user-attachments/assets/a20aa364-fd5e-43ac-ad46-853e3550c900)
 
4. Listar autores vivos en un determinado año
   <P>Permite al usuario especificar un año y mostrar todos los autores que estaban vivos en ese año. Utiliza consultas derivadas con Spring Data JPA para obtener la información de la base de datos.</P>

![op4](https://github.com/user-attachments/assets/b4258172-3742-418b-aff6-e912b667604b)

5. Listar libros por idioma
   <P>El usuario puede seleccionar un idioma para listar todos los libros registrados en la base de datos que correspondan a ese idioma.</P> 

![op5](https://github.com/user-attachments/assets/6f9d4763-4291-427a-8d00-7e3b579aebe5)

 
