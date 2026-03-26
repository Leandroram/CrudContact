Projeto feito em Java Swing usando o NetBeans e Xampp.
Um sistema de CRUD simples, com paginação, Adicionar, Editar, Excluir e listar. Tem os campos de busca e cores de definem o status.
------------------------------------------------
------------------------------------------------
CREATE DATABASE IF NOT EXISTS crud_contatos 
CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE crud_contatos;

CREATE TABLE IF NOT EXISTS contatos ( <br>
    id INT PRIMARY KEY AUTO_INCREMENT,<br>
    nome VARCHAR(100) NOT NULL,<br>
    telefone VARCHAR(20) NOT NULL,<br>
    email VARCHAR(100) NOT NULL,<br>
    status ENUM('Ativo', 'Desativado') DEFAULT 'Ativo',<br>
    data_cadastro DATETIME DEFAULT CURRENT_TIMESTAMP,<br>
    data_atualizacao DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP<br>
) ENGINE=InnoDB;


INSERT INTO contatos (nome, telefone, email, status) VALUES <br>
('João Silva Santos', '(11) 98765-4321', 'joao.silva@email.com', 'Ativo'),<br>
('Maria Oliveira Costa', '(21) 91234-5678', 'maria.oliveira@email.com', 'Ativo'),<br>
('Pedro Souza Lima', '(31) 99876-5432', 'pedro.souza@email.com', 'Desativado'),<br>
('Ana Paula Ferreira', '(41) 97654-3210', 'ana.ferreira@email.com', 'Ativo'),<br>
('Carlos Eduardo Alves', '(51) 96543-2109', 'carlos.alves@email.com', 'Ativo'),<br>
('Juliana Santos Rocha', '(61) 95432-1098', 'juliana.rocha@email.com', 'Desativado'),<br>
('Fernando Costa Silva', '(71) 94321-0987', 'fernando.silva@email.com', 'Ativo'),<br>
('Patrícia Lima Souza', '(81) 93210-9876', 'patricia.souza@email.com', 'Ativo'),<br>
('Roberto Almeida Reis', '(85) 92109-8765', 'roberto.reis@email.com', 'Ativo'),<br>
('Camila Rodrigues Dias', '(91) 91098-7654', 'camila.dias@email.com', 'Desativado');<br>
<br><br>
CREATE INDEX idx_nome ON contatos(nome);<br>
CREATE INDEX idx_email ON contatos(email);<br>
CREATE INDEX idx_status ON contatos(status);<br>
<br><br>
SELECT 'Banco de dados criado com sucesso!' as Status;<br>
SELECT COUNT(*) as Total_Contatos FROM contatos;<br>
SELECT status, COUNT(*) as Quantidade FROM contatos GROUP BY status;<br>


![1](https://github.com/user-attachments/assets/2e8fd3c4-94a8-4a11-875f-0d8c605fabbb)
![2](https://github.com/user-attachments/assets/849f0c3b-2e11-4dbf-8a42-2c9de1674455)
![3](https://github.com/user-attachments/assets/0cf5e099-4c2d-40da-97ee-22ea88df49e4)
![4](https://github.com/user-attachments/assets/2514ea5e-15a8-4487-9e4e-4951961a3317)
![5](https://github.com/user-attachments/assets/3d3e830f-f56e-489a-9e81-9cf62d899a95)
![6](https://github.com/user-attachments/assets/6e85fdbd-6ec7-4fad-940c-581db45c4aaf)
![7](https://github.com/user-attachments/assets/5728a1bb-7912-4be9-a035-617f7ef79486)


