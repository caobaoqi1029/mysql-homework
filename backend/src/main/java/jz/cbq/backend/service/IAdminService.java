package jz.cbq.backend.service;

import jz.cbq.backend.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * IAdminService
 *
 * @author caobaoqi
 */
public interface IAdminService extends IService<Admin> {

    /**
     * 获取下一个 ID
     * @return id
     */
    String getNextId();
}
