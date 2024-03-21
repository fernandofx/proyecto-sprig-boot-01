package com.fer.gestioncursos.repository;

import com.fer.gestioncursos.entity.Curso;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Integer> {

    Page<Curso> findByTituloContainingIgnoreCase(String keyword, Pageable pageable);

}
