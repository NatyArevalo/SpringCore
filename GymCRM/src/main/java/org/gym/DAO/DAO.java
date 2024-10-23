package org.gym.DAO;

import java.util.List;
import java.util.Optional;

public interface DAO<T, Id> {

    List<T> getAll();

    void create(T t);
    void update(T t);

    void delete(T t);
}
