package jz.cbq.backend.service;

import jz.cbq.backend.entity.Major;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * IMajorService
 *
 * @author caobaoqi
 */
public interface IMajorService extends IService<Major> {

    /**
     * 获取下一个专业 id
     * @return id
     */
    String findNextMajorId();

}
