package jz.cbq.backend.mapper;

import jz.cbq.backend.entity.Score;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jz.cbq.backend.entity.StuScoreMap;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * ScoreMapper
 *
 * @author caobaoqi
 */
public interface ScoreMapper extends BaseMapper<Score> {

    @Select("SELECT score_id FROM t_score ORDER BY score_id DESC LIMIT 1")
    String getMaxId();

    List<StuScoreMap> getStuScoreList(String admissionYearMajor, String stuName, String stuId, String className, int limit1, int limit2);

    int getStuScoreTotal(String admissionYearMajor, String stuName, String stuId, String className, int limit1, int limit2);


    @Update("update t_score set stu_name=#{stuName} where stu_id=#{stuId}")
    void updateStuName(String stuId, String stuName);

    @Delete("delete from t_score")
    int cancelAllScore();
}
