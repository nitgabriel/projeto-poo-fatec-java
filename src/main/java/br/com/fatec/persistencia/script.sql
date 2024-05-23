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

-- Criando a tabela de unidades
CREATE TABLE Unidades (
    -- Identificador único da unidade
    idUnidade INT PRIMARY KEY AUTO_INCREMENT,
    -- Nome da unidade específica
    nome VARCHAR(50) NOT NULL,
    -- Cep
    cep INT NOT NULL,
    -- Rua
    rua VARCHAR(50) NOT NULL,
    -- Bairro
    bairro VARCHAR(50) NOT NULL,
    -- Cidade
    cidade VARCHAR(50) NOT NULL,
    -- UF
    uf VARCHAR(2) NOT NULL,
    -- Número
    numero VARCHAR(50) NOT NULL
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
    idUnidade INT NOT NULL,
    -- Status do veterinário (ex: Ativo, Inativo)
    status VARCHAR(20) NOT NULL,
    -- Especialidade do veterinário (ex: clínica geral, cirurgia)
    especialidade VARCHAR(100) NOT NULL,
    FOREIGN KEY (idUnidade) REFERENCES Unidades(idUnidade)
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
    idUnidade INT NOT NULL,
    -- Especialidade do agendamento (pode ser diferente da especialidade do veterinário)
    especialidade VARCHAR(100) NOT NULL,
    -- Data do agendamento
    data DATE NOT NULL,
    -- Horário do agendamento
    horario TIME NOT NULL,
    FOREIGN KEY (idDono) REFERENCES Donos(idDono),
    FOREIGN KEY (idPet) REFERENCES Pets(idPet),
    FOREIGN KEY (idVeterinario) REFERENCES Veterinarios(idVeterinario),
    FOREIGN KEY (idUnidade) REFERENCES Unidades(idUnidade)
);
