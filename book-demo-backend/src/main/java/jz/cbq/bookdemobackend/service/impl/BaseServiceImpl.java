package jz.cbq.bookdemobackend.service.impl;

import jz.cbq.bookdemobackend.service.BaseService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * BaseServiceImpl
 *
 * @author cbq
 * @date 2023/12/15 12:50
 * @since 1.0.0
 */
@Service
public class BaseServiceImpl<T> implements BaseService<T> {
    @Override
    public T findById(Integer id) {
        return null;
    }

    @Override
    public List<T> selectAll() {
        return null;
    }

    @Override
    public Integer save(T entity) {
        return 0;
    }

    @Override
    public Integer updateById(T entity) {
        return 0;
    }

    @Override
    public Integer deleteById(Integer id) {
        return 0;
    }
}
