package jz.cbq.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import jz.cbq.backend.entity.*;
import jz.cbq.backend.service.*;
import jz.cbq.backend.vo.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Random;


/**
 * AdminController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Value("${cbq.admin-secret}")
    private String adminSecret;
    @Resource
    private IAdminService adminService;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private IStudentService studentService;
    @Resource
    private ICourseService courseService;
    @Resource
    private IMajorService majorService;
    @Resource
    private IChooseCourseService chooseCourseService;
    @Resource
    private IScoreService scoreService;
    @Resource
    private ILoginService loginService;

    /**
     * 添加管理员
     *
     * @param admin       admin
     * @param adminSecret Secret
     * @return INFO
     */
    @PostMapping("/add")
    public Result<String> addAdmin(@RequestBody Admin admin, String adminSecret) {
        if (adminSecret.equals(this.adminSecret)) {

            admin.setAdminId(adminService.getNextId());
            admin.setAdminPwd(passwordEncoder.encode(admin.getAdminPwd()));
            admin.setCreateTime(new Date());

            boolean save = adminService.save(admin);

            return save ? Result.success("注册管理员成功,你的管理员账号为 " + admin.getAdminId()) : Result.fail("系统错误，注册管理员失败");
        } else {
            return Result.fail("管理员密钥错误");
        }
    }

    /**
     * 通过 id 获取 admin
     *
     * @param adminId id
     * @return Admin
     */
    @GetMapping("/getAdminById")
    public Result<Admin> getAdminById(String adminId) {
        if (StringUtils.isBlank(adminId)) {
            return Result.fail("获取管理员信息失败");
        }
        Admin adminDatabase = adminService.getOne(new LambdaQueryWrapper<Admin>().eq(Admin::getAdminId, adminId));

        return adminDatabase != null ? Result.success(adminDatabase) : Result.fail("找不到指定 id 的管理员");
    }

    /**
     * 编辑管理员
     *
     * @param admin admin
     * @return INFO
     */
    @PutMapping("/editAdmin")
    public Result<String> editAdmin(@RequestBody Admin admin) {
        admin.setAdminPwd(passwordEncoder.encode(admin.getAdminPwd()));
        boolean update = adminService.update(admin, new LambdaQueryWrapper<Admin>().eq(Admin::getAdminId, admin.getAdminId()));

        return update ? Result.success("更新个人信息成功") : Result.fail("更新个人信息失败");
    }

    /**
     * 删除管理员
     *
     * @param token   token
     * @param adminId adminId
     * @return INFO
     */
    @DeleteMapping("/delAdmin")
    public Result<String> delAdmin(@RequestHeader("Authorization") String token, String adminId) {
        boolean removeById = adminService.removeById(adminId);
        if (removeById) {
            loginService.logout(token);
            return Result.success("注销账号成功");
        }
        return Result.fail("注销账号失败");
    }

    /**
     * 一键选课
     *
     * @return INFO
     */
    @Transactional
    @GetMapping("/adminChooseCourse")
    public Result<String> adminChooseCourse() {
        Long start = System.currentTimeMillis();
        List<Major> majorList = majorService.list();

        int nowYear = LocalDate.now().getYear() + 1900;
        int nowMonth = LocalDate.now().getMonthValue() + 1;


        if (nowMonth >= 3 && nowMonth < 9) {
            for (Major major : majorList) {
                List<Student> studentList42 = studentService.studentList42(nowYear, major.getMajorId());//大四下
                List<Student> studentList32 = studentService.studentList32(nowYear, major.getMajorId());//大三下
                List<Student> studentList22 = studentService.studentList22(nowYear, major.getMajorId());//大二下
                List<Student> studentList12 = studentService.studentList12(nowYear, major.getMajorId());//大一下
                List<Course> courseList42Must = courseService.courseList42Must(major.getMajorId());
                List<Course> courseList32Must = courseService.courseList32Must(major.getMajorId());
                List<Course> courseList22Must = courseService.courseList22Must(major.getMajorId());
                List<Course> courseList12Must = courseService.courseList12Must(major.getMajorId());

                listChooseCourse(studentList42, courseList42Must);
                listChooseCourse(studentList32, courseList32Must);
                listChooseCourse(studentList22, courseList22Must);
                listChooseCourse(studentList12, courseList12Must);
            }
        } else {
            for (Major major : majorList) {
                List<Student> studentList51 = studentService.studentList51(nowYear, major.getMajorId());
                List<Student> studentList41 = studentService.studentList41(nowYear, major.getMajorId());//大四上
                List<Student> studentList31 = studentService.studentList31(nowYear, major.getMajorId());//大三上
                List<Student> studentList21 = studentService.studentList21(nowYear, major.getMajorId());//大二上
                List<Student> studentList11 = studentService.studentList11(nowYear, major.getMajorId());//大一上

                List<Course> courseList51Must = courseService.courseList51Must(major.getMajorId());
                List<Course> courseList41Must = courseService.courseList41Must(major.getMajorId());
                List<Course> courseList31Must = courseService.courseList31Must(major.getMajorId());
                List<Course> courseList21Must = courseService.courseList21Must(major.getMajorId());
                List<Course> courseList11Must = courseService.courseList11Must(major.getMajorId());

                listChooseCourse(studentList51, courseList51Must);
                listChooseCourse(studentList41, courseList41Must);
                listChooseCourse(studentList31, courseList31Must);
                listChooseCourse(studentList21, courseList21Must);
                listChooseCourse(studentList11, courseList11Must);
            }
        }
        Long end = System.currentTimeMillis();
        return Result.success("一键选课完毕,服务器花费时间" + (end - start) + "ms");
    }


    public void listChooseCourse(List<Student> studentList, List<Course> courseList) {
        ChooseCourse chooseCourse = new ChooseCourse();
        for (Student student : studentList) {
            for (Course course : courseList) {
                chooseCourse.setChooseCourseId(student.getStuId() + "choosecourse" + course.getCourseId().substring(6, 10));
                LambdaQueryWrapper<ChooseCourse> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(ChooseCourse::getChooseCourseId, chooseCourse.getChooseCourseId());
                ChooseCourse one = chooseCourseService.getOne(queryWrapper);
                if (null != one) {
                    continue;
                }
                chooseCourse.setStuId(student.getStuId());
                chooseCourse.setCourseId(course.getCourseId());
                chooseCourse.setCreateTime(new Date());
                chooseCourse.setState(1);
                chooseCourse.setStuName(student.getStuName());
                chooseCourse.setCourseName(course.getCourseName());
                chooseCourse.setMajorName(student.getMajorName());
                chooseCourse.setCoursePeriod(course.getCoursePeriod());
                chooseCourse.setIfDegree(course.getIfDegree());
                chooseCourseService.save(chooseCourse);//有事务，不怕部分成功
                courseService.updateStuChooseNum(course.getCourseId());
            }
        }
    }

    @Transactional
    @GetMapping("/adminScoreRandomly")
    public Result<String> adminScoreRandomly() {
        Long start = System.currentTimeMillis();
        Random random = new Random();
        Score score = new Score();
        List<ChooseCourse> chooseCourseList = chooseCourseService.list();//select * from t_choose_course
        for (ChooseCourse chooseCourse : chooseCourseList) {
            score.setScoreId(chooseCourse.getStuId() + "-" + chooseCourse.getChooseCourseId());
            LambdaQueryWrapper<Score> scoreQueryWrapper = new LambdaQueryWrapper<>();
            scoreQueryWrapper.eq(Score::getScoreId, score.getScoreId());
            Score scoreDatabase = scoreService.getOne(scoreQueryWrapper);
            if (null != scoreDatabase)//针对此选课记录打过分
            {
                continue;
            }
            score.setChooseCourseId(chooseCourse.getChooseCourseId());
            score.setStuId(chooseCourse.getStuId());
            score.setCourseName(chooseCourse.getCourseName());
            score.setStuName(chooseCourse.getStuName());
            score.setCreateTime(new Date());
            score.setScore(random.nextInt(101) + 50);
            scoreService.save(score);
        }
        Long end = System.currentTimeMillis();
        return Result.success("一键随机打分完毕,服务器花费时间" + (end - start) + "ms");
    }


}
