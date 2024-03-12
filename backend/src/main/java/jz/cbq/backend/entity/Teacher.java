package jz.cbq.backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 老师 实体类
 *
 * @author caobaoqi
 */
@Data
@ApiModel(value = "老师")
public class Teacher implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 老师 id
     */
    @TableId(value = "tea_id")
    private String teaId;
    /**
     * 专业 id
     */
    private String majorId;
    /**
     * 年级
     */
    private String classYear;
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
     * 老师密码
     */
    private String teaPwd;
    /**
     * 班级 id
     */
    private String classId;
    /**
     * 老师 IDCard
     */
    private String teaIdCard;
    /**
     * 班级编号
     */
    private String classNo;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否删除
     */
    private Integer deleted;
}
