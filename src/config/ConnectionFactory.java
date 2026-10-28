package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    private static final String URL = lerVariavel("DB_URL","jdbc:mysql://localhost:3306/loja");

    private static final String USUARIO =lerVariavel("DB_USER","root");

    private static final String SENHA = lerVariavel("DB_PASSWORD","root");

    private static String lerVariavel(String nome, String valorPadrao){
        String valor = System.getenv(nome);
        return valor == null || valor.isBlank() ? valorPadrao :valor;
    }

    public static Connection abrirConexao() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
