package jz.cbq.backend.service;

import jz.cbq.backend.entity.Course;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * ICourseService
 *
 * @author caobaoqi
 */
public interface ICourseService extends IService<Course> {
    /**
     * 获取下一个课程 id
     * @param majorId 专业 id
     * @return id
     */
    String findNextCourseId(String majorId);

    /**
     * 获取课程列表
     * @param limit1 limit1
     * @param limit2 limit2
     * @param courseName 课程名称
     * @param majorName 专业名称
     * @param schoolPeriodNum  schoolPeriodNum
     * @param coursePeriod 是否选修
     * @return List<Course>
     */
    List<Course> getMyAllCourseList(int limit1,int limit2,String courseName,String majorName,Integer schoolPeriodNum,String coursePeriod);
    /**
     * 获取课程数量
     * @param limit1 limit1
     * @param limit2 limit2
     * @param courseName 课程名称
     * @param majorName 专业名称
     * @param schoolPeriodNum  schoolPeriodNum
     * @param coursePeriod 是否选修
     * @return count
     */
    Integer getMyAllCourseCount(int limit1, int limit2, String courseName, String majorName, Integer schoolPeriodNum,String coursePeriod);

    /**
     * 更新课程数量
     * @param courseId 课程 id
     */
    void updateStuChooseNum(String courseId);

    /**
     * 通过专业 id 获取大四下课程数据 (选修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList42(String majorId);
    /**
     * 通过专业 id 获取大三下课程数据 (选修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList32(String majorId);
    /**
     * 通过专业 id 获取大二下课程数据 (选修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList22(String majorId);
    /**
     * 通过专业 id 获取大一下课程数据 (选修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList12(String majorId);
    /**
     * 通过专业 id 获取大四上课程数据 (选修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList41(String majorId);
    /**
     * 通过专业 id 获取大三上课程数据 (选修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList31(String majorId);
    /**
     * 通过专业 id 获取大二上课程数据 (选修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList21(String majorId);
    /**
     * 通过专业 id 获取大一上课程数据 (选修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList11(String majorId);

    List<Course> courseList51(String majorId);


    /**
     * 通过专业 id 获取大四下课程数据 (必修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList42Must(String majorId);
    /**
     * 通过专业 id 获取大三下课程数据 (必修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList32Must(String majorId);
    /**
     * 通过专业 id 获取大二下课程数据 (必修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList22Must(String majorId);
    /**
     * 通过专业 id 获取大一下课程数据 (必修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList12Must(String majorId);

    /**
     * 通过专业 id 获取大四上课程数据 (必修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList41Must(String majorId);
    /**
     * 通过专业 id 获取大三上课程数据 (必修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList31Must(String majorId);
    /**
     * 通过专业 id 获取大二上课程数据 (必修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList21Must(String majorId);
    /**
     * 通过专业 id 获取大一上课程数据 (必修)
     * @param majorId majorId
     * @return List<Course>
     */
    List<Course> courseList11Must(String majorId);

    List<Course> courseList51Must(String majorId);

    /**
     * 更新选课数
     */
    void updateChooseCourseNum();
}
