package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface CrudDao <T, ID>{
    T criar(T entidade) throws SQLException;
    Optional<T> buscarPorId(ID id) throws SQLException;
    List<T> listarTodos() throws  SQLException;
    boolean atualizar(T entidade) throws  SQLException;
    boolean excluir(ID id) throws SQLException;
}
