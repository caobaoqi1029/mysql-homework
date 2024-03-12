package jz.cbq.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import jz.cbq.backend.entity.TeaMessage;
import jz.cbq.backend.entity.TeaMsgMap;

import java.util.List;

/**
 * ITeaMessageService
 *
 * @author caobaoqi
 */
public interface ITeaMessageService extends IService<TeaMessage> {

    List<TeaMsgMap> getAllTeaMsg(String teaId, String stuName);

}
