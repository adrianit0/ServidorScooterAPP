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
    ('Scooter dañada', 'La Scooter se encuentra dañada o en mal estado')
    ('Scooter no arranca', 'Problemas con la Scooter referidos al arranque'),
    ('Otros problemas', 'Otros problemas acontecidos con la Scooter');

-- Cosas que queda aún por hacer

INSERT INTO PuestoTrabajo (nombre) VALUES
    ('Directivo'),
    ('Administrador'),
    ('Técnico recogida'),
    ('Técnico mantenimiento'),
    ('Técnico mecánico');

INSERT INTO TipoTarea (nombre) VALUES
    ('Recoger Scooter de la calle'),
    ('Devolver Scooter a la calle'),
    ('Cambiar baterias y limpiar'),
    ('Comprobar si la Scooter funciona'),
    ('Arreglar Scooter'),
    ('Estimar gastos de reparación');

INSERT INTO EstadoTarea (nombre) VALUES
    ('Asignada'),
    ('Estimada'),
    ('Aplazada'),
    ('Transferida'),
    ('Finalizada sin incidencias'),
    ('Finalizada con incidencias'),
    ('Cancelada');