package jz.cbq.backend.mapper;

import jz.cbq.backend.entity.Major;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * MajorMapper
 *
 * @author caobaoqi
 */
public interface MajorMapper extends BaseMapper<Major> {
    @Select("select major_id from t_major order by major_id desc limit 1")
    String findMaxMajorId();
}
