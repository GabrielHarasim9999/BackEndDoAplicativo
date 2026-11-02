package dao;

import config.ConnectionFactory;
import model.Medico;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MedicoDao implements CrudDao<Medico, Long> {

    @Override
    public Medico criar(Medico medico) throws SQLException {
        String sql = "INSERT INTO medico (nome, email, crm, especialidade) VALUES (?, ?, ?, ?)";
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            comando.setString(1, medico.getNome());
            comando.setString(2, medico.getEmail());
            comando.setString(3, medico.getCrm());
            comando.setString(4, medico.getEspecialidade());
            comando.executeUpdate();

            try (ResultSet chavesGeradas = comando.getGeneratedKeys()) {
                if (chavesGeradas.next()) {
                    medico.setId(chavesGeradas.getLong(1));
                }
            }
        }
        return medico;
    }

    @Override
    public Optional<Medico> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, nome, email, crm, especialidade FROM medico WHERE id = ?";
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setLong(1, id);
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearMedico(resultado));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Medico> listarTodos() throws SQLException {
        String sql = "SELECT id, nome, email, crm, especialidade FROM medico";
        List<Medico> medicos = new ArrayList<>();
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                medicos.add(mapearMedico(resultado));
            }
        }
        return medicos;
    }

    @Override
    public boolean atualizar(Medico medico) throws SQLException {
        String sql = "UPDATE medico SET nome = ?, email = ?, crm = ?, especialidade = ? WHERE id = ?";
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, medico.getNome());
            comando.setString(2, medico.getEmail());
            comando.setString(3, medico.getCrm());
            comando.setString(4, medico.getEspecialidade());
            comando.setLong(5, medico.getId());
            return comando.executeUpdate() > 0;
        }
    }

    @Override
    public boolean excluir(Long id) throws SQLException {
        String sql = "DELETE FROM medico WHERE id = ?";
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setLong(1, id);
            return comando.executeUpdate() > 0;
        }
    }

    private Medico mapearMedico(ResultSet resultado) throws SQLException {
        return new Medico(
                resultado.getLong("id"),
                resultado.getString("nome"),
                resultado.getString("email"),
                resultado.getString("crm"),
                resultado.getString("especialidade"));
    }
}
