package com.emre.utility;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IService<T, ID> {
    T save(T t);

    Iterable<T> saveAll(Iterable<T> t);

    T update(T t);

    void deleteById(ID id);

    Optional<T> findById(ID id);

    List<T> findAll();
}
