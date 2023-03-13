
# Práctica Hibernate

Proyecto que permite la comunicación con una Base de Datos, mediante Hibernate, cuyas tablas y contenido han sido obtenidos a través de scrapear la web que contiene la wiki del videojuego [The Binding Of Isaac Afterbirth+](https://bindingofisaac.fandom.com/es/wiki/The_Binding_of_Isaac:_Afterbirth%2B) (TBOIA+), la estructura de la base de datos es similar a la del siguiente [esquema](https://github.com/AngelCM08/Hibernate-M6/blob/master/src/main/resources/schema.sql), el lenguaje utilizado en la Base de Datos es PostgreSQL.

## Ejecución
- Descarga el proyecto
- Ábrelo dentro de tu IDE de Java favorito
- Modifica el archivo db.properties y persistance.xml para que apunte a tu servidor de BBDD
- Ejecuta la clase Main

NOTA: Selecciona la opción "1. Poblar o restaurar tablas." la primera vez que lo ejecutes.

## Tecnología utilizada

- [Hibernate](https://hibernate.org/)
- [OpenCSV](https://www.baeldung.com/opencsv)
- [PostgreSQL](https://www.postgresql.org/)

## Metodología empleada
El programa se comunica con la BBDD asignada en el archivo db.properties, a partir del archivo schema.sql genera la BBDD nombrada 'tboia' con las tablas vacías, utiliza OpenCSV para leer los archivos CSV del package 'data' y rellenar o resetear las tablas. Con este programa se pueden seleccionar, insertar, actualizar o eliminar tablas y/o registros de la BBDD conectada, ofreciendo una serie de funciones para simplificar pequeñas tareas de tratamiento de datos.

## Author

- [@AngelCM08](https://github.com/AngelCM08)
