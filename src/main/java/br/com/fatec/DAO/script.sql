-- Criação do banco de dados
CREATE DATABASE Ultrapets;
USE Ultrapets;

-- Criando a tabela de Donos
CREATE TABLE Donos (
    -- Identificador único do dono
    idDono INT PRIMARY KEY AUTO_INCREMENT,
    -- Nome completo do dono
    nome VARCHAR(255) NOT NULL,
    -- Endereço de e-mail do dono (único)
    email VARCHAR(255) UNIQUE NOT NULL,
    -- CPF do dono (único)
    cpf VARCHAR(14) UNIQUE NOT NULL,
    -- Forma de pagamento preferida (ex: dinheiro, cartão, pix)
    formaPagamento VARCHAR(50) NOT NULL,
    -- Número de telefone para contato
    contato VARCHAR(20) NOT NULL
);

-- Criando a tabela de Pets
CREATE TABLE Pets (
    -- Identificador único do pet
    idPet INT PRIMARY KEY AUTO_INCREMENT,
    -- Nome do pet
    nome VARCHAR(255) NOT NULL,
    -- Espécie do pet (ex: cão, gato)
    especie VARCHAR(50) NOT NULL,
    -- Dono do pet (chave estrangeira)
    idDono INT NOT NULL,
    -- Número do convênio (se houver)
    numeroConveniado VARCHAR(50) NOT NULL,
    -- Raça do pet
    raca VARCHAR(50) NOT NULL,
    -- Data de nascimento do pet
    dataNascimento DATE NOT NULL,
    FOREIGN KEY (idDono) REFERENCES Donos(idDono)
);

-- Criando a tabela de Veterinários
CREATE TABLE Veterinarios (
    -- Identificador único do veterinário
    idVeterinario INT PRIMARY KEY AUTO_INCREMENT,
    -- Nome completo do veterinário
    nome VARCHAR(255) NOT NULL,
    -- CRMV do veterinário (único)
    crmv VARCHAR(20) UNIQUE NOT NULL,
    -- Unidade onde o veterinário atua
    unidadeAtuacao VARCHAR(100) NOT NULL,
    -- Status do veterinário (ex: Ativo, Inativo)
    status VARCHAR(20) NOT NULL,
    -- Especialidade do veterinário (ex: clínica geral, cirurgia)
    especialidade VARCHAR(100) NOT NULL
);

-- Criando a tabela de Agendamentos
CREATE TABLE Agendamentos (
    -- Identificador único do agendamento
    idAgendamento INT PRIMARY KEY AUTO_INCREMENT,
    -- Dono do pet agendado (chave estrangeira)
    idDono INT NOT NULL,
    -- Pet agendado (chave estrangeira)
    idPet INT NOT NULL,
    -- Veterinário do agendamento (chave estrangeira)
    idVeterinario INT NOT NULL,
    -- Unidade do agendamento
    unidade VARCHAR(100) NOT NULL,
    -- Especialidade do agendamento (pode ser diferente da especialidade do veterinário)
    especialidade VARCHAR(100) NOT NULL,
    -- Data do agendamento
    data DATE NOT NULL,
    -- Horário do agendamento
    horario TIME NOT NULL,
    FOREIGN KEY (idDono) REFERENCES Donos(idDono),
    FOREIGN KEY (idPet) REFERENCES Pets(idPet),
    FOREIGN KEY (idVeterinario) REFERENCES Veterinarios(idVeterinario)
);

-- Criando a tabela de Medicamentos
CREATE TABLE Medicamentos (
    -- Identificador único do medicamento
    idMedicamento INT PRIMARY KEY AUTO_INCREMENT,
    -- Nome do medicamento
    nomeMedicamento VARCHAR(255) NOT NULL,
    -- Data de validade do medicamento
    dataValidade DATE NOT NULL,
    -- Dosagem recomendada do medicamento
    dosagem VARCHAR(50) NOT NULL,
    -- Preço do medicamento
    preco DECIMAL(10, 2) NOT NULL,
    -- Descrição do medicamento
    descricao TEXT NOT NULL
);

-- Criando a tabela intermediária para a relação muitos-para-muitos entre Agendamentos e Medicamentos
CREATE TABLE AgendamentosMedicamentos (
    -- Chave estrangeira do agendamento
    idAgendamento INT NOT NULL,
    -- Chave estrangeira do medicamento
    idMedicamento INT NOT NULL,
    FOREIGN KEY (idAgendamento) REFERENCES Agendamentos(idAgendamento),
    FOREIGN KEY (idMedicamento) REFERENCES Medicamentos(idMedicamento),
    -- Chave primária composta para garantir unicidade
    PRIMARY KEY (idAgendamento, idMedicamento)
);
