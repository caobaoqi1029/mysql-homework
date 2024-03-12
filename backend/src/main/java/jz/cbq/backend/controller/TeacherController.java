package jz.cbq.backend.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jz.cbq.backend.vo.Result;
import jz.cbq.backend.entity.Class;
import jz.cbq.backend.entity.Major;
import jz.cbq.backend.entity.Student;
import jz.cbq.backend.entity.Teacher;
import jz.cbq.backend.service.IClassService;
import jz.cbq.backend.service.IMajorService;
import jz.cbq.backend.service.IStudentService;
import jz.cbq.backend.service.ITeacherService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * TeacherController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("/teacher")
public class TeacherController {
    @Resource
    private ITeacherService teacherService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private IStudentService studentService;
    @Resource
    private IClassService classService;
    @Resource
    private IMajorService majorService;

    /**
     * 条件老师
     *
     * @param teacher teacher
     * @return INFO
     */
    @Transactional
    @PostMapping("add")
    public Result<String> addTea(@RequestBody Teacher teacher) {
        String classYear = teacher.getClassYear();
        String majorName = teacher.getMajorName();
        String classId = teacher.getClassId();
        String teaName = teacher.getTeaName();
        String teaIdCard = teacher.getTeaIdCard();
        teacher.setClassName(classYear + "级" + majorName + classId.charAt(6) + "班");
        String className = teacher.getClassName();

        Teacher one = teacherService.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getClassId, classId));
        Teacher one1 = teacherService.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getTeaIdCard, teaIdCard).last("LIMIT 1"));
        Teacher one2 = teacherService.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getClassName, className));
        Student student = studentService.getOne(new LambdaQueryWrapper<Student>().eq(Student::getStuIdCard, teaIdCard).last("LIMIT 1"));

        if (null != one || null != one2) {
            return Result.fail("该班级已有班主任");
        } else if (null != one1) {
            return Result.fail("该用户已注册班主任编号");
        } else if (null != student) {
            return Result.fail("该用户已注册学生");
        }

        teacher.setTeaId("tea" + classId);
        teacher.setMajorId(classId.substring(4, 6));
        teacher.setCreateTime(new Date());
        teacher.setClassNo(classId.substring(6, 7));
        teacher.setTeaPwd(passwordEncoder.encode(teacher.getTeaIdCard().substring(12, 18)));
        boolean save = teacherService.save(teacher);
        if (save) {
            studentService.update(new LambdaUpdateWrapper<Student>().set(Student::getTeaName, teaName).set(Student::getTeaId, teacher.getTeaId()).eq(Student::getClassId, classId));
            classService.update(new LambdaUpdateWrapper<Class>().set(Class::getTeaName, teacher.getTeaName()).eq(Class::getClassId, classId));
            majorService.update(new LambdaUpdateWrapper<Major>().setSql("tea_total=tea_total+1").eq(Major::getMajorId, teacher.getMajorId()));

            return Result.success("添加班主任成功,班主任编号为" + teacher.getTeaId());
        }
        return Result.fail("添加班主任失败");
    }

    /**
     * 条件分页查询老师信息
     *
     * @param pageNum   pageNum
     * @param pageSize  pageSize
     * @param teaId     teaId
     * @param teaName   teaName
     * @param className className
     * @return Map<String, Object>
     */
    @GetMapping("/teaPageList")
    public Result<Map<String, Object>> teaPageList(int pageNum, int pageSize, @RequestParam(value = "teaId", required = false) String teaId,
                                                   @RequestParam(value = "teaName", required = false) String teaName,
                                                   @RequestParam(value = "className", required = false) String className) {
        Map<String, Object> data = new HashMap<>();
        Page<Teacher> page = new Page<>(pageNum, pageSize);
        teacherService.page(page,
                new LambdaQueryWrapper<Teacher>().like(StringUtils.hasLength(teaName), Teacher::getTeaName, teaName)
                        .like(StringUtils.hasLength(teaId), Teacher::getTeaId, teaId)
                        .like(StringUtils.hasLength(className), Teacher::getClassName, className)
                        .orderByDesc(Teacher::getCreateTime));
        data.put("teaList", page.getRecords());
        data.put("total", page.getTotal());
        return Result.success(data);
    }

    /**
     * 通过 id 获取老师
     *
     * @param teaId teaId
     * @return Teacher
     */
    @GetMapping("/getTeaById")
    public Result<Teacher> getTeaById(String teaId) {
        Teacher one = teacherService.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getTeaId, teaId).last("LIMIT 1"));

        return one != null ? Result.success(one) : Result.fail("打开编辑表单失败");
    }

    /**
     * 编辑老师
     *
     * @param teacher teacher
     * @return INFO
     */
    @PutMapping("/editById")
    public Result<String> editById(@RequestBody Teacher teacher) {
        teacher.setTeaPwd(passwordEncoder.encode(teacher.getTeaPwd()));
        boolean update = teacherService.update(teacher, new LambdaUpdateWrapper<Teacher>().eq(Teacher::getTeaId, teacher.getTeaId()));
        if (update) {
            classService.update(new LambdaUpdateWrapper<Class>().set(Class::getTeaName, teacher.getTeaName()).eq(Class::getClassId, teacher.getClassId()));
            studentService.update(new LambdaUpdateWrapper<Student>().set(Student::getTeaName, teacher.getTeaName()).eq(Student::getTeaId, teacher.getTeaId()));
            return Result.success("编辑成功");
        }
        return Result.fail("编辑失败");
    }

    /**
     * 根据 id 删除老师
     *
     * @param teaId teaId
     * @return INFO
     */
    @DeleteMapping("/delTea")
    public Result<String> delTea(String teaId) {
        Teacher teacher = teacherService.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getTeaId, teaId));
        boolean remove = teacherService.remove(new LambdaQueryWrapper<Teacher>().eq(Teacher::getTeaId, teaId));
        if (remove) {
            studentService.update(new LambdaUpdateWrapper<Student>().set(Student::getTeaName, "").set(Student::getTeaId, "").eq(Student::getTeaId, teaId));
            classService.update(new LambdaUpdateWrapper<Class>().set(Class::getTeaName, "").eq(Class::getClassId, teacher.getClassId()));
            majorService.update(new LambdaUpdateWrapper<Major>().setSql("tea_total=tea_total-1").eq(Major::getMajorId, teacher.getMajorId()));
            return Result.success("删除班主任成功");
        }
        return Result.fail("删除班主任失败");
    }
}
