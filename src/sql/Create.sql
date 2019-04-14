/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Nombre de la APP (Provisional): ScooterAPP 
 * Author:  Adrián García
 */

DROP TABLE IF EXISTS ScooterAPP;
CREATE DATABASE IF NOT EXISTS ScooterAPP;
USE ScooterAPP;

CREATE TABLE IF NOT EXISTS Cliente (
    id INT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL,
    apellido1 VARCHAR(30) NOT NULL,
    apellido2 VARCHAR(30),
    nick VARCHAR(30) UNIQUE NOT NULL,
    email VARCHAR(30) UNIQUE NOT NULL,
    pass VARCHAR(30) NOT NULL,
    minutos INT NOT NULL DEFAULT 0,
    activada TINYINT NOT NULL DEFAULT 1,
    fechaCreacion TIMESTAMP NOT NULL
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Modelo (
    id INT PRIMARY KEY,
    marca VARCHAR(30) NOT NULL,
    modelo VARCHAR(30) NOT NULL
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Scooter (
    id INT PRIMARY KEY,
    matricula VARCHAR(7) NOT NULL UNIQUE,
    codigo INT(6) NOT NULL UNIQUE,
    fechaCompra DATE NOT NULL,
    precioCompra DOUBLE(8,2) NOT NULL,
    -- Posición almacenada en la tabla sobre la scooter
    posicionLat INT(15) YES NULL,
    posicionAlt INT(15) YES NULL,
    -- Claves foreaneas
    modelo_id INT NOT NULL,
    CONSTRAINT moscfk FOREIGN KEY (modelo_id) REFERENCES Modelo (id)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Alquiler () ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS EstadoScooter () ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Ciudad () ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Sede () ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Asignado () ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Bono () ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS HistorialCompra () ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Descuento () ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Aplicado () ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS TipoIncidencia () ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Incidencia () ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Imagen () ENGINE=INNODB;