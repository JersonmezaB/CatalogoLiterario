package com.aluracursos.catalogoliterario.catalogoLiterario.model;

import com.aluracursos.catalogoliterario.catalogoLiterario.service.ConvierteDatos;
import jakarta.persistence.*;
import org.json.JSONObject;

import java.util.List;

@Entity
@Table(name = "libros")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String titulo;
    @OneToMany(mappedBy = "libro", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Autor> autores;
    private List<String> idiomas;
    private Long descargas;


    public Libro(){}

    public Libro(DatosLibro datosLibro){
        this.titulo = datosLibro.titulo();
        this.idiomas = datosLibro.idiomas();
        this.descargas = datosLibro.descargas();
    }

    @Override
    public String toString() {
        return
                "\n"+
                "**********-Libro-**********"+"\n"+
                "Titulo = " + titulo + "\n"+
                "Autor = "+autores.get(0).getNombre() + "\n"+
                "Idiomas = "+idiomas + "\n"+
                "Descargas = " + descargas+ "\n"+
                "***************************"+ "\n";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<String> getIdiomas() {
        return idiomas;
    }

    public void setIdiomas(List<String> idiomas) {
        this.idiomas = idiomas;
    }

    public Long getDescargas() {
        return descargas;
    }

    public void setDescargas(Long descargas) {
        this.descargas = descargas;
    }

    public List<Autor> getAutores() {
        return autores;
    }

    public void setAutores(List<Autor> autores) {
        autores.forEach(e -> e.setLibro(this)) ;
        this.autores = autores;
    }
}
