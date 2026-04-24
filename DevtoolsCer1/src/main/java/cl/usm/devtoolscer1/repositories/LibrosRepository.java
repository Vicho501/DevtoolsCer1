package cl.usm.devtoolscer1.repositories;

import cl.usm.devtoolscer1.entities.Libros;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface LibrosRepository extends MongoRepository<Libros, String> {

    List<Libros> findByAutor(String autor);

    List<Libros> findByTituloContainingIgnoreCaseOrAutorContainingIgnoreCase(String titulo, String autor);

    Optional<Libros>findByIsbn(String isbn);

}
