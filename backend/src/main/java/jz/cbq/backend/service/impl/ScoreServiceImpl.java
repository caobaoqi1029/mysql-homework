package jz.cbq.backend.service.impl;

import jz.cbq.backend.entity.Score;
import jz.cbq.backend.entity.StuScoreMap;
import jz.cbq.backend.mapper.ScoreMapper;
import jz.cbq.backend.service.IScoreService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * ScoreServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class ScoreServiceImpl extends ServiceImpl<ScoreMapper, Score> implements IScoreService {
    @Resource
    private ScoreMapper scoreMapper;

    @Override
    public List<StuScoreMap> getStuScoreList(String admissionYearMajor, String stuName, String stuId, String className, int limit1, int limit2) {
        return scoreMapper.getStuScoreList(admissionYearMajor, stuName, stuId, className, limit1, limit2);
    }


    @Override
    public int getStuScoreTotal(String admissionYearMajor, String stuName, String stuId, String className, int limit1, int limit2) {
        return scoreMapper.getStuScoreTotal(admissionYearMajor, stuName, stuId, className, limit1, limit2);
    }

    @Async("taskExecutor")
    @Override
    public void updateStuName(String stuId, String stuName) {
        System.out.println("线程--" + Thread.currentThread().getName() + " 执行异步任务：更新分数表中学生：" + stuId + " 的姓名");
        scoreMapper.updateStuName(stuId, stuName);
    }

    @Override
    public int cancelAllScore() {
        return scoreMapper.cancelAllScore();
    }

    @Override
    public String getMax() {
        return scoreMapper.getMaxId();
    }
}
