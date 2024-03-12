package jz.cbq.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jz.cbq.backend.entity.TeaMessage;
import jz.cbq.backend.entity.TeaMsgMap;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * TeaMessageMapper
 *
 * @author caobaoqi
 */
public interface TeaMessageMapper extends BaseMapper<TeaMessage> {
    List<TeaMsgMap> getAllTeaMsg(String teaId,String stuName);
}
