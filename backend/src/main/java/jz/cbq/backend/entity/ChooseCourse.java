package jz.cbq.backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

/**
 * 选课 实体类
 *
 * @author caobaoqi
 */
@Data
@ApiModel(value = "选课")
public class ChooseCourse {
    /**
     * 选课 id
     */
    @TableId(value = "choose_course_id")
    private String ChooseCourseId;
    /**
     * 学生 id
     */
    private String stuId;
    /**
     * 课程 id
     */
    private String CourseId;
    /**
     * 创建时间
     */
    private Date CreateTime;
    /**
     * 状态
     */
    private Integer state;
    /**
     * 学生名称
     */
    private String stuName;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 课程时间
     */
    private String coursePeriod;
    /**
     * 是否选修
     */
    private String ifDegree;
    /**
     * 是否删除
     */
    private Integer deleted;

}
