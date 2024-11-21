package com.alurachallenge.literalura;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LibroRepository extends JpaRepository<Libro, Long> {

    Libro findByTitulo(String titulo);
    Optional<Libro> findByTituloAndAutor(String titulo, String autor);

    List<Libro> findByIdioma(String idioma);

    List<Libro> findByFechaNacimientoAutorLessThanEqualAndFechaFallecimientoAutorGreaterThanEqual(Integer year, Integer yearEnd);


}
