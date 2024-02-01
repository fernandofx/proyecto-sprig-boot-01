package com.fer.gestioncursos.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "cursos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Curso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 128, nullable = false)
    private String titulo;

    @Column(length = 256)
    private String descripcion;

    @Column(nullable = false)
    private int nivel;

    @Column(name = "estado_publicacion")
    private boolean isPublicado;

}
