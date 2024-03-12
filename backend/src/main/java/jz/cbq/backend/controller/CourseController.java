package jz.cbq.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jz.cbq.backend.vo.Result;
import jz.cbq.backend.entity.Course;
import jz.cbq.backend.entity.Major;
import jz.cbq.backend.service.ICourseService;
import jz.cbq.backend.service.IMajorService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CourseController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("/course")
public class CourseController {
    @Resource
    private IMajorService majorService;
    @Resource
    private ICourseService courseService;

    /**
     * 添加课程
     *
     * @param course course
     * @return INFO
     */
    @Transactional
    @PostMapping("/add")
    public Result<String> addCourse(@RequestBody Course course) {
        String courseName = course.getCourseName();
        Course courseDatabase = courseService.getOne(new LambdaQueryWrapper<Course>().eq(Course::getCourseName, courseName).last("LIMIT 1"));
        if (courseDatabase != null) {
            return Result.fail("存在同名课程，添加失败");
        }
        String majorName = course.getMajorName();
        Major major = majorService.getOne(new LambdaQueryWrapper<Major>().eq(Major::getMajorName, majorName).last("LIMIT 1"));
        course.setMajorId(major.getMajorId());
        course.setCreateTime(new Date());
        String nextCourseId = courseService.findNextCourseId(major.getMajorId());
        course.setCourseId(nextCourseId);
        course.setStuChooseNum(0);
        boolean save = courseService.save(course);
        if (save) {
            majorService.update(new LambdaUpdateWrapper<Major>().setSql("course_total=course_total+1").eq(Major::getMajorName, course.getMajorName()));
            return Result.success("添加课程成功");
        }
        return Result.fail("添加课程失败");
    }

    /**
     * 条件分页查询课程信息
     *
     * @param pageNum      pageNum
     * @param pageSize     pageSize
     * @param courseName   courseName
     * @param majorName    majorName
     * @param coursePeriod coursePeriod
     * @return Map<String, Object>
     */
    @GetMapping("/coursePageList")
    public Result<Map<String, Object>> coursePageList(int pageNum, int pageSize, @RequestParam(value = "courseName") String courseName,
                                                      @RequestParam(value = "majorName") String majorName,
                                                      @RequestParam(value = "coursePeriod") String coursePeriod) {
        Map<String, Object> data = new HashMap<>();
        Page<Course> page = new Page<>(pageNum, pageSize);
        courseService.page(page,
                new LambdaQueryWrapper<Course>().like(StringUtils.hasLength(courseName), Course::getCourseName, courseName)
                        .like(StringUtils.hasLength(majorName), Course::getMajorName, majorName)
                        .eq(StringUtils.hasLength(coursePeriod), Course::getCoursePeriod, coursePeriod)
                        .orderByDesc(Course::getCreateTime));
        data.put("courseList", page.getRecords());
        data.put("total", page.getTotal());
        return Result.success(data);
    }

    /**
     * 通过 id 获取课程
     *
     * @param courseId courseId
     * @return Course
     */
    @GetMapping("/getCourseById")
    public Result<Course> getCourseById(String courseId) {
        Course course = courseService.getOne(new LambdaQueryWrapper<Course>().eq(Course::getCourseId, courseId).last("LIMIT 1"));

        return course != null ? Result.success(course) : Result.fail("加载课程失败");
    }

    /**
     * 编辑课程
     *
     * @param course course
     * @return INFO
     */
    @Transactional
    @PutMapping("/edit")
    public Result<String> editCourseById(@RequestBody Course course) {
        boolean update = courseService.update(course, new LambdaUpdateWrapper<Course>().eq(Course::getCourseId, course.getCourseId()));

        return update ? Result.success("修改课程成功") : Result.fail("修改课程失败");
    }

    /**
     * 通过 id 删除课程
     *
     * @param courseId courseId
     * @return INFO
     */
    @Transactional
    @DeleteMapping("/delCourseById")
    public Result<String> delCourseById(String courseId) {
        Course course = courseService.getOne(new LambdaQueryWrapper<Course>().eq(Course::getCourseId, courseId));
        boolean remove = courseService.remove(new LambdaQueryWrapper<Course>().eq(Course::getCourseId, courseId));
        if (remove) {
            majorService.update(new LambdaUpdateWrapper<Major>().setSql("course_total=course_total-1").eq(Major::getMajorId, course.getMajorId()));
            return Result.success("删除课程成功");
        }
        return Result.fail("删除课程失败");
    }

    /**
     * 根据专业 id 和 年纪获取课程列表
     *
     * @param majorId       majorId
     * @param admissionYear admissionYear
     * @return List<Course>
     */
    @GetMapping("/getScoreCourse")
    public Result<List<Course>> getScoreCourse(String majorId, String admissionYear) {
        int nowYear = LocalDate.now().getYear();
        int nowMonth = LocalDate.now().getMonthValue();
        if (nowMonth >= 3 && nowMonth < 9) {
            if (nowYear - Integer.parseInt(admissionYear) >= 4) {
                List<Course> courseList = courseService.courseList42(majorId);
                return Result.success(courseList);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 3) {
                List<Course> courseList = courseService.courseList32(majorId);
                return Result.success(courseList);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 2) {
                List<Course> courseList = courseService.courseList22(majorId);
                return Result.success(courseList);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 1) {
                List<Course> courseList = courseService.courseList12(majorId);
                return Result.success(courseList);
            }
        } else {
            if (nowYear - Integer.parseInt(admissionYear) >= 4) {
                List<Course> courseList = courseService.courseList51(majorId);
                return Result.success(courseList);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 3) {
                List<Course> courseList = courseService.courseList41(majorId);
                return Result.success(courseList);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 2) {
                List<Course> courseList = courseService.courseList31(majorId);
                return Result.success(courseList);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 1) {
                List<Course> courseList = courseService.courseList21(majorId);
                return Result.success(courseList);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 0) {
                List<Course> courseList = courseService.courseList11(majorId);
                return Result.success(courseList);
            }
        }
        return Result.fail();
    }
}
