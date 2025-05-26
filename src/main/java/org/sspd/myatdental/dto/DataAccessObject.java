package org.sspd.myatdental.dto;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface DataAccessObject<T> {

    List<T> findAll();
    boolean save(T t);
    boolean delete(T t);
    boolean update(T t);
    T findById(int id);
    boolean deleteById(int id);
    boolean updateById(T t);


}
