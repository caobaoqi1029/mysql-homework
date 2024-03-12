package jz.cbq.backend.entity;

import lombok.Data;

import java.util.List;
@Data
public class StuScoreMap {
    /**
     * 学生 id
     */
    private String stuId;
    /**
     * 学生名称
     */
    private String stuName;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 课程成绩
     */
    private List<Integer> courseScore;
    /**
     * 课程数量
     */
    private Integer scoreTotal;
}
