package dao;

import config.ConnectionFactory;
import model.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PacienteDao  implements  CrudDao<Paciente,Long>{

    @Override
    public Paciente criar (Paciente paciente) throws SQLException{
        String sql ="INSERT INTO paciente (nome, CPF, email) VALUES(?,?,?)";
        try (Connection conexao = ConnectionFactory.abrirConexao()) {
            PreparedStatment comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
        }

    }

    @Override
    public Optional<Paciente> buscarPorId(Long aLong) throws SQLException {
        return Optional.empty();
    }

    @Override
    public List<Paciente> listarTodos() throws SQLException {
        return List.of();
    }

    @Override
    public boolean atualizar(Paciente entidade) throws SQLException {
        return false;
    }

    @Override
    public boolean excluir(Long aLong) throws SQLException {
        return false;
    }
}

