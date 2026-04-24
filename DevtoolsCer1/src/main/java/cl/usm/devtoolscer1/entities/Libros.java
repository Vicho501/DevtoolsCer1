package cl.usm.devtoolscer1.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.*;

@Data
@Document(collection = "Libros")


public class Libros {
    @Id
    private String id;

    @NotBlank(message = "El titulo es obligatorio")
    private String titulo;

    @NotBlank(message ="El autor es obligatorio")
    private String autor;

    @NotBlank(message = "El ISBN es obligatorio")
    @Size(min = 13, max = 13, message = "El ISBN debe tener exactamente 13 caracteres")
    @Pattern(regexp = "\\d{13}", message = "El ISBN debe contener solo 13 dígitos numéricos")
    private String isbn;

    @NotNull(message = "El número de páginas es obligatorio")
    @Min(value = 11, message = "El número de páginas debe ser mayor que 10")
    private Integer paginas;

    @NotBlank(message = "La categoría es obligatoria")
    private String categoria;

}
