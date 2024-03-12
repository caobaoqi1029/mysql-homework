package jz.cbq.backend.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jz.cbq.backend.vo.Result;
import jz.cbq.backend.entity.StuMessage;
import jz.cbq.backend.service.IStuMessageService;
import jz.cbq.backend.service.IStudentService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * StuMessageController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("/stuMessage")
public class StuMessageController {
    @Resource
    private IStuMessageService stuMessageService;
    @Resource
    private IStudentService studentService;

    /**
     * 添加消息
     * @param stuMessage stuMessage
     * @return INFO
     */
    @Transactional
    @PostMapping("/addMsg")
    public Result<String> addMsg(@RequestBody StuMessage stuMessage) {
        stuMessage.setMsgId(stuMessage.getTeaId() + "msg" + stuMessage.getStuId() + UUID.randomUUID());
        stuMessage.setMsgTime(new Date());
        boolean save = stuMessageService.save(stuMessage);
        if (save) {
            studentService.plusMsgNum(stuMessage.getStuId());
            return Result.success("发消息成功");
        }
        return Result.fail("发消息失败");
    }

    /**
     * 通过学生 id 获取消息列表
     * @param stuId stuId
     * @return List<StuMessage>
     */
    @GetMapping("/getMsgsByStuId")
    public Result<List<StuMessage>> getMsgsByStuId(String stuId) {
        List<StuMessage> msgList = stuMessageService.list(new LambdaQueryWrapper<StuMessage>().eq(StuMessage::getStuId, stuId).orderByDesc(StuMessage::getMsgTime));
        return Result.success(msgList);
    }

    /**
     * 通过 id 删除
     *
     * @param msgId msgId
     * @param stuId stuId
     * @return INFO
     */
    @DeleteMapping("/delById")
    public Result<String> delById(String msgId, String stuId) {
        boolean remove = stuMessageService.remove(new LambdaQueryWrapper<StuMessage>().eq(StuMessage::getMsgId, msgId));
        if (remove) {
            studentService.delMsgNum(stuId);
            return Result.success("删除消息成功");
        }
        return Result.fail("删除消息失败");
    }

    /**
     * 已读
     *
     * @param stuMessage stuMessage
     * @return INFO
     */
    @PutMapping("/hasRead")
    public Result<String> hasRead(@RequestBody StuMessage stuMessage) {
        stuMessageService.update(new LambdaUpdateWrapper<StuMessage>().set(StuMessage::getMsgState, "已读").eq(StuMessage::getStuId, stuMessage.getStuId()));
        return Result.success();
    }

    /**
     * 通过学生 id 获取未读消息数
     *
     * @param stuId stuId
     * @return 未读消息数
     */
    @GetMapping("/getStuUnreadNum")
    public Result<Long> getStuUnreadNum(String stuId) {
        long count = stuMessageService.count(new LambdaQueryWrapper<StuMessage>().eq(StuMessage::getMsgState, "未读").eq(StuMessage::getStuId, stuId));
        return Result.success(count);
    }
}
