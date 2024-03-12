package jz.cbq.bookdemobackend.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
* 班级表
*/
@Data
public class Class implements Serializable {

    /**
    * 班级 ID
    */
    @NotNull(message="[班级 ID]不能为空")
    @ApiModelProperty("班级 ID")
    private Integer id;
    /**
    * 班级名称
    */
    @NotBlank(message="[班级名称]不能为空")
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("班级名称")
    @Length(max= 255,message="编码长度不能超过255")
    private String name;
    /**
    * 所属专业 ID
    */
    @NotNull(message="[所属专业 ID]不能为空")
    @ApiModelProperty("所属专业 ID")
    private Integer majorId;


}
