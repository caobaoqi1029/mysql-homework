package jz.cbq.bookdemobackend.mapper;

import java.util.List;

/**
 * BaseMapper
 *
 * @author cbq
 * @date 2023/12/15 12:48
 * @since 1.0.0
 */
public interface BaseMapper <T>{
    T findById(Integer id);
    List<T> selectAll();
    Integer save(T entity);
    Integer updateById(T entity);
    Integer deleteById(Integer id);
}
