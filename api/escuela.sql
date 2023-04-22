create database guia10_dsm_escuela;
use guia10_dsm_escuela;

CREATE TABLE alumnos (
	id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
	nombre VARCHAR(50) NOT NULL,
	apellido VARCHAR(50) NOT NULL,
	edad INT NOT NULL
)Engine=InnoDB;

insert into alumnos values
(null, 'Roberto Carlos', 'López Abarca', 22),
(null, 'Bryan Ernesto', 'Marroquín Anaya', 22),
(null, 'Marcos Benjamín', 'Rodríguez', 22);

create table profesores (
	id int auto_increment primary key,
    nombre varchar(60) not null,
    apellido varchar(60) not null,
    edad varchar(60) not null
)Engine=InnoDB;

insert into profesores values
(null, 'Luis Alonso', 'Arenivar', 55),
(null, 'Yessenia', 'Escobar', 42),
(null, 'Guillermo', 'Calderón', 30);