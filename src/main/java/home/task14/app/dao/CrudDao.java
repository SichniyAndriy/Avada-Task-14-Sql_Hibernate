package home.task14.app.dao;

import java.util.List;
import java.util.Optional;

public interface CrudDao<T> {
    long add(T t);

    Optional<T> getById(long id);

    List<T> getAll();

    boolean update(T t);

    boolean delete(T t);
}
