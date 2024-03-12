package jz.cbq.backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 课程 实体类
 *
 * @author caobaoqi
 */
@Data
@ApiModel(value = "课程")
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 课程 id
     */
    @TableId(value = "course_id")
    private String courseId;
    /**
     * 专业 id
     */
    private String majorId;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 课程名称
     */
    private String courseName;
    /**
     * 是否选修
     */
    private String ifDegree;
    /**
     * 课程时间
     */
    private String coursePeriod;
    /**
     * 选择该课程的学生数量
     */
    private Integer stuChooseNum;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否删除
     */
    private Integer deleted;

}
