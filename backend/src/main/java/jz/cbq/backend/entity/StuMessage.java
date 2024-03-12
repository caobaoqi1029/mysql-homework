package jz.cbq.backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 学生信息 实体类
 *
 * @author caobaoqi
 */
@Data
@ApiModel(value = "学生信息")
public class StuMessage implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 信息 id
     */
    @TableId(value = "msg_id")
    private String msgId;
    /**
     * 学生 id
     */
    private String stuId;
    /**
     * 老师 id
     */
    private String teaId;
    /**
     * 信息内容
     */
    private String msgContent;
    /**
     * 消息时间
     */
    private Date msgTime;
    /**
     * 消息状态(已读|未读)
     */
    private String msgState;
    /**
     * 是否删除
     */
    private Integer deleted;

}