package mx.edu.uteq.idgs12.microservicio_profesor.exception;

public class DivisionServiceException extends RuntimeException {
    public DivisionServiceException(String message) {
        super(message);
    }

    public DivisionServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
