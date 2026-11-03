package dao;

import config.ConnectionFactory;
import model.Administrador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AdministradorDao implements CrudDao<Administrador, Long> {

    @Override
    public Administrador criar(Administrador administrador) throws SQLException {
        if (administrador == null) {
            throw new IllegalArgumentException("Administrador não pode ser nulo");
        }

        String sql = "INSERT INTO administrador (nome, email, login, senha) VALUES (?, ?, ?, ?)";
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            comando.setString(1, administrador.getNome());
            comando.setString(2, administrador.getEmail());
            comando.setString(3, administrador.getLogin());
            comando.setString(4, administrador.getSenha());
            comando.executeUpdate();

            try (ResultSet chavesGeradas = comando.getGeneratedKeys()) {
                if (chavesGeradas.next()) {
                    administrador.setId(chavesGeradas.getLong(1));
                }
            }
        }
        return administrador;
    }

    @Override
    public Optional<Administrador> buscarPorId(Long id) throws SQLException {
        String sql = "SELECT id, nome, email, login, senha FROM administrador WHERE id = ?";
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setLong(1, id);
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearAdministrador(resultado));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<Administrador> autenticar(String login, String senha) throws SQLException {
        String sql = "SELECT id, nome, email, login, senha FROM administrador WHERE login = ? AND senha = ?";
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, login);
            comando.setString(2, senha);
            try (ResultSet resultado = comando.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(mapearAdministrador(resultado));
                }
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Administrador> listarTodos() throws SQLException {
        String sql = "SELECT id, nome, email, login, senha FROM administrador";
        List<Administrador> administradores = new ArrayList<>();
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql);
             ResultSet resultado = comando.executeQuery()) {
            while (resultado.next()) {
                administradores.add(mapearAdministrador(resultado));
            }
        }
        return administradores;
    }

    @Override
    public boolean atualizar(Administrador administrador) throws SQLException {
        if (administrador == null) {
            throw new IllegalArgumentException("Administrador não pode ser nulo");
        }

        String sql = "UPDATE administrador SET nome = ?, email = ?, login = ?, senha = ? WHERE id = ?";
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setString(1, administrador.getNome());
            comando.setString(2, administrador.getEmail());
            comando.setString(3, administrador.getLogin());
            comando.setString(4, administrador.getSenha());
            comando.setLong(5, administrador.getId());
            return comando.executeUpdate() > 0;
        }
    }

    @Override
    public boolean excluir(Long id) throws SQLException {
        String sql = "DELETE FROM administrador WHERE id = ?";
        try (Connection conexao = ConnectionFactory.abrirConexao();
             PreparedStatement comando = conexao.prepareStatement(sql)) {
            comando.setLong(1, id);
            return comando.executeUpdate() > 0;
        }
    }

    private Administrador mapearAdministrador(ResultSet resultado) throws SQLException {
        return new Administrador(
                resultado.getLong("id"),
                resultado.getString("nome"),
                resultado.getString("email"),
                resultado.getString("login"),
                resultado.getString("senha"));
    }
}
