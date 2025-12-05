package mx.edu.uteq.idgs12.microservicio_profesor.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.uteq.idgs12.microservicio_profesor.dto.ProfesorDto;
import mx.edu.uteq.idgs12.microservicio_profesor.dto.ProfesorViewDto;
import mx.edu.uteq.idgs12.microservicio_profesor.service.ProfesorService;

@RestController
@RequestMapping("/profesores")
@RequiredArgsConstructor
@Validated
public class ProfesorController {

    private final ProfesorService profesorService;

    @GetMapping
    public ResponseEntity<List<ProfesorDto>> obtenerTodos() {
        List<ProfesorDto> profesores = profesorService.obtenerTodos();
        return ResponseEntity.ok(profesores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfesorDto> obtenerPorId(@PathVariable Long id) {
        ProfesorDto profesor = profesorService.obtenerPorId(id);
        return ResponseEntity.ok(profesor);
    }

    @GetMapping("/{profesorId}/division")
    public ResponseEntity<ProfesorViewDto> obtenerProfesorConDivision(@PathVariable Long profesorId) {
        ProfesorViewDto profesor = profesorService.obtenerProfesorConDivision(profesorId);
        return ResponseEntity.ok(profesor);
    }

    @PostMapping
    public ResponseEntity<ProfesorDto> crear(@Valid @RequestBody ProfesorDto profesorDto) {
        ProfesorDto nuevoProfesor = profesorService.crear(profesorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProfesor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfesorDto> actualizar(
            @PathVariable Long id,
            @Valid @RequestBody ProfesorDto profesorDto) {
        ProfesorDto profesorActualizado = profesorService.actualizar(id, profesorDto);
        return ResponseEntity.ok(profesorActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        profesorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}