package com.aluracursos.catalogoliterario.catalogoLiterario.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibro(

        @JsonAlias("title") String titulo,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("download_count") Long descargas
) {
}
