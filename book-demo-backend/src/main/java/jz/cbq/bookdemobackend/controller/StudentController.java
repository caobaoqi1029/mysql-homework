package jz.cbq.bookdemobackend.controller;

import jz.cbq.bookdemobackend.annotation.CBQLog;
import jz.cbq.bookdemobackend.entity.Result;
import jz.cbq.bookdemobackend.entity.Student;
import jz.cbq.bookdemobackend.service.StudentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * StudentController
 *
 * @author cbq
 * @date 2023/12/15 13:35
 * @since 1.0.0
 */
@RestController
@Slf4j
@RequestMapping("api/v1/students")
public class StudentController {
    @Resource
    StudentService studentService;

    @GetMapping("/{id}")
    public Result<Student> findById(@PathVariable("id") Integer id) {
        Student student = studentService.findById(id);
        return !Objects.isNull(student) ? Result.ok(student) : Result.error();
    }

    @CBQLog
    @GetMapping("")
    public Result<List<Student>> selectAll() {
        List<Student> students = studentService.selectAll();
        return !Objects.isNull(students) ? Result.ok(students) : Result.error();

    }

    @PostMapping("")
    public Result<String> insert(@RequestBody Student student) {
        return studentService.save(student) != 0 ? Result.ok("添加成功") : Result.error("添加失败");
    }

    @PutMapping("/{id}")
    public Result<String> updateById(@PathVariable("id") Integer id, @RequestBody Student student) {
        student.setId(id);
        return studentService.updateById(student) != 0 ? Result.ok("更新成功") : Result.error("更新失败");
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteById(@PathVariable("id") Integer id) {
        return studentService.deleteById(id) != 0 ? Result.ok("删除成功") : Result.error("删除失败");
    }
}
