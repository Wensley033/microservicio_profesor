package mx.edu.uteq.idgs12.microservicio_profesor.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import mx.edu.uteq.idgs12.microservicio_profesor.client.DivisionClient;
import mx.edu.uteq.idgs12.microservicio_profesor.dto.DivisionDto;
import mx.edu.uteq.idgs12.microservicio_profesor.dto.ProfesorDto;
import mx.edu.uteq.idgs12.microservicio_profesor.dto.ProfesorViewDto;
import mx.edu.uteq.idgs12.microservicio_profesor.entity.ProfesorEntity;
import mx.edu.uteq.idgs12.microservicio_profesor.repository.ProfesorRepository;

@Service
@RequiredArgsConstructor
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
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con id: " + id));
        return convertirADto(profesor);
    }

    @Transactional(readOnly = true)
    public ProfesorViewDto obtenerProfesorConDivision(Long profesorId) {
        ProfesorEntity profesor = profesorRepository.findById(profesorId)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con id: " + profesorId));
        
        DivisionDto division = null;
        
        // Obtener información de la división solo si el profesor tiene una asignada
        if (profesor.getDivisionId() != null) {
            try {
                division = divisionClient.obtenerDivisionPorId(profesor.getDivisionId());
            } catch (Exception e) {
                // Si falla la llamada al microservicio, continuamos sin la división
                System.err.println("Error al obtener división: " + e.getMessage());
            }
        }
        
        return convertirAViewDto(profesor, division);
    }

    @Transactional
    public ProfesorDto crear(ProfesorDto profesorDto) {
        // Validar que el correo no exista
        if (profesorRepository.existsByCorreo(profesorDto.getCorreo())) {
            throw new RuntimeException("Ya existe un profesor con el correo: " + profesorDto.getCorreo());
        }
        
        // Validar que la división existe si se proporciona
        if (profesorDto.getDivisionId() != null) {
            try {
                divisionClient.obtenerDivisionPorId(profesorDto.getDivisionId());
            } catch (Exception e) {
                throw new RuntimeException("División no encontrada con id: " + profesorDto.getDivisionId());
            }
        }
        
        ProfesorEntity profesor = convertirAEntidad(profesorDto);
        ProfesorEntity guardado = profesorRepository.save(profesor);
        return convertirADto(guardado);
    }

    @Transactional
    public ProfesorDto actualizar(Long id, ProfesorDto profesorDto) {
        ProfesorEntity profesor = profesorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profesor no encontrado con id: " + id));
        
        // Validar correo si cambió
        if (!profesor.getCorreo().equals(profesorDto.getCorreo()) && 
            profesorRepository.existsByCorreo(profesorDto.getCorreo())) {
            throw new RuntimeException("Ya existe un profesor con el correo: " + profesorDto.getCorreo());
        }
        
        // Validar que la división existe si se proporciona o cambió
        if (profesorDto.getDivisionId() != null && 
            !profesorDto.getDivisionId().equals(profesor.getDivisionId())) {
            try {
                divisionClient.obtenerDivisionPorId(profesorDto.getDivisionId());
            } catch (Exception e) {
                throw new RuntimeException("División no encontrada con id: " + profesorDto.getDivisionId());
            }
        }
        
        profesor.setNombre(profesorDto.getNombre());
        profesor.setApellido(profesorDto.getApellido());
        profesor.setCorreo(profesorDto.getCorreo());
        profesor.setTelefono(profesorDto.getTelefono());
        profesor.setDivisionId(profesorDto.getDivisionId());
        
        ProfesorEntity actualizado = profesorRepository.save(profesor);
        return convertirADto(actualizado);
    }

    @Transactional
    public void eliminar(Long id) {
        if (!profesorRepository.existsById(id)) {
            throw new RuntimeException("Profesor no encontrado con id: " + id);
        }
        profesorRepository.deleteById(id);
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