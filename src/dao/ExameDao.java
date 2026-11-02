package dao;

import config.ConnectionFactory;
import model.Exame;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExameDao implements CrudDao<Exame, Long> {

    @Override
    public Exame criar(Exame exame) throws SQLException {
        String sql = "INSERT INTO exame (tipo, data_exame, resultado, paciente_id, medico_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            comando.setString(1, exame.getTipo());
            comando.setDate(2, Date.valueOf(exame.getDataExame()));
            comando.setString(3, exame.getResultado());
            comando.setLong(4, exame.getPacienteId());
            if (exame.getMedicoId() == null) {
                comando.setNull(5, java.sql.Types.BIGINT);
            } else {
                comando.setLong(5, exame.getMedicoId());
            }
            comando.executeUpdate();

            try (ResultSet chavesGeradas = comando.getGeneratedKeys()) {
                if (chavesGeradas.next()) {
                    exame.setId(chavesGeradas.getLong(1));
                }
            }
        }
        return exame;
    }

    @Override
    public Optional<Exame> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, tipo, data_exame, resultado, paciente_id, medico_id FROM exame WHERE id = ?";
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setLong(1, id);
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearExame(resultado));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Exame> listarTodos() throws SQLException {
        String sql = "SELECT id, tipo, data_exame, resultado, paciente_id, medico_id FROM exame";
        List<Exame> exames = new ArrayList<>();
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                exames.add(mapearExame(resultado));
            }
        }
        return exames;
    }

    @Override
    public boolean atualizar(Exame exame) throws SQLException {
        String sql = "UPDATE exame SET tipo = ?, data_exame = ?, resultado = ?, paciente_id = ?, medico_id = ? WHERE id = ?";
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, exame.getTipo());
            comando.setDate(2, Date.valueOf(exame.getDataExame()));
            comando.setString(3, exame.getResultado());
            comando.setLong(4, exame.getPacienteId());
            if (exame.getMedicoId() == null) {
                comando.setNull(5, java.sql.Types.BIGINT);
            } else {
                comando.setLong(5, exame.getMedicoId());
            }
            comando.setLong(6, exame.getId());
            return comando.executeUpdate() > 0;
        }
    }

    @Override
    public boolean excluir(Long id) throws SQLException {
        String sql = "DELETE FROM exame WHERE id = ?";
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setLong(1, id);
            return comando.executeUpdate() > 0;
        }
    }

    private Exame mapearExame(ResultSet resultado) throws SQLException {
        Date dataExame = resultado.getDate("data_exame");
        return new Exame(
                resultado.getLong("id"),
                resultado.getString("tipo"),
                dataExame == null ? null : dataExame.toLocalDate(),
                resultado.getString("resultado"),
                resultado.getLong("paciente_id"),
                resultado.getObject("medico_id") == null ? null : resultado.getLong("medico_id"));
    }
}
