package com.fer.gestioncursos.controller;


import com.fer.gestioncursos.entity.Curso;
import com.fer.gestioncursos.reports.CursoExporterExcel;
import com.fer.gestioncursos.reports.CursoExporterPDF;
import com.fer.gestioncursos.repository.CursoRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    public String listarCursos(Model model,
                               @Param("keyword") String keyword,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(defaultValue = "3" ) int size
        ){

        try{
            List<Curso> cursos = new ArrayList<>();
            Pageable pagina = PageRequest.of(page-1,size);

            Page<Curso> pageCursos = null;
            System.out.println("La palabra Keyword es -> " + keyword + " " + pagina);
            if(keyword == null || keyword.equals("")){
                System.out.println("La pagina es " +  pagina);

                pageCursos = cursoRepository.findAll(pagina);

            }else{
                pageCursos = cursoRepository.findByTituloContainingIgnoreCase(keyword, pagina);
                model.addAttribute("keyword", keyword);
                System.out.println("Entró al not null y el kayword -> " + keyword);
            }
            cursos = pageCursos.getContent();

            model.addAttribute("cursos", cursos);
            model.addAttribute("currentPage", pageCursos.getNumber() + 1);
            model.addAttribute("TotalItems", pageCursos.getTotalElements());
            model.addAttribute("TotalPages", pageCursos.getTotalPages());
            model.addAttribute("pageSize", size);
            System.out.println(cursos);
        }catch(Exception exception){
            model.addAttribute("message", exception.getMessage());
        }


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

    @GetMapping("/export/pdf")
    public void generarReportePdf(HttpServletResponse response) throws IOException {

        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";

        String headerKeyValue = "attachment; filename=cursos" + currentDateTime + ".pdf";

        response.setHeader(headerKey, headerKeyValue);

        List<Curso> cursos = cursoRepository.findAll();

        CursoExporterPDF exporterPDF = new CursoExporterPDF(cursos);
        exporterPDF.export(response);

    }

    @GetMapping("/export/excel")
    public void generarReporteExcel(HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";

        String headerKeyValue = "attachment; filename=cursos" + currentDateTime + ".xlsx";

        response.setHeader(headerKey, headerKeyValue);

        List<Curso> cursos = cursoRepository.findAll();

        CursoExporterExcel exporterExcel = new CursoExporterExcel(cursos);
        exporterExcel.export(response);

    }


}
