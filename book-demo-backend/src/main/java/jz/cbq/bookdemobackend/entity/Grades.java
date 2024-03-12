package jz.cbq.bookdemobackend.entity;

import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.math.BigDecimal;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* 成绩表
*/
@Data
public class Grades implements Serializable {

    /**
    * 成绩 ID
    */
    @NotNull(message="[成绩 ID]不能为空")
    @ApiModelProperty("成绩 ID")
    private Integer id;
    /**
    * 学生 ID
    */
    @NotNull(message="[学生 ID]不能为空")
    @ApiModelProperty("学生 ID")
    private Integer studentId;
    /**
    * 课程 ID
    */
    @NotNull(message="[课程 ID]不能为空")
    @ApiModelProperty("课程 ID")
    private Integer courseId;
    /**
    * 
    */
    @NotNull(message="[]不能为空")
    @ApiModelProperty("")
    private BigDecimal score;



}
