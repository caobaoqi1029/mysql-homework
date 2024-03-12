package jz.cbq.backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 学生 实体类
 *
 * @author caobaoqi
 */
@Data
@ApiModel(value = "学生")
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 学生 id
     */
    @TableId(value = "stu_id")
    private String stuId;
    /**
     * 老师 id
     */
    private String teaId;
    /**
     * 专业 id
     */
    private String majorId;
    /**
     * 班级 id
     */
    private String classId;
    /**
     * 学生名称
     */
    private String stuName;
    /**
     * 年级
     */
    private String admissionYear;
    /**
     * 班级名称
     */
    private String className;
    /**
     * 学生 IDCard
     */
    private String stuIdCard;
    /**
     * 学生密码
     */
    private String stuPwd;
    /**
     * 班级编号
     */
    private String classNo;
    /**
     * 专业名称
     */
    private String majorName;
    /**
     * 老师名称
     */
    private String teaName;
    /**
     * 信息数量
     */
    private String messageNum;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否删除
     */
    private Integer deleted;
}
