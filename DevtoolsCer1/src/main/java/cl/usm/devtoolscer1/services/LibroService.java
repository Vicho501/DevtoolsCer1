package cl.usm.devtoolscer1.services;

import cl.usm.devtoolscer1.entities.Libros;
import cl.usm.devtoolscer1.repositories.LibrosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {
    @Autowired
    private LibrosRepository librosRepository;

    public List<Libros> obtenerTodosLosLibros(){
        return librosRepository.findAll();

    }

    public List<Libros>buscarLibrosPorCoincidencia(String searchTerm){
        if (searchTerm ==null || searchTerm.trim().isEmpty()){
            return obtenerTodosLosLibros();
        }
        return librosRepository.findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCase(searchTerm,searchTerm);

    }
    public Libros creaLibro(Libros libros){
        Optional<Libros> librosExistente= librosRepository.findById(libros.getId());

        if (librosExistente.isPresent()){
            throw  new RuntimeException("Ya existe un libro con el ISBN" + libros.getIsbn());

        }
        return librosRepository.save(libros);
    }

    public List<Libros>obtenerLibrosPorAutor(String autor){
        return librosRepository.findByAutor(autor);
    }

}
