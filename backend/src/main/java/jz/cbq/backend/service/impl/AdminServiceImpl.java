package jz.cbq.backend.service.impl;

import jz.cbq.backend.entity.*;
import jz.cbq.backend.mapper.AdminMapper;
import jz.cbq.backend.service.*;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;

/**
 * AdminServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Resource
    private AdminMapper adminMapper;

    /**
     * 获取下一个 id
     * @return id
     */
    @Override
    public String getNextId() {
        String nowYear = Calendar.getInstance().get(Calendar.YEAR) + "";
        String maxId = getMaxId();
        int nextNum;
        if (maxId != null) {
            String maxYear = maxId.substring(3, 7);
            if (nowYear.equals(maxYear)) {
                nextNum = Integer.parseInt(maxId.substring(7, 9)) + 1;
                return nowYear + String.format("%02d", nextNum);
            } else {
                return nowYear + "01";
            }
        }
        return nowYear + "01";
    }

    /**
     * 获取 MaxID
     * @return id
     */
    public String getMaxId() {
        return adminMapper.getMaxId();
    }
}
