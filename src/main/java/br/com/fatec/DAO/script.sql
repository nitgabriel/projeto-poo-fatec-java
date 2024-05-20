
create database Ultrapets;
use Ultrapets;


-- Criando a tabela de Donos
CREATE TABLE Donos (
    idDono INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    formaPagamento VARCHAR(50),
    contato VARCHAR(20)
);

-- Criando a tabela de Pets
CREATE TABLE Pets (
    idPet INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    especie VARCHAR(50) NOT NULL,
    idDono INT,
    numeroConveniado VARCHAR(50),
    raca VARCHAR(50),
    dataNascimento DATE,
    FOREIGN KEY (idDono) REFERENCES Donos(idDono)
);

-- Criando a tabela de Veterinários
CREATE TABLE Veterinarios (
    idVeterinario INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    crmv VARCHAR(20) UNIQUE NOT NULL,
    unidadeAtuacao VARCHAR(100),
    status VARCHAR(20), -- Ex: Ativo, Inativo
    especialidade VARCHAR(100)
);

-- Criando a tabela de Agendamentos
CREATE TABLE Agendamentos (
    idAgendamento INT PRIMARY KEY AUTO_INCREMENT,
    idDono INT,
    idPet INT,
    idVeterinario INT,
    unidade VARCHAR(100),
    especialidade VARCHAR(100),
    data DATE NOT NULL,
    horario TIME NOT NULL,
    FOREIGN KEY (idDono) REFERENCES Donos(idDono),
    FOREIGN KEY (idPet) REFERENCES Pets(idPet),
    FOREIGN KEY (idVeterinario) REFERENCES Veterinarios(idVeterinario)
);

-- Criando a tabela de Medicamentos
CREATE TABLE Medicamentos (
    idMedicamento INT PRIMARY KEY AUTO_INCREMENT,
    nomeMedicamento VARCHAR(255) NOT NULL,
    dataValidade DATE,
    dosagem VARCHAR(50),
    preco DECIMAL(10, 2),
    descricao TEXT
);

-- Criando a tabela intermediária para a relação muitos-para-muitos entre Agendamentos e Medicamentos
CREATE TABLE AgendamentosMedicamentos (
    idAgendamento INT,
    idMedicamento INT,
    FOREIGN KEY (idAgendamento) REFERENCES Agendamentos(idAgendamento),
    FOREIGN KEY (idMedicamento) REFERENCES Medicamentos(idMedicamento),
    PRIMARY KEY (idAgendamento, idMedicamento) 
);
