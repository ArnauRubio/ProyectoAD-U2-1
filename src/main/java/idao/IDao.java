package idao;

import java.util.List;

/** Operaciones comunes a todos los DAOs. */
public interface IDao<T, K> {

    /** @return la entidad o null si no existe */
    T find(K id);

    List<T> findAll();

    /** @throws IllegalStateException si ya existe una entidad con ese código */
    T create(T t);

    /** @throws IllegalStateException si no existe */
    T update(T t);

    /**
     * @throws IllegalStateException si no existe o tiene suministros asociados
     */
    void delete(T t);
}