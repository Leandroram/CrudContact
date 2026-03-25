-- =====================================================
-- CRUD DE CONTATOS - BANCO DE DADOS
-- Execute este script no phpMyAdmin (XAMPP)
-- =====================================================

CREATE DATABASE IF NOT EXISTS crud_contatos 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE crud_contatos;

-- =====================================================
-- TABELA DE CONTATOS
-- =====================================================
CREATE TABLE IF NOT EXISTS contatos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    status ENUM('Ativo', 'Desativado') DEFAULT 'Ativo',
    data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =====================================================
-- DADOS DE EXEMPLO
-- =====================================================
INSERT INTO contatos (nome, telefone, email, status) VALUES 
('João Silva Santos', '(11) 98765-4321', 'joao.silva@email.com', 'Ativo'),
('Maria Oliveira Costa', '(21) 91234-5678', 'maria.oliveira@email.com', 'Ativo'),
('Pedro Souza Lima', '(31) 99876-5432', 'pedro.souza@email.com', 'Desativado'),
('Ana Paula Ferreira', '(41) 97654-3210', 'ana.ferreira@email.com', 'Ativo'),
('Carlos Eduardo Alves', '(51) 96543-2109', 'carlos.alves@email.com', 'Ativo'),
('Juliana Santos Rocha', '(61) 95432-1098', 'juliana.rocha@email.com', 'Desativado'),
('Fernando Costa Silva', '(71) 94321-0987', 'fernando.silva@email.com', 'Ativo'),
('Patrícia Lima Souza', '(81) 93210-9876', 'patricia.souza@email.com', 'Ativo'),
('Roberto Almeida Reis', '(85) 92109-8765', 'roberto.reis@email.com', 'Ativo'),
('Camila Rodrigues Dias', '(91) 91098-7654', 'camila.dias@email.com', 'Desativado');

-- =====================================================
-- ÍNDICES PARA MELHOR PERFORMANCE
-- =====================================================
CREATE INDEX idx_nome ON contatos(nome);
CREATE INDEX idx_email ON contatos(email);
CREATE INDEX idx_status ON contatos(status);

-- =====================================================
-- VERIFICAÇÃO
-- =====================================================
SELECT 'Banco de dados criado com sucesso!' as Status;
SELECT COUNT(*) as Total_Contatos FROM contatos;
SELECT status, COUNT(*) as Quantidade FROM contatos GROUP BY status;
