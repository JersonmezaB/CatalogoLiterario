package com.aluracursos.catalogoliterario.catalogoLiterario.principal;

import com.aluracursos.catalogoliterario.catalogoLiterario.model.Autor;
import com.aluracursos.catalogoliterario.catalogoLiterario.model.DatosAutor;
import com.aluracursos.catalogoliterario.catalogoLiterario.model.DatosLibro;
import com.aluracursos.catalogoliterario.catalogoLiterario.model.Libro;
import com.aluracursos.catalogoliterario.catalogoLiterario.repository.LibroRepository;
import com.aluracursos.catalogoliterario.catalogoLiterario.service.ConsumoAPI;
import com.aluracursos.catalogoliterario.catalogoLiterario.service.ConvierteDatos;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

public class Principal {

    private Scanner teclado = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    private LibroRepository repositorio;
    private List<Libro> libros;
    List<Autor> autors = new ArrayList<>();


    public Principal(LibroRepository repository) {
        this.repositorio = repository;
    }

    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    Por favor, seleccione una Opcion:
                    
                    1) - Buscar libro por título 
                    2) - Listar libros registrados
                    3) - Listar autores registrados
                    4) - Listar autores vivos en un determinado año
                    5) - Listar libros por idioma
                                  
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    mostrarLibrosGuardados();
                    break;
                case 3:
                    mostrarAutoresGuardados();
                    break;
                case 4:
                    mostrarAutoresVivosPorFecha();
                    break;
                case 5:
                    mostrarLibrosPorIdimoma();
                    break;
                case 0:
                    System.out.println("Cerrando aplicación...");
                    break;
                default:
                    System.out.println("No se encontraron resultados para su busqueda");
            }
        }

    }

    private String convertirStringAJson(String str,String key){

        JSONObject json_transform = null;
        try {

            json_transform = new JSONObject(str);
            var result = json_transform.getJSONArray(key).getJSONObject(0);
            return result.toString();

        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String consultarLibroPorTitulo() {
        System.out.println("Escriba el nombre del libro que desea buscar:");
        var nombreLibro = teclado.nextLine();
        var json = consumoApi.obtenerDatos(URL_BASE +"?search="+ nombreLibro.replace(" ", "%20"));

        return json;
    }
    private Libro obtenerDatosLibro(String json){

        var jsonResultLibro = convertirStringAJson(json,"results");
        DatosLibro datosLibro = conversor.obtenerDatos(jsonResultLibro, DatosLibro.class);
        Libro libro = new Libro(datosLibro);

        return libro;
    }
    private Autor obtenerDatosAutor(String json){
        var jsonResultLibro = convertirStringAJson(json,"results");
        var jsonResultAutor = convertirStringAJson(jsonResultLibro,"authors");

        DatosAutor datosAut = conversor.obtenerDatos(jsonResultAutor, DatosAutor.class);
        Autor autor = new Autor(datosAut);
        return autor;
    }

    private void buscarLibroPorTitulo() {

        List<Autor> autors = new LinkedList<>();

        String json = consultarLibroPorTitulo();
        Libro libro  = obtenerDatosLibro(json);
        Autor autor = obtenerDatosAutor(json);
        autors.add(autor);

        libro.setAutores(autors);
        repositorio.save(libro);

        System.out.println(libro.toString());
    }

    private void mostrarLibrosGuardados() {
        libros = repositorio.findAll();

        libros.stream()
                .sorted(Comparator.comparing(Libro::toString))
                .forEach(System.out::println);
    }

    private void mostrarAutoresGuardados() {

        libros = repositorio.findAll();
        libros.forEach(l->{
            autors.add(l.getAutores().get(0));
        });

        autors.stream()
                .sorted(Comparator.comparing(Autor::toString))
                .forEach(System.out::println);
    }

    private void mostrarAutoresVivosPorFecha() {

        System.out.println("Escriba el año que desea buscar:");
        Integer year = Integer.parseInt(teclado.nextLine());

        libros = repositorio.findAll();
        libros.forEach(l->{

            if(l.getAutores().get(0).getFechaNacimiento() < year
               && l.getAutores().get(0).getFechaFallecimiento() > year){

                autors.add(l.getAutores().get(0));
            }

        });

        autors.stream()
                .sorted(Comparator.comparing(Autor::toString))
                .forEach(System.out::println);
    }

    private void mostrarLibrosPorIdimoma() {

        var indicaciones ="""
                              Escriba el idioma que desea buscar:
                              es - Español
                              en - Ingrés
                              fr - francés
                              pt - portugués
                              """;
        System.out.println(indicaciones);
        var idioma = teclado.nextLine();

        libros = repositorio.findAll();

        libros.stream().filter(l->l.getIdiomas().get(0).equals(idioma))
                .sorted(Comparator.comparing(Libro::toString))
                .forEach(System.out::println);
    }
}
