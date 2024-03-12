package jz.cbq.backend.service.impl;

import jz.cbq.backend.entity.Course;
import jz.cbq.backend.mapper.CourseMapper;
import jz.cbq.backend.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * CourseServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Resource
    private CourseMapper courseMapper;

    @Override
    public String findNextCourseId(String majorId) {//05
        String maxCourseId = courseMapper.findMaxCourseId(majorId);//course 05 02
        int courseIdSuffix = 1;
        if (null != maxCourseId) {
            courseIdSuffix = Integer.parseInt(maxCourseId.substring(8, 10)) + 1;//3
            return "course" + majorId + String.format("%02d", courseIdSuffix);//03
        }
        return "course" + majorId + "01";
    }

    @Override
    public List<Course> getMyAllCourseList(int limit1, int limit2, String courseName, String majorName, Integer schoolPeriodNum, String coursePeriod) {
        return courseMapper.getMyAllCourseList(limit1, limit2, courseName, majorName, schoolPeriodNum, coursePeriod);
    }

    @Override
    public Integer getMyAllCourseCount(int limit1, int limit2, String courseName, String majorName, Integer schoolPeriodNum, String coursePeriod) {
        return courseMapper.getMyAllCourseCount(limit1, limit2, courseName, majorName, schoolPeriodNum, coursePeriod);
    }

    @Async("taskExecutor")
    @Override
    public void updateStuChooseNum(String courseId) {
        System.out.println("线程" + Thread.currentThread().getName() + " 执行异步任务：更新课程："+courseId+"学生选课数量+1");
        courseMapper.updateStuChooseNum(courseId);
    }

    @Override
    public List<Course> courseList42(String majorId) {
        return courseMapper.courseList42(majorId);
    }

    @Override
    public List<Course> courseList32(String majorId) {
        return courseMapper.courseList32(majorId);
    }

    @Override
    public List<Course> courseList22(String majorId) {
        return courseMapper.courseList22(majorId);
    }

    @Override
    public List<Course> courseList12(String majorId) {
        return courseMapper.courseList12(majorId);
    }

    @Override
    public List<Course> courseList41(String majorId) {
        return courseMapper.courseList41(majorId);
    }

    @Override
    public List<Course> courseList31(String majorId) {
        return courseMapper.courseList31(majorId);
    }

    @Override
    public List<Course> courseList21(String majorId) {
        return courseMapper.courseList21(majorId);
    }

    @Override
    public List<Course> courseList11(String majorId) {
        return courseMapper.courseList11(majorId);
    }

    @Override
    public List<Course> courseList51(String majorId) {
        return courseMapper.courseList51(majorId);
    }


    @Override
    public List<Course> courseList42Must(String majorId) {
        return courseMapper.courseList42Must(majorId);
    }

    @Override
    public List<Course> courseList32Must(String majorId) {
        return courseMapper.courseList32Must(majorId);
    }

    @Override
    public List<Course> courseList22Must(String majorId) {
        return courseMapper.courseList22Must(majorId);
    }

    @Override
    public List<Course> courseList12Must(String majorId) {
        return courseMapper.courseList12Must(majorId);
    }

    @Override
    public List<Course> courseList41Must(String majorId) {
        return courseMapper.courseList41Must(majorId);
    }

    @Override
    public List<Course> courseList31Must(String majorId) {
        return courseMapper.courseList31Must(majorId);
    }

    @Override
    public List<Course> courseList21Must(String majorId) {
        return courseMapper.courseList21Must(majorId);
    }

    @Override
    public List<Course> courseList11Must(String majorId) {
        return courseMapper.courseList11Must(majorId);
    }

    @Override
    public List<Course> courseList51Must(String majorId) {
        return courseMapper.courseList51Must(majorId);
    }

    @Async("taskExecutor")
    @Override
    public void updateChooseCourseNum() {
        System.out.println("线程--" + Thread.currentThread().getName() + "  执行异步任务：置空课程表选课数");
        courseMapper.updateChooseCourseNum();
    }
}
