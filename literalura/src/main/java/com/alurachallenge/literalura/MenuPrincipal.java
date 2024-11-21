package com.alurachallenge.literalura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class MenuPrincipal {

    private LibroRepository libroRepository;

    private final Scanner scanner = new Scanner(System.in);

    @Autowired
    private LibroService libroService;

    public void mostraMenu(){
        int opcion = -1;
        do {
            System.out.println("""
                    \nSeleccione una opción:
                    1- Buscar Libro por título
                    2- Listar libros registrados
                    3- Listar autores registrados
                    4- Listar autores vivos en un determinado año
                    5- Listar libros por idioma
                    0- Salir
                    """);

            try {
                opcion = scanner.nextInt();
                scanner.nextLine();

                switch (opcion){
                    case 1:
                        buscarLibroPorTitulo();
                        break;
                    case 2:
                        listarLibrosRegistrados();
                        break;
                    case 3:
                        listarAutoresRegistrados();
                        break;
                    case 4:
                        listarAutoresVivosEnAno();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, elija una opción.");
                }
            } catch (InputMismatchException e){
                System.out.println("Entrada no válida. Por favor ingrese una opción.");
                scanner.nextLine();
            }

        } while (opcion != 0);

        scanner.close();
        System.exit(0);
    }

    private void buscarLibroPorTitulo(){
        System.out.println("Introduzca el titulo del libro:");
        String titulo = scanner.nextLine();
        try {
            Libro libro = libroService.buscarLibroPorTitulo(titulo);
            System.out.println("------------------LIBRO ENCONTRADO------------------");
            System.out.println("Titulo: " + libro.getTitulo());
            System.out.println("Autor: " + libro.getAutor());
            System.out.println("Idioma: " + libro.getIdioma());
            System.out.println("Numero de descargas: " + libro.getNumeroDeDescargas());
        } catch (Exception e){
            System.out.println("Error: "  + e.getMessage());
        }
    }

    private void listarLibrosRegistrados(){
        try {
            List<Libro> libros = libroService.obtenerTodosLosLibros();
            if (libros.isEmpty()){
                System.out.println("No hay libros registrados en la base de datos.");
            } else {
                System.out.println("\nLibros registrados: ");
                for (Libro libro : libros){
                    System.out.println("-------------------------------------------------");
                    System.out.println("Titulo: " + libro.getTitulo());
                    System.out.println("Autor: " + libro.getAutor());
                    System.out.println("Idioma: " + libro.getIdioma());
                    System.out.println("Numero de descargas: " + libro.getNumeroDeDescargas());
                }
            }
        } catch (Exception e){
            System.out.println("No se pudo mostrar los libros registrados: " + e.getMessage());
        }

    }

    private void listarAutoresRegistrados(){
        try {
            List<Libro> libros = libroService.obtenerTodosLosLibros();

            if (libros.isEmpty()){
                System.out.println("No hay autores registrados.");
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

            for (Map.Entry<String, List<Libro>> entry : autoresMap.entrySet()){
                String autor = entry.getKey();
                List<Libro> librosDelAutor = entry.getValue();

                Integer fechaNacimiento = librosDelAutor.get(0).getFechaNacimientoAutor();
                Integer fechaFallecimiento = librosDelAutor.get(0).getFechaFallecimientoAutor();

                System.out.println("-------------------------");
                System.out.println("Autor: " + autor);
                System.out.println("Fecha de nacimiento: " + (fechaNacimiento != null ? fechaNacimiento : "Desconocida"));
                System.out.println("Fecha de fallecimiento: " + (fechaFallecimiento != null ? fechaFallecimiento : "Desconocida"));
                System.out.println("Libros: ");

                for (Libro libro : librosDelAutor){
                    System.out.println("- " + libro.getTitulo());
                }
            }

        } catch (Exception e){
            System.out.println("No se pudo mostrar los autores: " + e.getMessage());
        }

    }

    private void listarAutoresVivosEnAno(){

        System.out.println("Ingrese el año para buscar autores vivos en ese año:");

        int year = scanner.nextInt();
        scanner.nextLine();

        libroService.buscarAutoresVivos(year);

        /*try {
            int year = scanner.nextInt();
            scanner.nextLine();
            libroService.buscarAutoresVivos(year);
        } catch (InputMismatchException e){
            System.out.println("Entrada no válida. Por favor vuelva a ingresa una opción.");
            scanner.nextLine();
        } catch (Exception e){
            System.out.println("No se pudo mostrar los autores vivos: " + e.getMessage());
        }*/
    }

    private void listarLibrosPorIdioma(){

        System.out.println("""
                Seleccione un idioma para listar los libros registrados: 
                1- Español (es)
                2- Inglés (en)
                3- Francés (fr)
                4- Alemán (de)
                5- Italiano (it)
                6- Portugués (pt)""");

        try {
            int opcionIdioma = scanner.nextInt();
            scanner.nextLine();

            String idiomaSeleccionado = null;
            switch (opcionIdioma){
                case 1 -> idiomaSeleccionado = "es";
                case 2 -> idiomaSeleccionado = "en";
                case 3 -> idiomaSeleccionado = "fr";
                case 4 -> idiomaSeleccionado = "de";
                case 5 -> idiomaSeleccionado = "it";
                case 6 -> idiomaSeleccionado = "pt";
                default -> System.out.println("Opción no válida.");
            }

            if (idiomaSeleccionado != null){
                List<Libro> libros = libroService.obtenerLibrosPorIdiomaAutor(idiomaSeleccionado);
                if (libros.isEmpty()){
                    System.out.println("No hay libros registrados en ese idioma.");
                } else {
                    System.out.println("\nLibros registrados:");
                    for (Libro libro : libros){
                        System.out.println("------------------------------------------");
                        System.out.println("Titulo: " + libro.getTitulo());
                        System.out.println("Autor: " + libro.getAutor());
                        System.out.println("Idioma: " + libro.getIdioma());
                        System.out.println("Numero de descargas: " + libro.getNumeroDeDescargas());
                    }
                }
            }
        } catch (InputMismatchException e){
            System.out.println("Entrada no válida. Por favor ingrese una opción.");
            scanner.nextLine();
        }
    }
}
