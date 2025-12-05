package mx.edu.uteq.idgs12.microservicio_profesor.exception;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String email) {
        super("Ya existe un profesor con el correo: " + email);
    }
}
