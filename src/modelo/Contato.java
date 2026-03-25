package modelo;

import java.time.LocalDateTime;

/**
 * Classe modelo para Contato
 */
public class Contato {
    private int id;
    private String nome;
    private String telefone;
    private String email;
    private String status;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;
    
    // Construtores
    public Contato() {
    }
    
    public Contato(String nome, String telefone, String email, String status) {
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.status = status;
    }
    
    // Getters e Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getTelefone() {
        return telefone;
    }
    
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }
    
    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }
    
    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
    
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
    
    public boolean isAtivo() {
        return "Ativo".equals(this.status);
    }
    
    @Override
    public String toString() {
        return nome + " - " + email;
    }
}
