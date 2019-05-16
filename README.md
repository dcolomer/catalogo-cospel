# Aplicación Web Java
## Consulta de catálogo virtual páginado en backend y generación de PDF's
> ### Catálogo virtual de productos profesionales de peluquería y cosmética para mayoristas

### Tecnologías
- Frontend: HTML5, CSS3, JavaScript, JQuery 
- Backend: Java/Servlets/JSP (Tomcat 8.5)
- Base de datos: MySQL 8
- Construcción proyecto: Maven 3.5

### Bases de datos
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

### Imágenes del catálogo

En el directorio src/main/images-prod se proporciona un archivo comprimido que contiene las imágenes de los productos del catálogo.

Tendremos que descomprimir este archivo en una determinada ubicación y entonces informar de esta ruta en el fichero 'catalogo.properties', el cual se encuentra en 'src/main/resources'. Por ejemplo:

catalogo.properties
~~~
rutaImg = e:/imagenes-cosmetica/productos/product/
rutaImgDetail = e:/imagenes-cosmetica/productos/detailed/
~~~

### Logs

En la raiz de la carpeta web se genera el fichero **app.log** para almacenar los diferentes niveles de log.

### Construcción del proyecto y deploy en Tomcat

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