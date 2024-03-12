package jz.cbq.backend.service;

import jz.cbq.backend.entity.Score;
import com.baomidou.mybatisplus.extension.service.IService;
import jz.cbq.backend.entity.StuScoreMap;

import java.util.List;

/**
 * IScoreService
 *
 * @author caobaoqi
 */
public interface IScoreService extends IService<Score> {
    /**
     * 获取学生成绩列表
     * @param admissionYearMajor 年级
     * @param stuName 学生名称
     * @param stuId 学生 id
     * @param className 班级名称
     * @param limit1 limit1
     * @param limit2 limit2
     * @return List<StuScoreMap>
     */
    List<StuScoreMap> getStuScoreList(String admissionYearMajor,String stuName,String stuId,String className,int limit1,int limit2);
    /**
     * 获取学生成绩数量
     * @param admissionYearMajor 年级
     * @param stuName 学生名称
     * @param stuId 学生 id
     * @param className 班级名称
     * @param limit1 limit1
     * @param limit2 limit2
     * @return count
     */
    int getStuScoreTotal(String admissionYearMajor, String stuName, String stuId, String className, int limit1, int limit2);

    /**
     * 更新学生名称
     * @param stuId 学生 id
     * @param stuName 学生名称
     */
    void updateStuName(String stuId, String stuName);

    /**
     * 取消所有成绩
     * @return count
     */
    int cancelAllScore();

    String getMax();
}
