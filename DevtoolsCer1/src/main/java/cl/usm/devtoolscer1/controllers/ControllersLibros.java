package cl.usm.devtoolscer1.controllers;

import cl.usm.devtoolscer1.entities.Libros;
import cl.usm.devtoolscer1.services.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.sql.ClientInfoStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class ControllersLibros {
    @Autowired
    private LibroService libroService;

    @GetMapping("/libros")
    public ResponseEntity<?> obtenerLibros(@RequestParam(required = false)String search){
        try{
            List<Libros> libros;
            if (search !=null && !search.trim().isEmpty()){
                libros=libroService.buscarLibrosPorCoincidencia(search);
            }
            else {
                libros = libroService.obtenerTodosLosLibros();
            }
            if (libros.isEmpty()){
                Map<String, String> response =new HashMap<>();
                response.put("mensaje","No se encontraron libros");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.ok(libros);
        }
        catch (Exception e){
            Map<String, String> error =new HashMap<>();
            error.put("error", "Error al obtener los libros:"+ e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    //Endpoint2 crear un libro
    @PostMapping("/crearLibro")
    public ResponseEntity<?> crearLibro(@Validated @RequestBody Libros libros){
        try{
            if (libros.getIsbn()==null || libros.getIsbn().length() !=13){
                Map<String, String> error = new HashMap<>();
                error.put("error", "El ISBN debe tener exactamente 13 caracteres");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            if (libros.getPaginas() <= 10) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "El número de páginas debe ser mayor que 10");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
            }
            Libros nuevoLibro = libroService.creaLibro(libros);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoLibro);

        }
        catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
        }
        catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al crear el libro: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }
    //endpoint 3 obtener libros por autor
    @GetMapping("/libros/{autor}")
    public  ResponseEntity<?> obtenerLibrosPorAutor(@PathVariable String autor){
        try {
            List<Libros> libros =libroService.obtenerLibrosPorAutor(autor);
            if (libros.isEmpty()) {
                Map<String, String> response = new HashMap<>();
                response.put("mensaje", "No se encontraron libros del autor: " + autor);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
            return ResponseEntity.ok(libros);
        }
        catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al buscar libros por autor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationExceptions(
            org.springframework.web.bind.MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((org.springframework.validation.FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
