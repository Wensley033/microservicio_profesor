package mx.edu.uteq.idgs12.microservicio_profesor.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import mx.edu.uteq.idgs12.microservicio_profesor.dto.DivisionDto;

// Se cambió la ruta base de "/api/divisiones" a "/divisiones"
// para coincidir con tu DivisionController
@FeignClient(
    name = "microservicio-division",
    path = "/divisiones",
    fallback = DivisionClientFallback.class
)
public interface DivisionClient {

    @GetMapping("/{id}")
    DivisionDto obtenerDivisionPorId(@PathVariable("id") Long id);
    
    @GetMapping
    List<DivisionDto> obtenerTodasLasDivisiones();
    
    @GetMapping("/activas")
    List<DivisionDto> obtenerDivisionesActivas();
}