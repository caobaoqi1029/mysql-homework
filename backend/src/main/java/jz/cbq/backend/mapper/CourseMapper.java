package jz.cbq.backend.mapper;

import jz.cbq.backend.entity.Course;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * AdminMapper
 *
 * @author caobaoqi
 */
public interface CourseMapper extends BaseMapper<Course> {

    @Select("select course_id from t_course where course_id like concat('course',#{majorId},'%') order by course_id desc limit 1")
    String findMaxCourseId(String majorId);

    List<Course> getMyAllCourseList(int limit1, int limit2, String courseName, String majorName, Integer schoolPeriodNum, String coursePeriod);

    Integer getMyAllCourseCount(int limit1, int limit2, String courseName, String majorName, Integer schoolPeriodNum, String coursePeriod);

    @Update("update t_course set stu_choose_num=stu_choose_num+1 where course_id = #{courseId} ")
    void updateStuChooseNum(String courseId);

    @Select("select * from t_course where major_id=#{majorId} order by field (course_period,'大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')")
    List<Course> courseList42(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and course_period in('大三下','大三上','大二下','大二上','大一下','大一上') order by field (course_period,'大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')")
    List<Course> courseList32(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and course_period in('大二下','大二上','大一下','大一上') order by field (course_period,'大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')")
    List<Course> courseList22(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and course_period in('大一下','大一上') order by field (course_period,'大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')")
    List<Course> courseList12(String majorId);

    @Select("select * from t_course where major_id=#{majorId} order by field (course_period,'大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')")
    List<Course> courseList51(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and course_period in('大四上','大三下','大三上','大二下','大二上','大一下','大一上') order by field (course_period,'大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')")
    List<Course> courseList41(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and course_period in('大三上','大二下','大二上','大一下','大一上') order by field (course_period,'大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')")
    List<Course> courseList31(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and course_period in('大二上','大一下','大一上') order by  field (course_period,'大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')")
    List<Course> courseList21(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and course_period in('大一上') order by  field (course_period,'大一上','大一下','大二上','大二下','大三上','大三下','大四上','大四下')")
    List<Course> courseList11(String majorId);


    @Select("select * from t_course where major_id=#{majorId} and if_degree='1'")
    List<Course> courseList42Must(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and if_degree='1'  and course_period in('大三下','大三上','大二下','大二上','大一下','大一上')")
    List<Course> courseList32Must(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and  if_degree='1' and course_period in('大二下','大二上','大一下','大一上')")
    List<Course> courseList22Must(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and  if_degree='1' and course_period in('大一下','大一上')")
    List<Course> courseList12Must(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and  if_degree='1'")
    List<Course> courseList51Must(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and  if_degree='1' and course_period in('大四上','大三下','大三上','大二下','大二上','大一下','大一上')")
    List<Course> courseList41Must(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and  if_degree='1' and course_period in('大三上','大二下','大二上','大一下','大一上')")
    List<Course> courseList31Must(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and  if_degree='1' and course_period in('大二上','大一下','大一上')")
    List<Course> courseList21Must(String majorId);

    @Select("select * from t_course where major_id=#{majorId} and  if_degree='1' and course_period in('大一上' )")
    List<Course> courseList11Must(String majorId);

    @Update("update t_course set stu_choose_num=0")
    void updateChooseCourseNum();
}
