package dao;

import modelo.Contato;
import util.ConexaoBanco;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operações CRUD de Contatos
 */
public class ContatoDAO {
    
    /**
     * Salva um novo contato no banco
     */
    public boolean salvar(Contato contato) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConexaoBanco.getConexao();
            String sql = "INSERT INTO contatos (nome, telefone, email, status) VALUES (?, ?, ?, ?)";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getTelefone());
            stmt.setString(3, contato.getEmail());
            stmt.setString(4, contato.getStatus());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            ConexaoBanco.fecharStatement(stmt);
            ConexaoBanco.fecharConexao(conn);
        }
    }
    
    /**
     * Atualiza um contato existente
     */
    public boolean atualizar(Contato contato) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConexaoBanco.getConexao();
            String sql = "UPDATE contatos SET nome=?, telefone=?, email=?, status=? WHERE id=?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getTelefone());
            stmt.setString(3, contato.getEmail());
            stmt.setString(4, contato.getStatus());
            stmt.setInt(5, contato.getId());
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            ConexaoBanco.fecharStatement(stmt);
            ConexaoBanco.fecharConexao(conn);
        }
    }
    
    /**
     * Exclui um contato pelo ID
     */
    public boolean excluir(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        
        try {
            conn = ConexaoBanco.getConexao();
            String sql = "DELETE FROM contatos WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            ConexaoBanco.fecharStatement(stmt);
            ConexaoBanco.fecharConexao(conn);
        }
    }
    
    /**
     * Busca um contato por ID
     */
    public Contato buscarPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexaoBanco.getConexao();
            String sql = "SELECT * FROM contatos WHERE id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return extrairContato(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBanco.fecharResultSet(rs);
            ConexaoBanco.fecharStatement(stmt);
            ConexaoBanco.fecharConexao(conn);
        }
        return null;
    }
    
    /**
     * Lista todos os contatos
     */
    public List<Contato> listarTodos() {
        List<Contato> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexaoBanco.getConexao();
            String sql = "SELECT * FROM contatos ORDER BY id DESC";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                lista.add(extrairContato(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBanco.fecharResultSet(rs);
            ConexaoBanco.fecharStatement(stmt);
            ConexaoBanco.fecharConexao(conn);
        }
        return lista;
    }
    
    /**
     * Pesquisa contatos por nome ou email
     */
    public List<Contato> pesquisar(String termoPesquisa) {
        List<Contato> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexaoBanco.getConexao();
            String sql = "SELECT * FROM contatos WHERE nome LIKE ? OR email LIKE ? ORDER BY nome";
            stmt = conn.prepareStatement(sql);
            String termo = "%" + termoPesquisa + "%";
            stmt.setString(1, termo);
            stmt.setString(2, termo);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                lista.add(extrairContato(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBanco.fecharResultSet(rs);
            ConexaoBanco.fecharStatement(stmt);
            ConexaoBanco.fecharConexao(conn);
        }
        return lista;
    }
    
    /**
     * Conta total de contatos
     */
    public int contarTotal() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexaoBanco.getConexao();
            String sql = "SELECT COUNT(*) as total FROM contatos";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBanco.fecharResultSet(rs);
            ConexaoBanco.fecharStatement(stmt);
            ConexaoBanco.fecharConexao(conn);
        }
        return 0;
    }
    
    /**
     * Conta contatos ativos
     */
    public int contarAtivos() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexaoBanco.getConexao();
            String sql = "SELECT COUNT(*) as total FROM contatos WHERE status = 'Ativo'";
            stmt = conn.prepareStatement(sql);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBanco.fecharResultSet(rs);
            ConexaoBanco.fecharStatement(stmt);
            ConexaoBanco.fecharConexao(conn);
        }
        return 0;
    }
    
    /**
     * Extrai objeto Contato do ResultSet
     */
    private Contato extrairContato(ResultSet rs) throws SQLException {
        Contato contato = new Contato();
        contato.setId(rs.getInt("id"));
        contato.setNome(rs.getString("nome"));
        contato.setTelefone(rs.getString("telefone"));
        contato.setEmail(rs.getString("email"));
        contato.setStatus(rs.getString("status"));
        return contato;
    }
    
    /**
     * Lista contatos com paginação
     */
    public List<Contato> listarComPaginacao(int limit, int offset) {
        List<Contato> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexaoBanco.getConexao();
            String sql = "SELECT * FROM contatos ORDER BY id LIMIT ? OFFSET ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                lista.add(extrairContato(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBanco.fecharResultSet(rs);
            ConexaoBanco.fecharStatement(stmt);
            ConexaoBanco.fecharConexao(conn);
        }
        return lista;
    }
    
    /**
     * Pesquisa contatos com paginação
     */
    public List<Contato> pesquisarComPaginacao(String termoPesquisa, int limit, int offset) {
        List<Contato> lista = new ArrayList<>();
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexaoBanco.getConexao();
            String sql = "SELECT * FROM contatos WHERE nome LIKE ? OR email LIKE ? ORDER BY nome LIMIT ? OFFSET ?";
            stmt = conn.prepareStatement(sql);
            String termo = "%" + termoPesquisa + "%";
            stmt.setString(1, termo);
            stmt.setString(2, termo);
            stmt.setInt(3, limit);
            stmt.setInt(4, offset);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                lista.add(extrairContato(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBanco.fecharResultSet(rs);
            ConexaoBanco.fecharStatement(stmt);
            ConexaoBanco.fecharConexao(conn);
        }
        return lista;
    }
    
    /**
     * Conta total de resultados da pesquisa
     */
    public int contarPesquisa(String termoPesquisa) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = ConexaoBanco.getConexao();
            String sql = "SELECT COUNT(*) as total FROM contatos WHERE nome LIKE ? OR email LIKE ?";
            stmt = conn.prepareStatement(sql);
            String termo = "%" + termoPesquisa + "%";
            stmt.setString(1, termo);
            stmt.setString(2, termo);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            ConexaoBanco.fecharResultSet(rs);
            ConexaoBanco.fecharStatement(stmt);
            ConexaoBanco.fecharConexao(conn);
        }
        return 0;
    }
}
