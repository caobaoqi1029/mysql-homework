package jz.cbq.backend.service.impl;

import jz.cbq.backend.entity.StuMessage;
import jz.cbq.backend.mapper.StuMessageMapper;
import jz.cbq.backend.service.IStuMessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * StuMessageServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class StuMessageServiceImpl extends ServiceImpl<StuMessageMapper, StuMessage> implements IStuMessageService {

}
