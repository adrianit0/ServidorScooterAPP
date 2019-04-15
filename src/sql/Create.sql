/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Nombre de la APP (Provisional): ScooterAPP 
 * Author:  Adrián García
 */

DROP DATABASE IF EXISTS ScooterAPP;
CREATE DATABASE IF NOT EXISTS ScooterAPP;
USE ScooterAPP;

CREATE TABLE IF NOT EXISTS Cliente (
    id INT PRIMARY KEY AUTO_INCREMENT,
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
    id INT PRIMARY KEY AUTO_INCREMENT,
    marca VARCHAR(30) NOT NULL,
    modelo VARCHAR(30) NOT NULL
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Scooter (
    id INT PRIMARY KEY AUTO_INCREMENT,
    matricula VARCHAR(7) NOT NULL UNIQUE,
    codigo INT(6) NOT NULL UNIQUE,
    fechaCompra DATE NOT NULL,
    precioCompra DOUBLE(8,2) NOT NULL,
    -- Posición almacenada en la tabla sobre la scooter
    posicionLat INT(15),
    posicionAlt INT(15),
    -- Claves foreaneas
    modelo_id INT NOT NULL,
    CONSTRAINT moscfk FOREIGN KEY (modelo_id) REFERENCES Modelo (id)
) ENGINE=INNODB;

-- Estado del alquiler, que podrá ser:
-- 1 -> Reservado.
-- 2 -> Reserva cancelada.
-- 3 -> En curso.
-- 4 -> Finalizado.
-- 5 -> Cancelada.
CREATE TABLE IF NOT EXISTS EstadoAlquiler (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(30)
) ENGINE=INNODB;

-- Informe de alquiler realizado
CREATE TABLE IF NOT EXISTS Alquiler (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fechaInicio TIMESTAMP,
    fechaFin TIMESTAMP,
    minutosConducidos INT,
    minutosParada INT,
    costeTotal DOUBLE(6,2) NOT NULL DEFAULT 0,
    -- Foreign keys
    cliente_id INT NOT NULL,
    scooter_id INT NOT NULL,
    CONSTRAINT clalfk FOREIGN KEY (cliente_id) REFERENCES Cliente(id),
    CONSTRAINT clscfk FOREIGN KEY (scooter_id) REFERENCES Alquiler(id)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS EstadoScooter (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fecha TIMESTAMP NOT NULL,
    estadoBateria DOUBLE(5,2) NOT NULL,
    velocidad INT(3) NOT NULL,
    -- Posición almacenada en la tabla sobre la scooter
    posicionLat INT(15),
    posicionAlt INT(15),
    -- Foreign keys
    scooter_id INT NOT NULL,
    alquiler_id INT NOT NULL,
    CONSTRAINT scesfk FOREIGN KEY (scooter_id) REFERENCES Scooter(id),
    CONSTRAINT alesfk FOREIGN KEY (alquiler_id) REFERENCES Alquiler(id)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Ciudad (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    provincia VARCHAR(30) NOT NULL
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Sede (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    direccion VARCHAR(60) NOT NULL,
    ciudad_id INT NOT NULL,
    CONSTRAINT cisefk FOREIGN KEY (ciudad_id) REFERENCES Ciudad(id)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Asignado (
    sede_id INT NOT NULL,
    scooter_id INT NOT NULL,
    PRIMARY KEY(sede_id, scooter_id),
    CONSTRAINT seasfk FOREIGN KEY (sede_id) REFERENCES Sede(id),
    CONSTRAINT scasfk FOREIGN KEY (scooter_id) REFERENCES Scooter(id)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Bono (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    descripcion VARCHAR(300) NOT NULL,
    minutos INT NOT NULL,
    precio DOUBLE(5,2) NOT NULL
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS HistorialCompra (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fecha TIMESTAMP NOT NULL,
    precio DOUBLE (5,2) NOT NULL,
    minutos INT NOT NULL,
    bono_id INT NOT NULL,
    cliente_id INT NOT NULL,
    CONSTRAINT bohifk FOREIGN KEY (bono_id) REFERENCES Bono(id),
    CONSTRAINT clhifk FOREIGN KEY (cliente_id) REFERENCES Cliente(id)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Descuento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    desde TIMESTAMP NOT NULL,
    hasta TIMESTAMP NOT NULL,
    descuento DOUBLE(5,2) NOT NULL
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Aplicado (
    bono_id INT NOT NULL,
    descuento_id INT NOT NULL,
    PRIMARY KEY(bono_id, descuento_id),
    CONSTRAINT boapfk FOREIGN KEY (bono_id) REFERENCES Bono(id),
    CONSTRAINT deapfk FOREIGN KEY (descuento_id) REFERENCES Descuento(id)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS TipoIncidencia (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    descripcion VARCHAR(300) NOT NULL
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Incidencia (
    id INT PRIMARY KEY AUTO_INCREMENT,
    descripcion VARCHAR(300),
    -- Posición almacenada en la tabla sobre la scooter
    posicionLat INT(15),
    posicionAlt INT(15),
    -- Foreign keys
    tipoIncidencia_id INT NOT NULL,
    cliente_id INT NOT NULL,
    alquiler_id INT,
    CONSTRAINT tiinfk FOREIGN KEY (tipoIncidencia_id) REFERENCES TipoIncidencia(id),
    CONSTRAINT clinfk FOREIGN KEY (cliente_id) REFERENCES Cliente(id),
    CONSTRAINT alinfk FOREIGN KEY (alquiler_id) REFERENCES Alquiler(id)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Imagen (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    url VARCHAR(30) NOT NULl,
    incidencia_id INT NOT NULL,
    CONSTRAINT inimfk FOREIGN KEY (incidencia_id) REFERENCES Incidencia(id)
) ENGINE=INNODB;