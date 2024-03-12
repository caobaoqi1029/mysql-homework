package jz.cbq.backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 管理员 实体类
 *
 * @author caobaoqi
 */
@Data
@ApiModel(value = "管理员")
public class Admin implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 管理员 id
     */
    @TableId(value = "admin_id")
    private String adminId;
    /**
     * 管理员名称
     */
    private String adminName;
    /**
     * 管理员密码
     */
    private String adminPwd;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 是否删除
     */
    private Integer deleted;
}
