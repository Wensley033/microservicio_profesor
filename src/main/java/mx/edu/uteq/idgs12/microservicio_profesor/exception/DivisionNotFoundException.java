package mx.edu.uteq.idgs12.microservicio_profesor.exception;

public class DivisionNotFoundException extends RuntimeException {
    public DivisionNotFoundException(String message) {
        super(message);
    }

    public DivisionNotFoundException(Long id) {
        super("División no encontrada con id: " + id);
    }
}
