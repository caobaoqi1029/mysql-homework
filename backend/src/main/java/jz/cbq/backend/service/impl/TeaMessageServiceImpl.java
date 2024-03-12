package jz.cbq.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jz.cbq.backend.entity.TeaMessage;
import jz.cbq.backend.entity.TeaMsgMap;
import jz.cbq.backend.mapper.TeaMessageMapper;
import jz.cbq.backend.service.ITeaMessageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * TeaMessageServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class TeaMessageServiceImpl extends ServiceImpl<TeaMessageMapper, TeaMessage> implements ITeaMessageService {
    @Resource
    private TeaMessageMapper teaMessageMapper;

    @Override
    public List<TeaMsgMap> getAllTeaMsg(String teaId, String stuName) {
        return teaMessageMapper.getAllTeaMsg(teaId, stuName);
    }
}
