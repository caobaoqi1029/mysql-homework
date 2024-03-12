package jz.cbq.backend.mapper;

import jz.cbq.backend.entity.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * AdminMapper
 *
 * @author caobaoqi
 */
public interface AdminMapper extends BaseMapper<Admin> {
    @Select("SELECT admin_id FROM t_admin ORDER BY admin_id DESC LIMIT 1")
    String getMaxId();
}
