package jz.cbq.backend.mapper;

import jz.cbq.backend.entity.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * AdminMapper
 *
 * @author caobaoqi
 */
public interface StudentMapper extends BaseMapper<Student> {

    @Select("select stu_id from t_student where stu_id like concat(#{stuIdPrefix},'%') order by stu_id desc limit 1")
    String findMaxStuId(String stuIdPrefix);

    @Delete("delete from t_student where stu_id = #{stu_id}")
    int delStuById(String stuId);

    @Select("select * from t_student where (#{nowYear}-convert(admission_year,UNSIGNED))>=4 and major_id=#{majorId}")
    List<Student> studentList42(int nowYear, String majorId);

    @Select("select * from t_student where (#{nowYear}-convert(admission_year,UNSIGNED))=3 and major_id=#{majorId}")
    List<Student> studentList32(int nowYear, String majorId);

    @Select("select * from t_student where (#{nowYear}-convert(admission_year,UNSIGNED))=2 and major_id=#{majorId}")
    List<Student> studentList22(int nowYear, String majorId);

    @Select("select * from t_student where (#{nowYear}-convert(admission_year,UNSIGNED))=1 and major_id=#{majorId}")
    List<Student> studentList12(int nowYear, String majorId);

    @Select("select * from t_student where (#{nowYear}-convert(admission_year,UNSIGNED))>=3 and major_id=#{majorId}")
    List<Student> studentList41(int nowYear, String majorId);

    @Select("select * from t_student where (#{nowYear}-convert(admission_year,UNSIGNED))=2 and major_id=#{majorId}")
    List<Student> studentList31(int nowYear, String majorId);

    @Select("select * from t_student where (#{nowYear}-convert(admission_year,UNSIGNED))=1 and major_id=#{majorId}")
    List<Student> studentList21(int nowYear, String majorId);

    @Select("select * from t_student where (#{nowYear}-convert(admission_year,UNSIGNED))=0 and major_id=#{majorId}")
    List<Student> studentList11(int nowYear, String majorId);

    @Select("select * from t_student where (#{nowYear}-convert(admission_year,UNSIGNED))>=4 and major_id=#{majorId}")
    List<Student> studentList51(int nowYear, String majorId);
    @Update("update t_student set message_num=message_num+1 where stu_id=#{stuId}")
    void plusMsgNum(String stuId);
    @Update("update t_student set message_num=message_num-1 where stu_id=#{stuId}")
    void delMsgNum(String stuId);
}
