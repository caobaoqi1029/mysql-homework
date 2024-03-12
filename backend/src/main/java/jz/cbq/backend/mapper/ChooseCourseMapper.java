package jz.cbq.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jz.cbq.backend.entity.ChooseCourse;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Update;

/**
 * ChooseCourseMapper
 *
 * @author caobaoqi
 */
public interface ChooseCourseMapper extends BaseMapper<ChooseCourse> {
    @Update("UPDATE t_choose_course SET stu_name=#{stuName} WHERE stu_id=#{stuId}")
    void updateStuName(String stuId, String stuName);

    @Delete("DELETE from t_choose_course")
    int cancelChooseCourse();
}


