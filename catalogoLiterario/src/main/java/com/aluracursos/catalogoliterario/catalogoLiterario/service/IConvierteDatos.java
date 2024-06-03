package com.aluracursos.catalogoliterario.catalogoLiterario.service;

import com.fasterxml.jackson.annotation.JsonAlias;

public interface IConvierteDatos {

    <T> T obtenerDatos(String json, Class<T> clase);
}
