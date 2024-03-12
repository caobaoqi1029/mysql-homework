package jz.cbq.backend.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jz.cbq.backend.entity.ChooseCourse;
import jz.cbq.backend.entity.Score;
import jz.cbq.backend.service.IChooseCourseService;
import jz.cbq.backend.service.ICourseService;
import jz.cbq.backend.service.IScoreService;
import jz.cbq.backend.vo.Result;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * ChooseCourseController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("/chooseCourse")
public class ChooseCourseController {
    @Resource
    private IChooseCourseService chooseCourseService;
    @Resource
    private ICourseService courseService;
    @Resource
    private IScoreService scoreService;

    /**
     * 选课
     *
     * @param chooseCourse chooseCourse
     * @return INFO
     */
    @Transactional
    @PostMapping("/add")
    public Result<String> add(@RequestBody ChooseCourse chooseCourse) {
        String courseId = chooseCourse.getCourseId();
        String stuId = chooseCourse.getStuId();
        String chooseCourseId = stuId + "choose" + courseId;
        chooseCourse.setChooseCourseId(chooseCourseId);

        chooseCourse.setCreateTime(new Date());
        chooseCourse.setState(1);

        boolean save = chooseCourseService.save(chooseCourse);
        if (save) {
            courseService.updateStuChooseNum(courseId);
            return Result.success("选课成功");
        }
        return Result.fail("选课失败");
    }

    /**
     * 通过学生 id 获取可选课程列表
     *
     * @param stuId stuId
     * @return List<ChooseCourse>
     */
    @GetMapping("/getCanScoreCourse")
    public Result<List<ChooseCourse>> getCanScoreCourse(String stuId) {
        List<ChooseCourse> canScoreCourseList = chooseCourseService.list(new LambdaQueryWrapper<ChooseCourse>().eq(ChooseCourse::getStuId, stuId));
        return Result.success(canScoreCourseList);
    }

    /**
     * 取消所有选课
     *
     * @return INFO
     */
    @Transactional
    @DeleteMapping("/cancelChooseCourse")
    public Result<String> cancelChooseCourse() {

        if (!StringUtils.isEmpty(scoreService.getMax())) {
            return Result.fail("还有成绩数据无法进行一键退课,请先将分数置空");
        } else {
            long start = System.currentTimeMillis();
            int cancelChooseCourse = chooseCourseService.cancelChooseCourse();
            if (cancelChooseCourse >= 1) {
                courseService.updateChooseCourseNum();
                long end = System.currentTimeMillis();
                return Result.success("一键退课完毕,服务器花费时间" + (end - start) + "ms");
            }
            return Result.fail("一键退课失败");
        }
    }
}
