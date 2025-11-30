package mx.edu.uteq.idgs12.microservicio_profesor.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.edu.uteq.idgs12.microservicio_profesor.entity.ProfesorEntity;

@Repository
public interface ProfesorRepository extends JpaRepository<ProfesorEntity, Long> {
    
    List<ProfesorEntity> findByNombreContainingIgnoreCase(String nombre);
    List<ProfesorEntity> findByApellidoContainingIgnoreCase(String apellido);
    boolean existsByCorreo(String correo);
}