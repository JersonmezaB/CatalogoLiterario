package com.aluracursos.catalogoliterario.catalogoLiterario;

import com.aluracursos.catalogoliterario.catalogoLiterario.principal.Principal;
import com.aluracursos.catalogoliterario.catalogoLiterario.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CatalogoLiterarioApplication implements CommandLineRunner {

	@Autowired
	private LibroRepository repository;
	public static void main(String[] args) {
		SpringApplication.run(CatalogoLiterarioApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Principal principal = new Principal(repository);
		principal.muestraElMenu();
	}
}
