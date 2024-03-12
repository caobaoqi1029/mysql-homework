package jz.cbq.backend.service.impl;

import jz.cbq.backend.entity.Major;
import jz.cbq.backend.mapper.MajorMapper;
import jz.cbq.backend.service.IMajorService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * MajorServiceImpl
 *
 * @author caobaoqi
 */
@Service
public class MajorServiceImpl extends ServiceImpl<MajorMapper, Major> implements IMajorService {
    @Resource
    private MajorMapper majorMapper;

    @Override
    public String findNextMajorId() {
        String maxMajorId = majorMapper.findMaxMajorId();
        if (null != maxMajorId) {
            return String.format("%02d", Integer.valueOf(maxMajorId) + 1);
        }
        return "01";
    }
}
