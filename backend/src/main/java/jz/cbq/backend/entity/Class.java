package jz.cbq.backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 班级 实体类
 *
 * @author caobaoqi
 */
@Data
@ApiModel(value = "班级")
public class Class implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 班级 id
     */
    @TableId(value = "class_id")
    private String classId;
    /**
     * 年级
     */
    private String classYear;
    /**
     * 专业 id
     */
    private String majorId;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 老师名称
     */
    private String teaName;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 学生数量
     */
    private Integer stuTotal;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否删除
     */
    private Integer deleted;

}
