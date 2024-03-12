package jz.cbq.backend.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jz.cbq.backend.entity.ChooseCourse;
import jz.cbq.backend.entity.Course;
import jz.cbq.backend.entity.Score;
import jz.cbq.backend.entity.StuScoreMap;
import jz.cbq.backend.service.IChooseCourseService;
import jz.cbq.backend.service.ICourseService;
import jz.cbq.backend.service.IScoreService;
import jz.cbq.backend.vo.Result;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * ScoreController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("/score")
public class ScoreController {
    @Resource
    private IScoreService scoreService;
    @Resource
    private IChooseCourseService chooseCourseService;
    @Resource
    private ICourseService courseService;

    /**
     * 通过学生 id 和课程名称获取成绩
     *
     * @param courseName courseName
     * @param stuId      stuId
     * @return 成绩
     */
    @GetMapping("getScoreByCourseName")
    public Result<Integer> getScoreByCourseName(String courseName, String stuId) {
        Score score = scoreService.getOne(new LambdaQueryWrapper<Score>().eq(Score::getCourseName, courseName).eq(Score::getStuId, stuId));

        return score != null ? Result.success(score.getScore()) : Result.success();
    }

    /**
     * 打分
     *
     * @param score score
     * @return INFO
     */
    @PutMapping("/giveNewScore")
    public Result<String> giveNewScore(@RequestBody Score score) {

        String stuId = score.getStuId();
        String courseName = score.getCourseName();
        ChooseCourse chooseCourseDatabase = chooseCourseService.getOne(new LambdaQueryWrapper<ChooseCourse>().eq(ChooseCourse::getCourseName, courseName).eq(ChooseCourse::getStuId, stuId));
        String chooseCourseId = chooseCourseDatabase.getChooseCourseId();
        Score scoreDatabase = scoreService.getOne(new LambdaQueryWrapper<Score>().eq(Score::getStuId, stuId).eq(Score::getChooseCourseId, chooseCourseId));

        if (scoreDatabase != null) {
            scoreService.update(score, new LambdaUpdateWrapper<Score>().eq(Score::getStuId, stuId).eq(Score::getChooseCourseId, chooseCourseId));
            return Result.success("更新分数成功");
        } else {
            score.setChooseCourseId(chooseCourseId);
            score.setScoreId(stuId + "-" + chooseCourseId);
            score.setCreateTime(new Date());
            scoreService.save(score);
            return Result.success("初次打分成功");
        }

    }

    /**
     * 条件分页获取学生成绩列表
     *
     * @param stuName            stuName
     * @param stuId              stuId
     * @param className          className
     * @param admissionYearMajor admissionYearMajor
     * @param pageNum            pageNum
     * @param pageSize           pageSize
     * @return HashMap<String, Object>
     */
    @GetMapping("/getStuScoreList")
    public Result<HashMap<String, Object>> getStuScoreList(@RequestParam(value = "stuName", required = false) String stuName,//201901
                                                           @RequestParam(value = "stuId", required = false) String stuId,
                                                           @RequestParam(value = "className", required = false) String className, String admissionYearMajor, int pageNum, int pageSize) {
        HashMap<String, Object> data = new HashMap<>();
        int nowYear = LocalDate.now().getYear() + 1900;
        int nowMonth = LocalDate.now().getMonthValue() + 1;
        String admissionYear = admissionYearMajor.substring(0, 4);
        String majorId = admissionYearMajor.substring(4, 6);
        List<Course> courseList = new ArrayList<>();
        if (nowMonth >= 3 && nowMonth < 9) {
            if (nowYear - Integer.parseInt(admissionYear) >= 4) {
                courseList = courseService.courseList42(majorId);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 3) {
                courseList = courseService.courseList32(majorId);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 2) {
                courseList = courseService.courseList22(majorId);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 1) {
                courseList = courseService.courseList12(majorId);
            }
        } else {
            if (nowYear - Integer.parseInt(admissionYear) >= 4) {
                courseList = courseService.courseList51(majorId);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 3) {
                courseList = courseService.courseList41(majorId);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 2) {
                courseList = courseService.courseList31(majorId);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 1) {
                courseList = courseService.courseList21(majorId);
            } else if (nowYear - Integer.parseInt(admissionYear) >= 0) {
                courseList = courseService.courseList11(majorId);
            }
        }
        List<StuScoreMap> stuScoreList = scoreService.getStuScoreList(admissionYearMajor, stuName, stuId, className, (pageNum - 1) * pageSize, pageSize);
        int total = scoreService.getStuScoreTotal(admissionYearMajor, stuName, stuId, className, (pageNum - 1) * pageSize, pageSize);
        for (StuScoreMap stuScoreMap : stuScoreList) {
            List<Integer> courseScoreList = new ArrayList<>();
            for (Course course : courseList) {
                Score scoreDatabase = scoreService.getOne(new LambdaQueryWrapper<Score>().eq(Score::getChooseCourseId, stuScoreMap.getStuId() + "choose" + course.getCourseId()));
                if (null == scoreDatabase) {
                    courseScoreList.add(0);
                } else {
                    courseScoreList.add(scoreDatabase.getScore());
                }
                stuScoreMap.setCourseScore(courseScoreList);
            }
        }
        data.put("stuScoreList", stuScoreList);
        data.put("total", total);
        return Result.success(data);
    }

    /**
     * 取消所有成绩
     *
     * @return INFO
     */
    @DeleteMapping("/cancelAllScore")
    public Result<String> cancelAllScore() {
        long start = System.currentTimeMillis();
        int cancelAllScore = scoreService.cancelAllScore();
        if (cancelAllScore >= 1) {
            long end = System.currentTimeMillis();
            return Result.success("一键分数置空完毕,服务器花费时间" + (end - start) + "ms");
        }
        return Result.fail("一键分数置空失败");
    }
}
