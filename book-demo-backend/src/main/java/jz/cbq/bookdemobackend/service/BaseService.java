package jz.cbq.bookdemobackend.service;

import java.util.List;

/**
 * BaseService
 *
 * @author cbq
 * @date 2023/12/15 12:49
 * @since 1.0.0
 */
public interface BaseService<T> {
    T findById(Integer id);
    List<T> selectAll();
    Integer save(T entity);
    Integer updateById(T entity);
    Integer deleteById(Integer id);
}
