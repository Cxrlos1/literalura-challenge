package com.alurachallenge.literalura;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;


    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Libro buscarLibroPorTitulo(String titulo) throws IOException, InterruptedException{

        String tituloCodificado = URLEncoder.encode(titulo, StandardCharsets.UTF_8);

        String url = "https://gutendex.com/books/?search=" + tituloCodificado;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200){
            String responseBody = response.body();

            JsonNode rootNode = objectMapper.readTree(responseBody);
            JsonNode resultsNode = rootNode.path("results");

            if (resultsNode.isArray() && resultsNode.size() > 0){
                JsonNode bookNode = resultsNode.get(0);

                String idLibro = bookNode.path("id").asText();
                String tituloLibro = bookNode.path("title").asText();
                String autorLibro = bookNode.path("authors").get(0).path("name").asText();
                String idiomaLibro = bookNode.path("languages").get(0).asText();
                String descripcionLibro = bookNode.path("description").asText();
                int numeroDeDescargas = bookNode.path("download_count").asInt(0);

                String fechaNacimiento = bookNode.path("authors").get(0).path("birth_year").asText(null);
                String fechaFallecimiento = bookNode.path("authors").get(0).path("death_year").asText(null);


                Optional<Libro> libroExistente = libroRepository.findByTituloAndAutor(tituloLibro, autorLibro);
                if (libroExistente.isPresent()){
                    System.out.println("\n***Este libro ya esta registrado en la base de datos***");
                    return libroExistente.get();
                }

                Libro libro = new Libro();
                libro.setId(Long.valueOf(idLibro));
                libro.setTitulo(tituloLibro);
                libro.setAutor(autorLibro);
                libro.setIdioma(idiomaLibro);
                libro.setDescripcion(descripcionLibro);
                libro.setNumeroDeDescargas(numeroDeDescargas);

                if (fechaNacimiento != null && !fechaNacimiento.isEmpty()){
                    libro.setFechaNacimientoAutor(Integer.parseInt(fechaNacimiento));
                }
                if (fechaFallecimiento != null && !fechaFallecimiento.isEmpty()){
                    libro.setFechaFallecimientoAutor(Integer.parseInt(fechaFallecimiento));
                }



                return libroRepository.save(libro);
            } else {
                throw new IOException("No se pudo encontrar el libro " + titulo);
            }

        } else {
            throw new IOException("Error al buscar el libro: " + response.statusCode());
        }

    }

    public List<Libro> obtenerTodosLosLibros(){
        return libroRepository.findAll();
    }

    public void buscarAutoresVivos(int year){
        //List<Libro> libros = libroRepository.findAll();
        List<Libro> libros = libroRepository.findByFechaNacimientoAutorLessThanEqualAndFechaFallecimientoAutorGreaterThanEqual(year, year);

        if (libros.isEmpty()){
            System.out.println("\nNo se encontraron autores vivos en el año " + year);
            return;
        }

        Map<String, List<Libro>> autoresMap = new HashMap<>();
        for (Libro libro : libros){
            String autor = libro.getAutor();
            if (!autoresMap.containsKey(autor)){
                autoresMap.put(autor, new ArrayList<>());
            }
            autoresMap.get(autor).add(libro);
        }

        //boolean autoresEncontrados = false;

        for (Map.Entry<String, List<Libro>> entry : autoresMap.entrySet()){
            String autor = entry.getKey();
            List<Libro> librosDelAutor = entry.getValue();

            Integer fechaNacimiento = librosDelAutor.get(0).getFechaNacimientoAutor();
            Integer fechaFallecimiento = librosDelAutor.get(0).getFechaFallecimientoAutor();

            //int anoNacimiento = (fechaNacimiento != null) ? Integer.parseInt(String.valueOf(fechaNacimiento)) : Integer.MIN_VALUE;
            //int anoFacllecimiento = (fechaFallecimiento != null) ? Integer.parseInt(String.valueOf(fechaFallecimiento)) : Integer.MAX_VALUE;

            //if (year >= anoNacimiento && year <= anoFacllecimiento){
                //autoresEncontrados = true;
                System.out.println("----------------------------------------");
                System.out.println("Autor: " + autor);
                System.out.println("Fecha de nacimiento: " + (fechaNacimiento != null ? fechaNacimiento : "Desconocida"));
                System.out.println("Fecha de fallecimiento: " + (fechaFallecimiento != null ? fechaFallecimiento : "Desconocido"));

                for (Libro libro : librosDelAutor){
                    System.out.println("- " + libro.getTitulo());
                }
            //}
        }
        /*if (!autoresEncontrados){
            System.out.println("No se encontraron autores vivos en el año " + year);
        }*/
    }


    public List<Libro> obtenerLibrosPorIdiomaAutor(String idioma){
        return libroRepository.findByIdioma(idioma);
    }


}
