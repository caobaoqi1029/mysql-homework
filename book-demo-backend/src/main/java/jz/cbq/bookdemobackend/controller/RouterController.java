package jz.cbq.bookdemobackend.controller;

import jz.cbq.bookdemobackend.entity.Student;
import jz.cbq.bookdemobackend.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import java.util.List;

/**
 * RouterController
 *
 * @author cbq
 * @date 2023/12/16 13:52
 * @since 1.0.0
 */
@Controller
@Slf4j
public class RouterController {
    @Resource
    StudentService studentService;

    @GetMapping("/students")
    public String getStudents(Model model) {
        List<Student> students = studentService.selectAll();
        model.addAttribute("students", students);
        return "students";
    }
}
