package jz.cbq.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jz.cbq.backend.entity.ChooseCourse;

/**
 * IChooseCourseService
 *
 * @author caobaoqi
 */
public interface IChooseCourseService extends IService<ChooseCourse> {

    /**
     * 更新学生名称
     * @param stuId 学生 id
     * @param stuName 学生名称
     */
    void updateStuName(String stuId,String stuName);

    /**
     * 取消选择课程
     * @return count
     */
    int cancelChooseCourse();





}
