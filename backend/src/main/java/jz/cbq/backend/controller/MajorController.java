package jz.cbq.backend.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jz.cbq.backend.vo.Result;
import jz.cbq.backend.entity.Major;
import jz.cbq.backend.service.IMajorService;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * MajorController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("/major")
public class MajorController {
    @Resource
    private IMajorService majorService;

    /**
     * 添加专业
     *
     * @param major major
     * @return INFO
     */
    @PostMapping("/add")
    public Result<String> addMajor(@RequestBody Major major) {
        Major one = majorService.getOne(new LambdaQueryWrapper<Major>().eq(Major::getMajorName, major.getMajorName()).last("LIMIT 1"));
        if (one != null) {
            return Result.fail("该专业已存在");
        }
        String nextMajorId = majorService.findNextMajorId();
        major.setMajorId(nextMajorId);
        major.setCreateTime(new Date());
        major.setClassTotal(0);
        major.setCourseTotal(0);
        major.setStuTotal(0);
        major.setTeaTotal(0);
        boolean save = majorService.save(major);

        return save ? Result.success("添加专业成功") : Result.fail("添加专业失败");
    }

    /**
     * 通过 id 获取专业
     *
     * @param majorId majorId
     * @return Major
     */
    @GetMapping("getByMajorId")
    public Result<Major> getByMajorId(String majorId) {
        Major major = majorService.getOne(new LambdaQueryWrapper<Major>().eq(Major::getMajorId, majorId).last("LIMIT 1"));

        return major != null ? Result.success(major) : Result.fail("获取专业失败");
    }

    /**
     * 编辑专业
     *
     * @param major major
     * @return INFO
     */
    @PutMapping("/edit")
    public Result<String> editMajor(@RequestBody Major major) {
        boolean update = majorService.update(new LambdaUpdateWrapper<Major>().set(Major::getMajorName, major.getMajorName()).eq(Major::getMajorId, major.getMajorId()));

        return update ? Result.success("编辑专业成功") : Result.fail("编辑专业失败");
    }

    /**
     * 条件分页查询专业信息
     *
     * @param pageNum   pageNum
     * @param pageSize  pageSize
     * @param majorName majorName
     * @return Map<String, Object>
     */
    @GetMapping("/majorPageList")
    public Result<Map<String, Object>> majorPageList(int pageNum, int pageSize, @RequestParam(value = "majorName", required = false) String majorName) {
        Map<String, Object> data = new HashMap<>();
        Page<Major> page = new Page<>(pageNum, pageSize);
        majorService.page(page, new LambdaQueryWrapper<Major>().like(StringUtils.hasLength(majorName), Major::getMajorName, majorName).orderByDesc(Major::getCreateTime));
        data.put("majorList", page.getRecords());
        data.put("total", page.getTotal());
        return Result.success(data);
    }

    /**
     * 根据 id 删除专业
     *
     * @param majorId majorId
     * @return INFO
     */
    @DeleteMapping("/delById")
    public Result<String> delById(String majorId) {
        boolean remove = majorService.remove(new LambdaQueryWrapper<Major>().eq(Major::getMajorId, majorId));

        return remove ? Result.success("删除专业成功") : Result.fail("删除专业失败");
    }

    /**
     * 获取所有专业名称
     *
     * @return List<Major>
     */
    @GetMapping("/getAllMajorName")
    public Result<List<Major>> getAllMajorName() {
        List<Major> majorList = majorService.list();
        return Result.success(majorList);
    }
}
