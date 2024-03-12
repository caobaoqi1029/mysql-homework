package jz.cbq.bookdemobackend.mapper;

import jz.cbq.bookdemobackend.entity.Student;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * StudentMapper
 *
 * @author cbq
 * @date 2023/12/15 13:10
 * @since 1.0.0
 */
public interface StudentMapper extends BaseMapper<Student> {
    /**
     * 根据id查询学生信息
     *
     * @param id 学生id
     * @return 对应的学生信息
     */
    @Select("SELECT * FROM `mysql-demo`.tb_student WHERE id = #{id}")
    @Override
    Student findById(Integer id);

    /**
     * 查询所有学生信息
     *
     * @return 所有学生信息列表
     */
    @Select("SELECT * FROM `mysql-demo`.tb_student")
    @Override
    List<Student> selectAll();

    /**
     * 保存学生信息
     *
     * @param student 待保存的学生信息
     * @return 保存的id
     */
    @Insert("INSERT INTO `mysql-demo`.tb_student (name, address) VALUES (#{name}, #{address})")
    @Override
    Integer save(Student student);

    /**
     * 根据学生信息更新数据库中对应记录
     *
     * @param student 待更新的学生信息
     * @return 更新的记录数
     */
    @Update("UPDATE `mysql-demo`.tb_student SET name = #{name}, address = #{address} WHERE id = #{id}")
    @Override
    Integer updateById(Student student);

    /**
     * 根据id删除学生信息
     *
     * @param id 学生id
     * @return 删除的记录数
     */
    @Delete("DELETE FROM `mysql-demo`.tb_student WHERE id = #{id}")
    @Override
    Integer deleteById(Integer id);

}
