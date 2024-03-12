package jz.cbq.backend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jz.cbq.backend.entity.TeaMessage;
import jz.cbq.backend.entity.TeaMsgMap;
import jz.cbq.backend.service.ITeaMessageService;
import jz.cbq.backend.vo.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * TeaMessageController
 *
 * @author caobaoqi
 */
@RestController
@RequestMapping("teaMessage")
public class TeaMessageController {
    @Resource
    private ITeaMessageService teaMessageService;

    /**
     * 通过老师 id 和学生姓名 获取消息列表
     *
     * @param teaId   teaId
     * @param stuName stuName
     * @return List<TeaMsgMap>
     */
    @GetMapping("/getMsgsByTeaId")
    public Result<List<TeaMsgMap>> getMsgsByTeaId(String teaId, @RequestParam(value = "stuName", required = false) String stuName) {
        List<TeaMsgMap> allTeaMsg = teaMessageService.getAllTeaMsg(teaId, stuName);
        return Result.success(allTeaMsg);
    }

    /**
     * 添加消息
     *
     * @param teaMessage teaMessage
     * @return INFO
     */
    @PostMapping("/addMsg")
    public Result<String> addMsg(@RequestBody TeaMessage teaMessage) {
        teaMessage.setMsgId(teaMessage.getTeaId() + "msg" + teaMessage.getStuId() + UUID.randomUUID());
        teaMessage.setMsgTime(new Date());
        boolean save = teaMessageService.save(teaMessage);
        if (save) {
            return Result.success("发消息成功");
        }
        return Result.fail("发消息失败");
    }

    /**
     * 根据 id 删除
     *
     * @param msgId msgId
     * @return INFO
     */
    @DeleteMapping("/delById")
    public Result<String> delById(String msgId) {
        boolean remove = teaMessageService.remove(new LambdaQueryWrapper<TeaMessage>().eq(TeaMessage::getMsgId, msgId));

        return remove ? Result.success("删除消息成功") : Result.fail("删除消息失败");
    }
}
