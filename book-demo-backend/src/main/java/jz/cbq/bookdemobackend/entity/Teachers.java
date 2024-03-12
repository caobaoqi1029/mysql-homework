package jz.cbq.bookdemobackend.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
* 老师表
*/
@Data
public class Teachers implements Serializable {

    /**
    * 教师 ID
    */
    @NotNull(message="[教师 ID]不能为空")
    @ApiModelProperty("教师 ID")
    private Integer id;
    /**
    * 教师名称
    */
    @NotBlank(message="[教师名称]不能为空")
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("教师名称")
    @Length(max= 255,message="编码长度不能超过255")
    private String name;
    /**
    * 教师职称
    */
    @NotBlank(message="[教师职称]不能为空")
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("教师职称")
    @Length(max= 255,message="编码长度不能超过255")
    private String title;



}
