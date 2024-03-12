package jz.cbq.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jz.cbq.backend.vo.Result;
import jz.cbq.backend.entity.*;
import jz.cbq.backend.entity.Class;
import jz.cbq.backend.service.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * StudentController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("/student")
public class StudentController {
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private IStudentService studentService;
    @Resource
    private IClassService classService;
    @Resource
    private ITeacherService teacherService;
    @Resource
    private IMajorService majorService;
    @Resource
    private ICourseService courseService;
    @Resource
    private IChooseCourseService chooseCourseService;
    @Resource
    private IScoreService scoreService;
    @Resource
    private IStuMessageService stuMessageService;
    @Resource
    private ITeaMessageService teaMessageService;

    /**
     * 添加学生
     *
     * @param student student
     * @return INFO
     */
    @Transactional
    @PostMapping("/add")
    public Result<String> addStu(@RequestBody Student student) {
        String admissionYear = student.getAdmissionYear();
        String majorName = student.getMajorName();
        String classId = student.getClassId();
        String idCard = student.getStuIdCard();

        Student repeatStuIDCard = studentService.getOne(new LambdaQueryWrapper<Student>().eq(Student::getStuIdCard, idCard));
        if (null != repeatStuIDCard) {
            return Result.fail("该 IDCard 已注册学生");
        }

        Teacher repeatTeaIDCard = teacherService.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getTeaIdCard, idCard));
        if (null != repeatTeaIDCard) {
            return Result.fail("该 IDCard 已注册班主任");
        }

        String nextStuId = studentService.findNextStuId(classId);
        student.setStuId(nextStuId);

        Class repeatClassId = classService.getOne(new LambdaQueryWrapper<Class>().eq(Class::getClassId, classId));

        if (!StringUtils.hasText(repeatClassId.getTeaName())) {
            student.setTeaName("");
            student.setTeaId("");
        } else {
            Teacher teacher = teacherService.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getClassId, classId).last("LIMIT 1"));
            student.setTeaId(teacher.getTeaId());
            student.setTeaName(teacher.getTeaName());
        }
        student.setMajorId(classId.substring(4, 6));
        student.setClassName(admissionYear + " 级" + majorName + classId.charAt(6) + " 班");
        student.setClassNo(classId.substring(6, 7));
        student.setCreateTime(new Date());
        student.setStuPwd(passwordEncoder.encode(idCard.substring(12, 18)));
        boolean save = studentService.save(student);
        if (save) {
            classService.update(new LambdaUpdateWrapper<Class>().setSql("stu_total=stu_total+1").eq(Class::getClassId, classId));
            majorService.update(new LambdaUpdateWrapper<Major>().setSql("stu_total=stu_total+1").eq(Major::getMajorId, student.getMajorId()));

            System.out.println("线程--" + Thread.currentThread().getName() + " 执行异步任务：" + student.getStuId());
            return Result.success("添加学生成功");
        }
        return Result.fail("添加学生失败");
    }

    /**
     * 条件分页展示学生列表
     *
     * @param pageNum   pageNum
     * @param pageSize  pageSize
     * @param stuId     stuId
     * @param stuName   stuName
     * @param className className
     * @return INFO
     */
    @GetMapping("/stuPageList")
    public Result<Map<String, Object>> stuPageList(int pageNum, int pageSize,
                                                   @RequestParam(value = "stuId", required = false) String stuId,
                                                   @RequestParam(value = "stuName", required = false) String stuName,
                                                   @RequestParam(value = "className", required = false) String className) {
        Map<String, Object> data = new HashMap<>();
        Page<Student> page = new Page<>(pageNum, pageSize);
        studentService.page(page,
                new LambdaQueryWrapper<Student>()
                        .like(StringUtils.hasLength(stuId), Student::getStuId, stuId)
                        .like(StringUtils.hasLength(stuName), Student::getStuName, stuName)
                        .like(StringUtils.hasLength(className), Student::getClassName, className)
                        .orderByDesc(Student::getCreateTime)
        );
        data.put("stuList", page.getRecords());
        data.put("total", page.getTotal());
        return Result.success(data);
    }

    /**
     * 根据 id 删除学生
     *
     * @param stuId stuId
     * @return INFO
     */
    @Transactional
    @DeleteMapping("/delete")
    public Result<String> deleteStu(String stuId) {
        Score scoreDatabase = scoreService.getOne(new LambdaQueryWrapper<Score>().eq(Score::getStuId, stuId).last("LIMIT 1"));
        StuMessage stuMessageDatabase = stuMessageService.getOne(new LambdaQueryWrapper<StuMessage>().eq(StuMessage::getStuId, stuId).last("LIMIT 1"));
        TeaMessage teaMessageDatabase = teaMessageService.getOne(new LambdaQueryWrapper<TeaMessage>().eq(TeaMessage::getStuId, stuId).last("LIMIT 1"));
        ChooseCourse chooseCourseDatabase = chooseCourseService.getOne(new LambdaQueryWrapper<ChooseCourse>().eq(ChooseCourse::getStuId, stuId).last("LIMIT 1"));

        if (scoreDatabase != null) {
            return Result.fail("该学生还有成绩数据,无法删除");
        } else if (stuMessageDatabase != null || teaMessageDatabase != null) {
            return Result.fail("该学生还有消息数据,无法删除");
        } else if (chooseCourseDatabase != null) {
            return Result.fail("该学生还有选课数据,无法删除");
        }

        Student student = studentService.getOne(new LambdaQueryWrapper<Student>().eq(Student::getStuId, stuId));
        boolean delStuById = studentService.remove(new LambdaQueryWrapper<Student>().eq(Student::getStuId, stuId));
        if (delStuById) {
            classService.update(new LambdaUpdateWrapper<Class>().setSql("stu_total=stu_total-1").eq(Class::getClassId, student.getClassId()));
            majorService.update(new LambdaUpdateWrapper<Major>().setSql("stu_total=stu_total-1").eq(Major::getMajorId, student.getMajorId()));
            return Result.success("删除学生成功");
        }
        return Result.fail("删除学生失败");
    }

    /**
     * 通过 id 获取学生
     *
     * @param stuId stuId
     * @return INFO
     */
    @GetMapping("/getByStuId")
    public Result<Student> getByStuId(String stuId) {
        if (!StringUtils.hasText(stuId)) {
            return Result.fail("学生 id 不能为空");
        }
        Student studentDatabase = studentService.getOne(new LambdaQueryWrapper<Student>().eq(Student::getStuId, stuId));

        return studentDatabase != null ? Result.success(studentDatabase) : Result.fail("没有该学生信息");
    }

    /**
     * 通过 id 编辑学生
     *
     * @param student student
     * @return INFO
     */
    @Transactional
    @PutMapping("editByStuId")
    public Result<String> editByStuId(@RequestBody Student student) {
        student.setStuPwd(passwordEncoder.encode(student.getStuPwd()));
        boolean update = studentService.update(student, new LambdaQueryWrapper<Student>().eq(Student::getStuId, student.getStuId()));
        if (update) {
            chooseCourseService.updateStuName(student.getStuId(), student.getStuName());
            scoreService.updateStuName(student.getStuId(), student.getStuName());
            return Result.success("编辑学生成功");
        }
        return Result.fail("编辑学生失败");
    }

    /**
     * 条件分页展示课程信息
     *
     * @param pageNum         pageNum
     * @param pageSize        pageSize
     * @param courseName      courseName
     * @param coursePeriod    coursePeriod
     * @param majorName       majorName
     * @param schoolPeriodNum schoolPeriodNum
     * @return INFO
     */
    @GetMapping("/coursePageList")
    public Result<Map<String, Object>> coursePageList(int pageNum, int pageSize,
                                                      @RequestParam(value = "courseName", required = false) String courseName,
                                                      @RequestParam(value = "coursePeriod", required = false) String coursePeriod,
                                                      String majorName, Integer schoolPeriodNum) {
        Map<String, Object> data = new HashMap<>();
        List<Course> myAllCourseList = courseService.getMyAllCourseList((pageNum - 1) * pageSize, pageSize, courseName, majorName, schoolPeriodNum, coursePeriod);
        Integer total = courseService.getMyAllCourseCount((pageNum - 1) * pageSize, pageSize, courseName, majorName, schoolPeriodNum, coursePeriod);
        data.put("courseList", myAllCourseList);
        data.put("total", total);
        return Result.success(data);
    }
}
