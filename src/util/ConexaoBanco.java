package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 * Classe utilitária para conexão com MySQL (XAMPP)
 */
public class ConexaoBanco {
    
    // Configurações do banco de dados
    private static final String URL = "jdbc:mysql://localhost:3306/crud_contatos?useTimezone=true&serverTimezone=UTC";
    private static final String USUARIO = "root";
    private static final String SENHA = "";
    
    /**
     * Obtém conexão com o banco de dados
     */
    public static Connection getConexao() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, 
                "Driver MySQL não encontrado!\nVerifique se o mysql-connector-java.jar está nas bibliotecas.", 
                "Erro de Driver", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, 
                "Erro ao conectar ao banco!\n" +
                "Verifique se o XAMPP está rodando e se o banco 'crud_contatos' existe.\n\n" +
                "Erro: " + e.getMessage(), 
                "Erro de Conexão", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Fecha conexão
     */
    public static void fecharConexao(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Fecha PreparedStatement
     */
    public static void fecharStatement(PreparedStatement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Fecha ResultSet
     */
    public static void fecharResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Testa conexão com o banco
     */
    public static boolean testarConexao() {
        Connection conn = getConexao();
        if (conn != null) {
            fecharConexao(conn);
            return true;
        }
        return false;
    }
}
