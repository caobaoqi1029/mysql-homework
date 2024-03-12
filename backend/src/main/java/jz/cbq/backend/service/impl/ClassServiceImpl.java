package jz.cbq.backend.service.impl;

import jz.cbq.backend.entity.Class;
import jz.cbq.backend.mapper.ClassMapper;
import jz.cbq.backend.service.IClassService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * ClassServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class ClassServiceImpl extends ServiceImpl<ClassMapper, Class> implements IClassService {

}
