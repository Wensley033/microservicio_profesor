-- Datos iniciales para la tabla profesores
-- Nota: Asegúrate de que los divisionId correspondan a divisiones existentes en microservicio-division

INSERT INTO profesores (nombre, apellido, correo, telefono, division_id, activo) VALUES
('Juan', 'Pérez García', 'juan.perez@uteq.edu.mx', '4421234567', 1, true),
('María', 'González López', 'maria.gonzalez@uteq.edu.mx', '4421234568', 1, true),
('Carlos', 'Rodríguez Martínez', 'carlos.rodriguez@uteq.edu.mx', '4421234569', 2, true),
('Ana', 'Hernández Sánchez', 'ana.hernandez@uteq.edu.mx', '4421234570', 2, true),
('Luis', 'Martínez Ramírez', 'luis.martinez@uteq.edu.mx', '4421234571', 3, true),
('Laura', 'López Flores', 'laura.lopez@uteq.edu.mx', '4421234572', 3, true),
('José', 'García Torres', 'jose.garcia@uteq.edu.mx', '4421234573', 4, true),
('Patricia', 'Ramírez Morales', 'patricia.ramirez@uteq.edu.mx', '4421234574', 4, true),
('Roberto', 'Torres Jiménez', 'roberto.torres@uteq.edu.mx', '4421234575', 5, true),
('Carmen', 'Jiménez Ruiz', 'carmen.jimenez@uteq.edu.mx', '4421234576', 5, true);
