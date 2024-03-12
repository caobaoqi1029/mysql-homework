package jz.cbq.bookdemobackend.service.impl;

import jz.cbq.bookdemobackend.entity.Student;
import jz.cbq.bookdemobackend.mapper.StudentMapper;
import jz.cbq.bookdemobackend.service.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * StudentServiceImpl
 *
 * @author cbq
 * @date 2023/12/15 13:08
 * @since 1.0.0
 */
@Service
public class StudentServiceImpl extends BaseServiceImpl<Student> implements StudentService {
    @Resource
    StudentMapper studentMapper;

    @Override
    public Student findById(Integer id) {
        return studentMapper.findById(id);
    }

    @Override
    public List<Student> selectAll() {
        return studentMapper.selectAll();
    }

    @Override
    public Integer save(Student entity) {
       return studentMapper.save(entity);
    }

    @Override
    public Integer updateById(Student entity) {
        return studentMapper.updateById(entity);
    }

    @Override
    public Integer deleteById(Integer id) {
       return studentMapper.deleteById(id);
    }
}
