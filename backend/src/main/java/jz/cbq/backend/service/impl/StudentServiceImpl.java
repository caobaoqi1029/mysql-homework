package jz.cbq.backend.service.impl;

import jz.cbq.backend.entity.Student;
import jz.cbq.backend.mapper.StudentMapper;
import jz.cbq.backend.service.IStudentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * StudentServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements IStudentService {
    @Resource
    private StudentMapper studentMapper;

    @Override
    public String findNextStuId(String stuIdPrefix) {
        int nextNum;
        String maxStuId = studentMapper.findMaxStuId(stuIdPrefix);
        if (null == maxStuId) {
            return stuIdPrefix + "01";
        }
        nextNum = Integer.parseInt(maxStuId.substring(7, 9)) + 1;
        return stuIdPrefix + String.format("%02d", Integer.valueOf(nextNum));
    }


    @Override
    public List<Student> studentList42(int nowYear, String majorId) {
        return studentMapper.studentList42(nowYear, majorId);
    }

    @Override
    public List<Student> studentList32(int nowYear, String majorId) {
        return studentMapper.studentList32(nowYear, majorId);
    }

    @Override
    public List<Student> studentList22(int nowYear, String majorId) {
        return studentMapper.studentList22(nowYear, majorId);
    }

    @Override
    public List<Student> studentList12(int nowYear, String majorId) {
        return studentMapper.studentList12(nowYear, majorId);
    }

    @Override
    public List<Student> studentList41(int nowYear, String majorId) {
        return studentMapper.studentList41(nowYear, majorId);
    }

    @Override
    public List<Student> studentList31(int nowYear, String majorId) {
        return studentMapper.studentList31(nowYear, majorId);
    }

    @Override
    public List<Student> studentList21(int nowYear, String majorId) {
        return studentMapper.studentList21(nowYear, majorId);
    }

    @Override
    public List<Student> studentList11(int nowYear, String majorId) {
        return studentMapper.studentList11(nowYear, majorId);
    }

    @Override
    public List<Student> studentList51(int nowYear, String majorId) {
        return studentMapper.studentList51(nowYear, majorId);
    }

    @Async("taskExecutor")
    @Override
    public void plusMsgNum(String stuId) {
        System.out.println("线程--" + Thread.currentThread().getName() + " 执行异步任务：更新学生表中学生" + stuId + "的消息数+1");
        studentMapper.plusMsgNum(stuId);
    }

    @Async("taskExecutor")
    @Override
    public void delMsgNum(String stuId) {
        System.out.println("线程--" + Thread.currentThread().getName() + " 执行异步任务：更新学生表中学生" + stuId + "的消息数-1");
        studentMapper.delMsgNum(stuId);
    }
}
