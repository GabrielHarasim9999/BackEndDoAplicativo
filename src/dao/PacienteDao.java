package dao;

import config.ConnectionFactory;
import model.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PacienteDao implements CrudDao<Paciente, Long> {

    @Override
    public Paciente criar(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO paciente (nome, CPF, email) VALUES (?, ?, ?)";
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            comando.setString(1, paciente.getNome());
            comando.setString(2, paciente.getCPF());
            comando.setString(3, paciente.getEmail());
            comando.executeUpdate();

            try (ResultSet chavesGeradas = comando.getGeneratedKeys()) {
                if (chavesGeradas.next()) {
                    paciente.setId(chavesGeradas.getLong(1));
                }
            }
        }
        return paciente;
    }

    @Override
    public Optional<Paciente> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, nome, CPF, email FROM paciente WHERE id = ?";
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setLong(1, id);
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearPaciente(resultado));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Paciente> listarTodos() throws SQLException {
        String sql = "SELECT id, nome, CPF, email FROM paciente";
        List<Paciente> pacientes = new ArrayList<>();
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                pacientes.add(mapearPaciente(resultado));
            }
        }
        return pacientes;
    }

    @Override
    public boolean atualizar(Paciente paciente) throws SQLException {
        String sql = "UPDATE paciente SET nome = ?, CPF = ?, email = ? WHERE id = ?";
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, paciente.getNome());
            comando.setString(2, paciente.getCPF());
            comando.setString(3, paciente.getEmail());
            comando.setLong(4, paciente.getId());
            return comando.executeUpdate() > 0;
        }
    }

    @Override
    public boolean excluir(Long id) throws SQLException {
        String sql = "DELETE FROM paciente WHERE id = ?";
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setLong(1, id);
            return comando.executeUpdate() > 0;
        }
    }

    private Paciente mapearPaciente(ResultSet resultado) throws SQLException {
        return new Paciente(
                resultado.getLong("id"),
                resultado.getString("nome"),
                resultado.getString("email"),
                resultado.getString("CPF"));
    }
}
