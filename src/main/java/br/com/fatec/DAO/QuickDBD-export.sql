-- Exported from QuickDBD: https://www.quickdatabasediagrams.com/
-- NOTE! If you have used non-SQL datatypes in your design, you will have to change these here.

-- Diagrama de Banco de Dados para um Pet Shop

-- Tabela de Donos dos Pets
CREATE TABLE `Donos` (
    -- Identificador único do dono
    `idDono` INT AUTO_INCREMENT NOT NULL ,
    -- Nome completo do dono
    `nome` VARCHAR(255)  NOT NULL ,
    -- Endereço de e-mail do dono (único)
    `email` VARCHAR(255)  NOT NULL ,
    -- CPF do dono (único)
    `cpf` VARCHAR(14)  NOT NULL ,
    -- Forma de pagamento preferida (ex: dinheiro, cartão)
    `formaPagamento` VARCHAR(50)  NOT NULL ,
    -- Número de telefone para contato
    `contato` VARCHAR(20)  NOT NULL ,
    PRIMARY KEY (
        `idDono`
    )
);

-- Tabela de Pets
CREATE TABLE `Pets` (
    -- Identificador único do pet
    `idPet` INT AUTO_INCREMENT NOT NULL ,
    -- Nome do pet
    `nome` VARCHAR(255)  NOT NULL ,
    -- Espécie do pet (ex: cão, gato)
    `especie` VARCHAR(50)  NOT NULL ,
    -- Dono do pet (chave estrangeira)
    `idDono` INT  NOT NULL ,
    -- Número do convênio (se houver)
    `numeroConveniado` VARCHAR(50)  NOT NULL ,
    -- Raça do pet
    `raca` VARCHAR(50)  NOT NULL ,
    -- Data de nascimento do pet
    `dataNascimento` DATE  NOT NULL ,
    PRIMARY KEY (
        `idPet`
    )
);

-- Tabela de Veterinários
CREATE TABLE `Veterinarios` (
    -- Identificador único do veterinário
    `idVeterinario` INT AUTO_INCREMENT NOT NULL ,
    -- Nome completo do veterinário
    `nome` VARCHAR(255)  NOT NULL ,
    -- CRMV do veterinário (único)
    `crmv` VARCHAR(20)  NOT NULL ,
    -- Unidade onde o veterinário atua
    `unidadeAtuacao` VARCHAR(100)  NOT NULL ,
    -- Status do veterinário (ex: Ativo, Inativo)
    `status` VARCHAR(20)  NOT NULL ,
    -- Especialidade do veterinário (ex: clínica geral, cirurgia)
    `especialidade` VARCHAR(100)  NOT NULL ,
    PRIMARY KEY (
        `idVeterinario`
    )
);

-- Tabela de Agendamentos
CREATE TABLE `Agendamentos` (
    -- Identificador único do agendamento
    `idAgendamento` INT AUTO_INCREMENT NOT NULL ,
    -- Dono do pet agendado (chave estrangeira)
    `idDono` INT  NOT NULL ,
    -- Pet agendado (chave estrangeira)
    `idPet` INT  NOT NULL ,
    -- Veterinário do agendamento (chave estrangeira)
    `idVeterinario` INT  NOT NULL ,
    -- Unidade do agendamento
    `unidade` VARCHAR(100)  NOT NULL ,
    -- Especialidade do agendamento (pode ser diferente da especialidade do veterinário)
    `especialidade` VARCHAR(100)  NOT NULL ,
    -- Data do agendamento
    `data` DATE  NOT NULL ,
    -- Horário do agendamento
    `horario` TIME  NOT NULL ,
    PRIMARY KEY (
        `idAgendamento`
    )
);

-- Tabela de Medicamentos
CREATE TABLE `Medicamentos` (
    -- Identificador único do medicamento
    `idMedicamento` INT AUTO_INCREMENT NOT NULL ,
    -- Nome do medicamento
    `nomeMedicamento` VARCHAR(255)  NOT NULL ,
    -- Data de validade do medicamento
    `dataValidade` DATE  NOT NULL ,
    -- Dosagem recomendada do medicamento
    `dosagem` VARCHAR(50)  NOT NULL ,
    -- Preço do medicamento
    `preco` money  NOT NULL ,
    -- Descrição do medicamento
    `descricao` TEXT  NOT NULL ,
    PRIMARY KEY (
        `idMedicamento`
    )
);

-- Tabela de Associação entre Agendamentos e Medicamentos
CREATE TABLE `AgendamentosMedicamentos` (
    -- Chave estrangeira do agendamento
    `idAgendamento` INT  NOT NULL ,
    -- Chave estrangeira do medicamento
    `idMedicamento` INT  NOT NULL 
);

ALTER TABLE `Pets` ADD CONSTRAINT `fk_Pets_idDono` FOREIGN KEY(`idDono`)
REFERENCES `Donos` (`idDono`);

ALTER TABLE `Agendamentos` ADD CONSTRAINT `fk_Agendamentos_idDono` FOREIGN KEY(`idDono`)
REFERENCES `Donos` (`idDono`);

ALTER TABLE `Agendamentos` ADD CONSTRAINT `fk_Agendamentos_idPet` FOREIGN KEY(`idPet`)
REFERENCES `Pets` (`idPet`);

ALTER TABLE `Agendamentos` ADD CONSTRAINT `fk_Agendamentos_idVeterinario` FOREIGN KEY(`idVeterinario`)
REFERENCES `Veterinarios` (`idVeterinario`);

ALTER TABLE `AgendamentosMedicamentos` ADD CONSTRAINT `fk_AgendamentosMedicamentos_idAgendamento` FOREIGN KEY(`idAgendamento`)
REFERENCES `Agendamentos` (`idAgendamento`);

ALTER TABLE `AgendamentosMedicamentos` ADD CONSTRAINT `fk_AgendamentosMedicamentos_idMedicamento` FOREIGN KEY(`idMedicamento`)
REFERENCES `Medicamentos` (`idMedicamento`);

