package mx.edu.uteq.idgs12.microservicio_profesor.exception;

public class ProfesorNotFoundException extends RuntimeException {
    public ProfesorNotFoundException(String message) {
        super(message);
    }

    public ProfesorNotFoundException(Long id) {
        super("Profesor no encontrado con id: " + id);
    }
}
