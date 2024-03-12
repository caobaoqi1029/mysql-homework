package jz.cbq.backend.service;

import jz.cbq.backend.entity.Student;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * ITeaMessageService
 *
 * @author caobaoqi
 */
public interface IStudentService extends IService<Student> {
    /**
     * 获取下一个学生 id
     * @param stuIdPrefix 学生 id 前缀
     * @return id
     */
    String findNextStuId(String stuIdPrefix);

    List<Student> studentList42(int nowYear,String majorId);

    List<Student> studentList32(int nowYear,String majorId);

    List<Student> studentList22(int nowYear,String majorId);

    List<Student> studentList12(int nowYear,String majorId);

    List<Student> studentList41(int nowYear, String majorId);
    List<Student> studentList31(int nowYear, String majorId);
    List<Student> studentList21(int nowYear, String majorId);
    List<Student> studentList11(int nowYear, String majorId);

    List<Student> studentList51(int nowYear, String majorId);

    /**
     * 消息
     * @param stuId 学生 id
     */
    void plusMsgNum(String stuId);
    /**
     * 删除消息
     * @param stuId 学生 id
     */
    void delMsgNum(String stuId);
}
