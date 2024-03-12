package jz.cbq.bookdemobackend.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Student
 *
 * @author cbq
 * @date 2023/12/15 13:07
 * @since 1.0.0
 */
@Data
public class Student {
    /**
     * 学生 id
     */
    @NotNull(message="[学生 id]不能为空")
    @ApiModelProperty("学生 id")
    private Integer id;
    /**
     * 学生名称
     */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("学生名称")
    @Length(max= 255,message="编码长度不能超过255")
    private String name;
    /**
     * 籍贯
     */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("籍贯")
    @Length(max= 255,message="编码长度不能超过255")
    private String address;
    /**
     * 年龄
     */
    @NotNull(message="[年龄]不能为空")
    @ApiModelProperty("年龄")
    private Integer age;
    /**
     * 性别
     */
    @NotNull(message="[性别]不能为空")
    @ApiModelProperty("性别")
    private Object gender;
    /**
     * 联系电话
     */
    @Size(max= 11,message="编码长度不能超过11")
    @ApiModelProperty("联系电话")
    @Length(max= 11,message="编码长度不能超过11")
    private String phone;
    /**
     * 电子邮箱
     */
    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("电子邮箱")
    @Length(max= 255,message="编码长度不能超过255")
    private String email;
    /**
     * 入学年份
     */
    @ApiModelProperty("入学年份")
    private Object admissionYear;
    /**
     * 专业 ID
     */
    @NotNull(message="[专业 ID]不能为空")
    @ApiModelProperty("专业 ID")
    private Integer majorId;
    /**
     * 班级 ID
     */
    @NotNull(message="[班级 ID]不能为空")
    @ApiModelProperty("班级 ID")
    private Integer classId;
}
