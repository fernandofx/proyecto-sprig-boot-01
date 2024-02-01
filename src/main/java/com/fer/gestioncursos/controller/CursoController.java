package com.fer.gestioncursos.controller;


import com.fer.gestioncursos.entity.Curso;
import com.fer.gestioncursos.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CursoController {

    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping("/")
    public String home(){
        return "redirect:/cursos";
    }

    @GetMapping("/cursos")
    public String listarCursos(Model model){
        List<Curso> cursos = cursoRepository.findAll();
        model.addAttribute("cursos", cursos);
        return "cursos";

    }

    @GetMapping("/cursos/nuevo")
    public String agregarCurso(Model model   ){
        Curso curso = new Curso();
        model.addAttribute("curso", curso);
        model.addAttribute("title", "Agregar un Nuevo Curso");

        return "curso_form";
    }

    @PostMapping("/cursos/save")
    public String guardarCurso(Curso curso, RedirectAttributes redirectAttributes){

        try {
            cursoRepository.save(curso);
            redirectAttributes.addFlashAttribute("message", "El curso se añadió ");
        }catch(Exception ex){

            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/cursos";
    }

    @GetMapping("/cursos/{id}")
    public String editarCurso(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes){

        try {
            Curso curso = cursoRepository.findById(id).get();
            model.addAttribute("pageTitle", "Editar curso " + id);
            model.addAttribute("curso", curso);
            return "curso_form";
        }catch(Exception ex){

            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }

        return "redirect:/cursos";
    }

    @GetMapping("/cursos/delete/{id}")
    public String eliminarCurso(@PathVariable Integer id, Model model, RedirectAttributes redirectAttributes){

        try{
            cursoRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("message", "Curso eliminado");
        }catch(Exception ex){
            redirectAttributes.addFlashAttribute("message", ex.getMessage());
        }
        return "redirect:/cursos";
    }



}
