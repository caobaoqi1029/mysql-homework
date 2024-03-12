package jz.cbq.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jz.cbq.backend.entity.ChooseCourse;
import jz.cbq.backend.mapper.ChooseCourseMapper;
import jz.cbq.backend.service.IChooseCourseService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * ChooseCourseServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class ChooseCourseServiceImpl extends ServiceImpl<ChooseCourseMapper, ChooseCourse> implements IChooseCourseService {
    @Resource
    private ChooseCourseMapper chooseCourseMapper;

    @Async("taskExecutor")
    @Override
    public void updateStuName(String stuId, String stuName) {
        System.out.println("线程" + Thread.currentThread().getName() + " 执行异步任务：更新选课表中学生姓名");
        chooseCourseMapper.updateStuName(stuId, stuName);
    }

    @Override
    public int cancelChooseCourse() {
        return chooseCourseMapper.cancelChooseCourse();
    }



}
