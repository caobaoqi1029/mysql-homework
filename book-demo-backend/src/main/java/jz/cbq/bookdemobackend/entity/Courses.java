package jz.cbq.bookdemobackend.entity;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
* 课程表
*/
@Data
public class Courses implements Serializable {

    /**
    * 课程 ID
    */
    @NotNull(message="[课程 ID]不能为空")
    @ApiModelProperty("课程 ID")
    private Integer id;
    /**
    * 课程 名称
    */
    @NotBlank(message="[课程 名称]不能为空")
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("课程 名称")
    @Length(max= 255,message="编码长度不能超过255")
    private String name;
    /**
    * 教师 ID
    */
    @NotNull(message="[教师 ID]不能为空")
    @ApiModelProperty("教师 ID")
    private Integer teacherId;
    /**
    * 学分
    */
    @NotNull(message="[学分]不能为空")
    @ApiModelProperty("学分")
    private Integer credit;
    /**
    * 是否必修
    */
    @NotNull(message="[是否必修]不能为空")
    @ApiModelProperty("是否必修")
    private Integer isRequired;


}
