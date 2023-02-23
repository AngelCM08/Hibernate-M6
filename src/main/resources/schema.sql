-- PostgreSQL Script

-- Schema tboia

DROP SCHEMA IF EXISTS tboia CASCADE;

-- Schema tboia

CREATE SCHEMA IF NOT EXISTS tboia;

-- Table tboia.Monstruo

DROP TABLE IF EXISTS tboia.Monstruo;

CREATE TABLE IF NOT EXISTS tboia.Monstruo (
    idMonstruo SERIAL NOT NULL,
    icono VARCHAR(500),
    nombre VARCHAR(45),
    vida INTEGER,
    descripcion VARCHAR(500),
    PRIMARY KEY (idMonstruo)
);

-- Table tboia.Personaje

DROP TABLE IF EXISTS tboia.Personaje;

CREATE TABLE IF NOT EXISTS tboia.Personaje (
    idPersonaje SERIAL NOT NULL,
    icono VARCHAR(500),
    nombre VARCHAR(45),
    vida INTEGER,
    daño VARCHAR(45),
    cadencia VARCHAR(45),
    vel_proyectil VARCHAR(45),
    rango VARCHAR(45),
    velocidad VARCHAR(45),
    suerte INTEGER,
    PRIMARY KEY (idPersonaje)
);

-- Table tboia.Objeto

DROP TABLE IF EXISTS tboia.Objeto;

CREATE TABLE IF NOT EXISTS tboia.Objeto (
    idObjeto SERIAL NOT NULL,
    icono VARCHAR(500),
    nombre VARCHAR(45),
    descripcion VARCHAR(500),
    PRIMARY KEY (idObjeto)
);

-- Table tboia.Equipar

DROP TABLE IF EXISTS tboia.Equipar;

CREATE TABLE IF NOT EXISTS tboia.Equipar (
    idPersonaje INTEGER NOT NULL,
    idObjeto INTEGER NOT NULL,
    PRIMARY KEY (idPersonaje, idObjeto),
    FOREIGN KEY (idPersonaje) REFERENCES tboia.Personaje (idPersonaje) ON DELETE NO ACTION ON UPDATE NO ACTION,
    FOREIGN KEY (idObjeto) REFERENCES tboia.Objeto (idObjeto) ON DELETE NO ACTION ON UPDATE NO ACTION
);
