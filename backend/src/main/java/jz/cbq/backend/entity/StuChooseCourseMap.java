package jz.cbq.backend.entity;

import lombok.Data;

import java.util.Date;

@Data
public class StuChooseCourseMap {
    /**
     * 选课 id
     */
    private String chooseCourseId;
    /**
     * 课程 id
     */
    private String courseId;
    /**
     * 专业 id
     */
    private String majorId;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 是否必修
     */
    private String ifDegree;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 课程时间
     */
    private String coursePeriod;
    /**
     * 学生数
     */
    private String stuChooseNum;
    /**
     * 学生 id
     */
    private String stuId;
    /**
     * 状态
     */
    private String state;
    /**
     * 学生名称
     */
    private String stuName;
}
