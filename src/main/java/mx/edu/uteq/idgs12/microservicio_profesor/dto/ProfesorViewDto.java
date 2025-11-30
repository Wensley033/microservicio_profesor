package mx.edu.uteq.idgs12.microservicio_profesor.dto;

import lombok.Data;

@Data
public class ProfesorViewDto {
    private Long id;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private String division;

}
