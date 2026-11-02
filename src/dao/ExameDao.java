package dao;

import config.ConnectionFactory;
import model.Exame;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExameDao implements CrudDao<Exame, Long> {

    @Override
    public Exame criar(Exame exame) throws SQLException {
        if (exame == null) {
            throw new IllegalArgumentException("Exame não pode ser nulo");
        }

        String sql = "INSERT INTO exame (tipo, data_exame, resultado, paciente_id, medico_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            comando.setString(1, exame.getTipo());
            setDataExame(comando, 2, exame.getDataExame());
            comando.setString(3, exame.getResultado());
            comando.setLong(4, exame.getPacienteId());
            setMedicoId(comando, 5, exame.getMedicoId());
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

    public List<Exame> listarPorPaciente(Long pacienteId) throws SQLException {
        return listarPorCampo("paciente_id", pacienteId);
    }

    public List<Exame> listarPorMedico(Long medicoId) throws SQLException {
        return listarPorCampo("medico_id", medicoId);
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
        if (exame == null) {
            throw new IllegalArgumentException("Exame não pode ser nulo");
        }

        String sql = "UPDATE exame SET tipo = ?, data_exame = ?, resultado = ?, paciente_id = ?, medico_id = ? WHERE id = ?";
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, exame.getTipo());
            setDataExame(comando, 2, exame.getDataExame());
            comando.setString(3, exame.getResultado());
            comando.setLong(4, exame.getPacienteId());
            setMedicoId(comando, 5, exame.getMedicoId());
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

    private List<Exame> listarPorCampo(String campo, Long valor) throws SQLException {
        String sql = "SELECT id, tipo, data_exame, resultado, paciente_id, medico_id FROM exame WHERE " + campo + " = ?";
        List<Exame> exames = new ArrayList<>();
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setLong(1, valor);
            try (ResultSet resultado = comando.executeQuery()) {
                while (resultado.next()) {
                    exames.add(mapearExame(resultado));
                }
            }
        }
        return exames;
    }

    private void setDataExame(PreparedStatement comando, int indice, LocalDate dataExame) throws SQLException {
        if (dataExame == null) {
            comando.setNull(indice, Types.DATE);
        } else {
            comando.setDate(indice, Date.valueOf(dataExame));
        }
    }

    private void setMedicoId(PreparedStatement comando, int indice, Long medicoId) throws SQLException {
        if (medicoId == null) {
            comando.setNull(indice, Types.BIGINT);
        } else {
            comando.setLong(indice, medicoId);
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
