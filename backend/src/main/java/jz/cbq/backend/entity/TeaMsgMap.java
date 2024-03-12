package jz.cbq.backend.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class TeaMsgMap implements Serializable {
    private static final long serialVersionUID = 1L;
    @TableId(value = "msg_id")

    private String msgId;

    private String stuId;

    private String teaId;

    private String stuName;

    private String msgContent;

    private Date msgTime;
    /**
     * 是否删除
     */
    private Integer deleted;

}
