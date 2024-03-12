package jz.cbq.bookdemobackend.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
* 专业表
*/
@Data
public class Major implements Serializable {

    /**
    * 专业 ID
    */
    @NotNull(message="[专业 ID]不能为空")
    @ApiModelProperty("专业 ID")
    private Integer id;
    /**
    * 专业名称
    */
    @NotBlank(message="[专业名称]不能为空")
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("专业名称")
    @Length(max= 255,message="编码长度不能超过255")
    private String name;



}
