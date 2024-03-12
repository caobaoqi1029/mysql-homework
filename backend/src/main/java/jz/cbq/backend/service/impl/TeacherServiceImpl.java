package jz.cbq.backend.service.impl;

import jz.cbq.backend.entity.Teacher;
import jz.cbq.backend.mapper.TeacherMapper;
import jz.cbq.backend.service.ITeacherService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * TeacherServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher> implements ITeacherService {

}
