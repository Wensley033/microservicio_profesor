package mx.edu.uteq.idgs12.microservicio_profesor.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mx.edu.uteq.idgs12.microservicio_profesor.client.DivisionClient;
import mx.edu.uteq.idgs12.microservicio_profesor.dto.DivisionDto;
import mx.edu.uteq.idgs12.microservicio_profesor.dto.ProfesorDto;
import mx.edu.uteq.idgs12.microservicio_profesor.dto.ProfesorViewDto;
import mx.edu.uteq.idgs12.microservicio_profesor.entity.ProfesorEntity;
import mx.edu.uteq.idgs12.microservicio_profesor.exception.DivisionNotFoundException;
import mx.edu.uteq.idgs12.microservicio_profesor.exception.DivisionServiceException;
import mx.edu.uteq.idgs12.microservicio_profesor.exception.DuplicateEmailException;
import mx.edu.uteq.idgs12.microservicio_profesor.exception.ProfesorNotFoundException;
import mx.edu.uteq.idgs12.microservicio_profesor.repository.ProfesorRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfesorService {

    private final ProfesorRepository profesorRepository;
    private final DivisionClient divisionClient;

    @Transactional(readOnly = true)
    public List<ProfesorDto> obtenerTodos() {
        return profesorRepository.findAll()
                .stream()
                .map(this::convertirADto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ProfesorDto obtenerPorId(Long id) {
        ProfesorEntity profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new ProfesorNotFoundException(id));
        return convertirADto(profesor);
    }

    @Transactional(readOnly = true)
    public ProfesorViewDto obtenerProfesorConDivision(Long profesorId) {
        ProfesorEntity profesor = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new ProfesorNotFoundException(profesorId));

        DivisionDto division = null;

        if (profesor.getDivisionId() != null) {
            try {
                division = obtenerDivisionConResiliencia(profesor.getDivisionId());
            } catch (DivisionServiceException e) {
                log.warn("Servicio de división no disponible para profesor {}: {}",
                        profesorId, e.getMessage());
            } catch (Exception e) {
                log.error("Error inesperado al obtener división para profesor {}: {}",
                        profesorId, e.getMessage());
            }
        }

        return convertirAViewDto(profesor, division);
    }

    @Transactional
    public ProfesorDto crear(ProfesorDto profesorDto) {
        if (profesorRepository.existsByCorreo(profesorDto.getCorreo())) {
            throw new DuplicateEmailException(profesorDto.getCorreo());
        }

        if (profesorDto.getDivisionId() != null) {
            validarDivisionExiste(profesorDto.getDivisionId());
        }

        ProfesorEntity profesor = convertirAEntidad(profesorDto);
        ProfesorEntity guardado = profesorRepository.save(profesor);
        log.info("Profesor creado exitosamente: {}", guardado.getId());
        return convertirADto(guardado);
    }

    @Transactional
    public ProfesorDto actualizar(Long id, ProfesorDto profesorDto) {
        ProfesorEntity profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new ProfesorNotFoundException(id));

        if (!profesor.getCorreo().equals(profesorDto.getCorreo()) &&
                profesorRepository.existsByCorreo(profesorDto.getCorreo())) {
            throw new DuplicateEmailException(profesorDto.getCorreo());
        }

        if (profesorDto.getDivisionId() != null &&
                !profesorDto.getDivisionId().equals(profesor.getDivisionId())) {
            validarDivisionExiste(profesorDto.getDivisionId());
        }

        profesor.setNombre(profesorDto.getNombre());
        profesor.setApellido(profesorDto.getApellido());
        profesor.setCorreo(profesorDto.getCorreo());
        profesor.setTelefono(profesorDto.getTelefono());
        profesor.setDivisionId(profesorDto.getDivisionId());

        ProfesorEntity actualizado = profesorRepository.save(profesor);
        log.info("Profesor actualizado exitosamente: {}", actualizado.getId());
        return convertirADto(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!profesorRepository.existsById(id)) {
            throw new ProfesorNotFoundException(id);
        }
        profesorRepository.deleteById(id);
        log.info("Profesor eliminado exitosamente: {}", id);
    }

    @CircuitBreaker(name = "divisionService", fallbackMethod = "obtenerDivisionFallback")
    @Retry(name = "divisionService")
    private DivisionDto obtenerDivisionConResiliencia(Long divisionId) {
        log.debug("Intentando obtener división con id: {}", divisionId);
        return divisionClient.obtenerDivisionPorId(divisionId);
    }

    private DivisionDto obtenerDivisionFallback(Long divisionId, Exception e) {
        log.warn("Fallback activado para división {}: {}", divisionId, e.getMessage());
        return null;
    }

    private void validarDivisionExiste(Long divisionId) {
        try {
            DivisionDto division = obtenerDivisionConResiliencia(divisionId);
            if (division == null) {
                throw new DivisionNotFoundException(divisionId);
            }
        } catch (DivisionServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new DivisionNotFoundException("No se pudo verificar la división con id: " + divisionId);
        }
    }

    // Métodos de conversión
    private ProfesorDto convertirADto(ProfesorEntity entity) {
        ProfesorDto dto = new ProfesorDto();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setCorreo(entity.getCorreo());
        dto.setTelefono(entity.getTelefono());
        dto.setDivisionId(entity.getDivisionId());
        return dto;
    }

    private ProfesorEntity convertirAEntidad(ProfesorDto dto) {
        ProfesorEntity entity = new ProfesorEntity();
        entity.setNombre(dto.getNombre());
        entity.setApellido(dto.getApellido());
        entity.setCorreo(dto.getCorreo());
        entity.setTelefono(dto.getTelefono());
        entity.setDivisionId(dto.getDivisionId());
        return entity;
    }

    private ProfesorViewDto convertirAViewDto(ProfesorEntity profesor, DivisionDto division) {
        ProfesorViewDto viewDto = new ProfesorViewDto();
        viewDto.setId(profesor.getId());
        viewDto.setNombre(profesor.getNombre());
        viewDto.setApellido(profesor.getApellido());
        viewDto.setCorreo(profesor.getCorreo());
        viewDto.setTelefono(profesor.getTelefono());
        viewDto.setDivision(division != null ? division.getNombre() : "Sin división");
        return viewDto;
    }
}