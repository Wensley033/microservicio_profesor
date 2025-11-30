package mx.edu.uteq.idgs12.microservicio_profesor.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import mx.edu.uteq.idgs12.microservicio_profesor.dto.ProfesorDto;
import mx.edu.uteq.idgs12.microservicio_profesor.dto.ProfesorViewDto;
import mx.edu.uteq.idgs12.microservicio_profesor.service.ProfesorService;

@RestController
@RequestMapping("/profesores")
@RequiredArgsConstructor
public class ProfesorController {

    private final ProfesorService profesorService;

    @GetMapping
    public ResponseEntity<List<ProfesorDto>> obtenerTodos() {
        try {
            List<ProfesorDto> profesores = profesorService.obtenerTodos();
            return ResponseEntity.ok(profesores);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesorDto> obtenerPorId(@PathVariable Long id) {
        try {
            ProfesorDto profesor = profesorService.obtenerPorId(id);
            return ResponseEntity.ok(profesor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Ahora solo requiere el ID del profesor
    @GetMapping("/{profesorId}/division")
    public ResponseEntity<ProfesorViewDto> obtenerProfesorConDivision(
            @PathVariable Long profesorId) {
        try {
            ProfesorViewDto profesor = profesorService.obtenerProfesorConDivision(profesorId);
            return ResponseEntity.ok(profesor);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<ProfesorDto> crear(@RequestBody ProfesorDto profesorDto) {
        try {
            ProfesorDto nuevoProfesor = profesorService.crear(profesorDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProfesor);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesorDto> actualizar(
            @PathVariable Long id,
            @RequestBody ProfesorDto profesorDto) {
        try {
            ProfesorDto profesorActualizado = profesorService.actualizar(id, profesorDto);
            return ResponseEntity.ok(profesorActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        try {
            profesorService.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}