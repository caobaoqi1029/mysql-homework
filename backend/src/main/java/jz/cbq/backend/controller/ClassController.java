package jz.cbq.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jz.cbq.backend.entity.Student;
import jz.cbq.backend.entity.Teacher;
import jz.cbq.backend.service.IStudentService;
import jz.cbq.backend.service.ITeacherService;
import jz.cbq.backend.vo.Result;
import jz.cbq.backend.entity.Class;
import jz.cbq.backend.entity.Major;
import jz.cbq.backend.service.IClassService;
import jz.cbq.backend.service.IMajorService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ClassController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("/class")
public class ClassController {
    @Resource
    private IMajorService majorService;
    @Resource
    private ITeacherService teacherService;
    @Resource
    private IStudentService studentService;
    @Resource
    private IClassService classService;

    /**
     * 添加班级
     *
     * @param data data
     * @return INFO
     */
    @Transactional
    @PostMapping("/add")
    public Result<String> addClass(@RequestBody Class data) {
        Major major = majorService.getOne(new LambdaQueryWrapper<Major>().eq(Major::getMajorName, data.getMajorName()).last("LIMIT 1"));
        Class maxClass = classService.getOne(new LambdaQueryWrapper<Class>().likeRight(Class::getClassId, data.getClassYear() + major.getMajorId()).orderByDesc(Class::getClassId).last("LIMIT 1"));
        if (maxClass != null) {
            data.setClassId(Integer.valueOf(maxClass.getClassId()) + 1 + "");
        } else {
            data.setClassId(data.getClassYear() + major.getMajorId() + "1");
        }
        data.setClassName(data.getClassYear() + "级" + data.getMajorName() + data.getClassId().charAt(6) + "班");
        data.setCreateTime(new Date());
        data.setMajorId(major.getMajorId());
        data.setStuTotal(0);
        boolean save = classService.save(data);
        if (save) {
            majorService.update(new LambdaUpdateWrapper<Major>().setSql("class_total=class_total+1").eq(Major::getMajorName, data.getMajorName()));
            return Result.success("添加班级成功");
        }
        return Result.fail("添加班级失败");
    }

    /**
     * 条件分页展示班级信息
     *
     * @param pageNum   pageNum
     * @param pageSize  pageSize
     * @param className className
     * @param teaName   teaName
     * @return Map<String, Object>
     */
    @GetMapping("/classPageList")
    public Result<Map<String, Object>> classPageList(int pageNum, int pageSize, @RequestParam(value = "className", required = false) String className,
                                                     @RequestParam(value = "teaName", required = false) String teaName) {
        Map<String, Object> data = new HashMap<>();
        Page<Class> page = new Page<>(pageNum, pageSize);

        Page<Class> paged = classService.page(page, new LambdaQueryWrapper<Class>().like(StringUtils.hasLength(className), Class::getClassName, className)
                .like(StringUtils.hasLength(teaName), Class::getTeaName, teaName).orderByDesc(Class::getCreateTime));
        if (paged != null) {
            data.put("classList", page.getRecords());
            data.put("total", page.getTotal());
        }
        if (data.keySet().isEmpty()) {
            return Result.fail("分页查询班级失败");
        }
        return Result.success(data);
    }

    /**
     * 通过 id 删除老师
     *
     * @param classId classId
     * @return INFO
     */
    @Transactional
    @DeleteMapping("/delClassById")
    public Result<String> delClassById(String classId) {
        Teacher teacher = teacherService.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getClassId, classId).last("LIMIT 1"));
        Student student = studentService.getOne(new LambdaQueryWrapper<Student>().eq(Student::getClassId, classId).last("LIMIT 1"));
        if (student != null) {
            Major major = majorService.getOne(new LambdaQueryWrapper<Major>().eq(Major::getMajorName, student.getMajorName()).gt(Major::getClassTotal, 0).last("LIMIT 1"));
            if (major != null) {
                return Result.fail("该班级还有专业数据,无法删除");
            }
        }
        if (teacher != null) {
            return Result.fail("该班级还有老师数据,无法删除");
        } else if (student != null) {
            return Result.fail("该班级还有学生数据,无法删除");
        }

        Class serviceOne = classService.getOne(new LambdaQueryWrapper<Class>().eq(Class::getClassId, classId));
        boolean remove = classService.remove(new LambdaQueryWrapper<Class>().eq(Class::getClassId, classId));
        if (remove) {
            majorService.update(new LambdaUpdateWrapper<Major>().setSql("class_total=class_total-1").eq(Major::getMajorId, serviceOne.getMajorId()));
            return Result.success("删除班级成功");
        }
        return Result.fail("删除班级失败");
    }

    /**
     * 根据专业名称获取班级列表
     *
     * @param majorName majorName
     * @param classYear classYear
     * @return List<Class>
     */
    @GetMapping("getClassesByMajorName")
    public Result<List<Class>> getClassesByMajorName(String majorName, String classYear) {
        List<Class> classList = classService.list(new LambdaQueryWrapper<Class>().eq(Class::getMajorName, majorName).eq(Class::getClassYear, classYear));
        return Result.success(classList);
    }

}
