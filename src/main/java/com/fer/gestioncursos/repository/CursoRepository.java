package com.fer.gestioncursos.repository;

import com.fer.gestioncursos.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Integer> {

}
