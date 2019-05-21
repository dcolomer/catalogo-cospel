# Aplicación Web Java
## Consulta de catálogo virtual páginado en backend y generación de PDF's
> ### Catálogo virtual de productos profesionales de peluquería y cosmética para mayoristas

## Tecnologías
- Frontend: HTML5, CSS3, JavaScript, JQuery 
- Backend: Java/Servlets/JSP (Tomcat 8.5)
- Base de datos: MySQL 8
- Construcción proyecto: Maven 3.5

## Capturas de pantalla
A continuación se muestra visualmente las principales funcionalidades de la aplicación.

### Inicio de la aplicación
Se accede a la aplicación mediante la URL http://localhost:8080/catalogo-cosmetica/  

![Inicio](https://github.com/dcolomer/catalogo-cospel/blob/master/src/main/screenshots/01.png)

### Login
Para acceder a la aplicación se debe introducir el usuario 'catalogo' y la contraseña 'catalogo'. Esta información no está almacenada en en la base de datos, sencillamente se encuentra 'hardcoded' en el propio código fuente (mala práctica...esto no se debe hacer jamás en una aplicación en producción)

![Login](https://github.com/dcolomer/catalogo-cospel/blob/master/src/main/screenshots/02.png)

### Catelorías principales
Una vez validados nos encontramos en la pantalla raíz de categorías. Vamos a seleccionar para esta ocasión la categoría 'peluquería', para lo cual podemos hacer doble click sobre el ítem 'peluquería' o bien solamente un click y a continuación pulsar el botón 'Acceder a la categoría':

![Categoria principal](https://github.com/dcolomer/catalogo-cospel/blob/master/src/main/screenshots/03.png)

### Subcatelorías
Ahora podemos seleccionar un fabricante de productos de peluquería. Seleccionamos, por ejemplo, L'Oreal, para lo cual podemos hacer doble click sobre el ítem 'L'Oreal' o bien solamente un click y a continuación pulsar el botón 'Acceder':

![Fabricante de peluquería](https://github.com/dcolomer/catalogo-cospel/blob/master/src/main/screenshots/04.png)

### Líneas de productos
A continuación se nos muestran las diferentes líneas de productos del fabricante. Podemos seleccionar 'Expert', para lo cual podemos hacer doble click sobre el ítem 'Expert' o bien solamente un click y a continuación pulsar el botón 'Acceder':

![Líneas de productos](https://github.com/dcolomer/catalogo-cospel/blob/master/src/main/screenshots/05.png)

### Sublíneas de productos
Habitualmente una línea de productos está formada por diversas sublíneas. En nuestro caso vamos a seleccionar 'Vitamino color', para lo cual podemos hacer doble click sobre el ítem 'Vitamino color' o bien solamente un click y a continuación pulsar el botón 'Acceder':

![Subíneas de productos](https://github.com/dcolomer/catalogo-cospel/blob/master/src/main/screenshots/06.png)

### Productos
En este punto se nos muestra en forma tabular los diferentes productos de la línea de productos que hemos seleccionado. Vemos la descripción, precio, descuento y una imagen. Los productos aparecen paginados de tres en tres, por lo que podremos avanzar de página en página mediante los botones de la parte inferior-derecha de la pantalla. Es posible hacer un zoom de la imagen situando el cursor del ratón encima:

![Productos](https://github.com/dcolomer/catalogo-cospel/blob/master/src/main/screenshots/07.png)

### Generación de PDF
Podemos generar un PDF con la información de productos que se encuentre en pantalla. Para ello, sencillamente tenemos que pulsar el botón 'Generar PDF':

![PDF Dialog](https://github.com/dcolomer/catalogo-cospel/blob/master/src/main/screenshots/08a.png)  
![PDF abierto](https://github.com/dcolomer/catalogo-cospel/blob/master/src/main/screenshots/08b.png)

### Búsqueda de productos

Mediante el botón 'Buscar productos' se nos mostrará una ventana en la que podremos escribir texto relacionado con la descripción de los productos. Esta opción es útil cuando queremos obtener una relación de productos que tenga una descripción similar pero pertenezcan a diferentes fabricantes. Por ejemplo, un caso sería querer obtener cepillos, sea quien sea el fabricante:

![Buscar Dialog](https://github.com/dcolomer/catalogo-cospel/blob/master/src/main/screenshots/09.png)

## Bases de datos
La base de datos se llama 'catalogo-cospel' y en el directorio src/main/db se proporciona un archivo comprimido de tipo sql, el cual contiene la estructura y los datos necesarios para el correcto funcionamiento de la aplicación.

Las tablas de la base de datos son las siguientes:

- categories
- category_descriptions
- images
- images_links
- products
- products_categories
- product_descriptions
- product_prices
- promotions
- promotion_descriptions

En la carpeta src/main/webapp/META-INF, en el fichero 'context.xml', se define el DataSource para la conexión entre Tomcat y la base de datos. De esta manera Tomcat crea un pool de conexiones.

Las credenciales para la conexión Tomcat-MySQL son:
 
- usuario: catalogo
- password: catalogo

Por tanto, en MySQL hay que definir un usuario con estas credenciales y con permimos CRUD para la base de datos 'catalogo_cospel'.

## Imágenes de los productos del catálogo

En el directorio src/main/images-prod se proporciona un archivo comprimido que contiene las imágenes de los productos del catálogo.

Tendremos que descomprimir este archivo en una determinada ubicación y entonces informar de esta ruta en el fichero 'catalogo.properties', el cual se encuentra en 'src/main/resources'. Por ejemplo:

catalogo.properties
~~~
rutaImg = e:/imagenes-cosmetica/productos/product/
rutaImgDetail = e:/imagenes-cosmetica/productos/detailed/
~~~

## Logs

En la raiz de la carpeta web se genera el fichero **app.log** para almacenar los diferentes niveles de log.

## Construcción del proyecto y deploy en Tomcat

Configuración previa:
 
- El plugin de Maven es para la versión 7 de Tomcat pero también funciona para la versión 8.

- Necesitamos dar de alta un usuario en el archivo 'tomcat-users.xml' de Tomcat, en la carpeta 'conf' de la instalación del mismo, que tenga permisos del tipo 'manager-script'. Este usuario será el que se utilizará en el archivo pom.xml para hacer el deploy del proyecto.

Ejemplo:

Fichero **tomcat-users.xml:**

~~~
<tomcat-users>
	<role rolename="manager-script"/>
	<user username="maven" password="deployer" roles="manager-script"/>
</tomcat-users>
~~~

Fichero **pom.xml:**
~~~
<plugin>
    <groupId>org.apache.tomcat.maven</groupId>
    <artifactId>tomcat7-maven-plugin</artifactId>
    <version>2.2</version>
    <configuration>
        <url>http://localhost:8080/manager/text</url>
        <username>maven</username>
        <password>deployer</password>
        <update>true</update>
    </configuration>
</plugin>
~~~
Pasos para el despliegue y redespliegue del proyecto:

1. Nos aseguramos de tener Tomcat en marcha.
2. En un terminal (PowerShell, etc), nos aseguramos de estar situados en la raíz del proyecto (donde se encuentra el fichero pom.xml) y ejecutamos: **mvn tomcat7:deploy**
3. Finalizado sin errores el proceso anterior tendremos disponible la aplicación web en http://localhost:8080/catalogo-cosmetica/
