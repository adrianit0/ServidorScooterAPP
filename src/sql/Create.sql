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
    nombre VARCHAR(32) NOT NULL,
    apellido1 VARCHAR(32) NOT NULL,
    apellido2 VARCHAR(32),
    nick VARCHAR(32) UNIQUE NOT NULL,
    email VARCHAR(32) UNIQUE NOT NULL,
    pass VARCHAR(32) NOT NULL,
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
    noSerie VARCHAR(10) NOT NULL UNIQUE,
    matricula VARCHAR(7) NOT NULL UNIQUE,
    codigo INT(6) NOT NULL UNIQUE,
    fechaCompra DATE NOT NULL,
    precioCompra DOUBLE(8,2) NOT NULL,
    fechaBaja DATE,
    -- Posición almacenada en la tabla sobre la scooter
    posicionLat DECIMAL(10,6),
    posicionLon DECIMAL(10,6),
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
    costeTotal DOUBLE(6,2) NOT NULL DEFAULT 0,
    -- Foreign keys
    cliente_id INT NOT NULL,
    scooter_id INT NOT NULL,
    estado_id INT NOT NULL,
    CONSTRAINT clalfk FOREIGN KEY (cliente_id) REFERENCES Cliente(id),
    CONSTRAINT clscfk FOREIGN KEY (scooter_id) REFERENCES Scooter(id),
    CONSTRAINT clesfk FOREIGN KEY (estado_id) REFERENCES EstadoAlquiler (id)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS EstadoScooter (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fecha TIMESTAMP NOT NULL,
    estadoBateria DOUBLE(5,2) NOT NULL,
    velocidad INT(3) NOT NULL,
    -- Posición almacenada en la tabla sobre la scooter
    posicionLat decimal(10,6),
    posicionLon decimal(10,6),
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

CREATE TABLE IF NOT EXISTS Mantenimiento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fecha_inicio TIMESTAMP NOT NULL,
    fecha_fin TIMESTAMP NOT NULL, -- Por defecto 8h de la hora de inicio.
    scooter_id INT NOT NULL,
    CONSTRAINT scmafk FOREIGN KEY (scooter_id) REFERENCES Scooter(id)
    
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Incidencia (
    id INT PRIMARY KEY AUTO_INCREMENT,
    descripcion VARCHAR(300),
    fechaCreacion TIMESTAMP NOT NULL,
    -- Posición almacenada en la tabla sobre la scooter
    posicionLat decimal(10,6),
    posicionLon decimal(10,6),
    -- Foreign keys
    tipoIncidencia_id INT NOT NULL,
    cliente_id INT NOT NULL,
    scooter_id INT NOT NULL,
    alquiler_id INT,
    mantenimiento_id INT,
    CONSTRAINT tiinfk FOREIGN KEY (tipoIncidencia_id) REFERENCES TipoIncidencia(id),
    CONSTRAINT clinfk FOREIGN KEY (cliente_id) REFERENCES Cliente(id),
    CONSTRAINT scinfk FOREIGN KEY (scooter_id) REFERENCES Scooter(id),
    CONSTRAINT alinfk FOREIGN KEY (alquiler_id) REFERENCES Alquiler(id),
    CONSTRAINT mainfk FOREIGN KEY (mantenimiento_id) REFERENCES Mantenimiento(id)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Imagen (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    url VARCHAR(30) NOT NULl,
    incidencia_id INT NOT NULL,
    CONSTRAINT inimfk FOREIGN KEY (incidencia_id) REFERENCES Incidencia(id)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Puesto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    descripcion VARCHAR(2000)
) ENGINE=INNODB;

CREATE TABLE IF NOT EXISTS Empleado (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    apellido1 VARCHAR(30) NOT NULL,
    apellido2 VARCHAR(30),
    dni VARCHAR(9) NOT NULL UNIQUE,
    direccion VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    pass VARCHAR(100) NOT NULL,
    sueldo DOUBLE(8,2) NOT NULL,
    puesto_id INT NOT NULL,
    ciudad_id INT NOT NULL,
    sede_id INT NOT NULL,
    fecha_alta DATE NOT NULL,
    fecha_baja DATE,
    CONSTRAINT puemfk FOREIGN KEY (puesto_id) REFERENCES Puesto(id),
    CONSTRAINT ciemfk FOREIGN KEY (ciudad_id) REFERENCES Ciudad(id),
    CONSTRAINT seemfk FOREIGN KEY (sede_id) REFERENCES Sede(id)
) ENGINE=INNODB;

CREATE TABLE EstadoTarea (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(30) NOT NULL,
    descripcion VARCHAR(2000) NOT NULL
) ENGINE=INNODB;

CREATE TABLE TipoTarea (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(50) NOT NULL
) ENGINE=INNODB;

CREATE TABLE Tarea (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(60) NOT NULL,
    observaciones VARCHAR(300) NOT NULL,
    fecha_asignacion TIMESTAMP NOT NULL,
    estimacion INT NOT NULL, -- EL tiempo en minutos
    empleado_id INT NOT NULL,
    tipoTarea_id INT NOT NULL,
    estadoTarea_id INT NOT NULL,
    mantenimiento_id INT NOT NULL,
    CONSTRAINT emtafk FOREIGN KEY (empleado_id) REFERENCES Empleado(id),
    CONSTRAINT titafk FOREIGN KEY (tipoTarea_id) REFERENCES TipoTarea(id),
    CONSTRAINT estafk FOREIGN KEY (estadoTarea_id) REFERENCES EstadoTarea(id),
    CONSTRAINT matafk FOREIGN KEY (mantenimiento_id) REFERENCES Mantenimiento(id)
) ENGINE=INNODB;

CREATE TABLE Horario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    diaSemana INT NOT NULL, -- Siendo 0 lunes y 6 domingo
    horaInicio INT(2) NOT NULL,    -- Min 00, Max 23
    horaFin INT(2) NOT NULL        -- Min 00, Max 23
) ENGINE=INNODB;

CREATE TABLE HorarioEmpleado (
    empleado_id INT NOT NULL,
    horario_id INT NOT NULL,
    PRIMARY KEY(empleado_id, horario_id),
    CONSTRAINT emhofk FOREIGN KEY (empleado_id) REFERENCES Empleado(id),
    CONSTRAINT hohofk FOREIGN KEY (horario_id) REFERENCES Horario(id)
) ENGINE=INNODB;