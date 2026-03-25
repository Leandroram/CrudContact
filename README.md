Projeto feito em Java usando o NetBeans e Xammp.
Um sistema de CRUD simples, com paginação, Adicionar, Editar, Excluir e listar. Tem os campos de busca e cores de definem o status.

CREATE DATABASE IF NOT EXISTS crud_contatos 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE crud_contatos;

CREATE TABLE IF NOT EXISTS contatos (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(100) NOT NULL,
    telefone VARCHAR(20) NOT NULL,
    email VARCHAR(100) NOT NULL,
    status ENUM('Ativo', 'Desativado') DEFAULT 'Ativo',
    data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP,
    data_atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;


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

CREATE INDEX idx_nome ON contatos(nome);
CREATE INDEX idx_email ON contatos(email);
CREATE INDEX idx_status ON contatos(status);

SELECT 'Banco de dados criado com sucesso!' as Status;
SELECT COUNT(*) as Total_Contatos FROM contatos;
SELECT status, COUNT(*) as Quantidade FROM contatos GROUP BY status;


![1](https://github.com/user-attachments/assets/2e8fd3c4-94a8-4a11-875f-0d8c605fabbb)
![2](https://github.com/user-attachments/assets/849f0c3b-2e11-4dbf-8a42-2c9de1674455)
![3](https://github.com/user-attachments/assets/0cf5e099-4c2d-40da-97ee-22ea88df49e4)
![4](https://github.com/user-attachments/assets/2514ea5e-15a8-4487-9e4e-4951961a3317)
![5](https://github.com/user-attachments/assets/3d3e830f-f56e-489a-9e81-9cf62d899a95)
![6](https://github.com/user-attachments/assets/6e85fdbd-6ec7-4fad-940c-581db45c4aaf)
![7](https://github.com/user-attachments/assets/5728a1bb-7912-4be9-a035-617f7ef79486)


