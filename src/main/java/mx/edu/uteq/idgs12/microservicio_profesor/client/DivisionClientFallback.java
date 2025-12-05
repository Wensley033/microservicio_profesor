package mx.edu.uteq.idgs12.microservicio_profesor.client;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import mx.edu.uteq.idgs12.microservicio_profesor.dto.DivisionDto;
import mx.edu.uteq.idgs12.microservicio_profesor.exception.DivisionServiceException;

@Slf4j
@Component
public class DivisionClientFallback implements DivisionClient {

    @Override
    public DivisionDto obtenerDivisionPorId(Long id) {
        log.warn("Fallback activado para obtenerDivisionPorId - ID: {}", id);
        throw new DivisionServiceException("Servicio de divisiones no disponible temporalmente");
    }

    @Override
    public List<DivisionDto> obtenerTodasLasDivisiones() {
        log.warn("Fallback activado para obtenerTodasLasDivisiones");
        return Collections.emptyList();
    }

    @Override
    public List<DivisionDto> obtenerDivisionesActivas() {
        log.warn("Fallback activado para obtenerDivisionesActivas");
        return Collections.emptyList();
    }
}
