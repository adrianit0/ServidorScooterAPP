/* 
    Inserts necesarios para que funcione correctamente la aplicación.
 */
/**
 * Author:  agarcia.gonzalez
 * Created: 25-abr-2019
 */

INSERT INTO EstadoAlquiler (nombre) VALUES ('Reservado'), ('Reserva cancelada'), ('En curso'), ('Finalizado'), ('Cancelado');

INSERT INTO TipoIncidencia (nombre, descripcion) VALUES
    ('Scooter mal aparcada', 'La Scooter no se encuentra correctamente aparcada'),
    ('Scooter dañada', 'La Scooter se encuentra dañada o en mal estado'),
    ('Scooter no arranca', 'Problemas con la Scooter referidos al arranque'),
    ('Otros problemas', 'Otros problemas acontecidos con la Scooter');

INSERT INTO Puesto (nombre) VALUES
    ('Directivo'),
    ('Administrativo'),
    ('Técnico recogida'),
    ('Técnico mantenimiento'),
    ('Técnico mecánico');

INSERT INTO TipoTarea (nombre) VALUES
    ('Llevar Scooter al taller'),
    ('Devolver Scooter a la calle'),
    ('Cambiar baterias y limpiar'),
    ('Aparcar bien la moto'),
    ('Comprobar si la Scooter funciona'),
    ('Reparar Scooter'),
    ('Estimar gastos de reparación');

INSERT INTO EstadoTarea (nombre, descripcion) VALUES
    ('Asignada', 'La tarea ha sido asignada a un técnico que debe completar'),
    ('Aplazada', 'La tarea no ha sido completado durante una jornada laboral y el resto se realizará en la siguiente jornada laboral'),
    ('Transferida', ''),
    ('Finalizado', 'Ha sido finalizado sin problemas'),
    ('No finalizado', 'No ha completado la tarea'),
    ('Cancelada', 'La tarea ha sido cancelada por parte de la administración a un empleado. No suma al computo total diario');

-- PRIMEROS VALORES PARA POBLAR LA BASE DE DATOS
INSERT INTO ciudad (nombre, provincia) VALUES
    ('Cádiz', 'Cádiz'), ('Jerez de la Frontera', 'Cádiz'), ('San Fernando', 'Cádiz'), ('Sevilla', 'Sevilla');

INSERT INTO sede (nombre, direccion, ciudad_id) VALUES
    ('ScooterAPP principal', 'Calle Sacramento', (SELECT id FROM ciudad WHERE nombre='Cádiz' LIMIT 1)),
    ('ScooterAPP filial', 'Calle Investigación', (SELECT id FROM ciudad WHERE nombre LIKE '%Jerez%' LIMIT 1));

INSERT INTO empleado (nombre, apellido1, apellido2, dni, direccion, email, pass, sueldo, puesto_id, ciudad_id, sede_id) VALUES
    ('Administrador', 'Admin', null, '12345678A', 'Foo Street', 'admin', '1234', 0, 1, 1, 1);

INSERT INTO modelo (marca, modelo) VALUES ('Honda', 'PCX 108');