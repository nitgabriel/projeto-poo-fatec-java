/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.fatec.controller;

import br.com.fatec.App;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import br.com.fatec.DAO.*;
import br.com.fatec.model.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class ConsultarInformacoesController implements Initializable {

    @FXML
    private Label lblMenu;
    @FXML
    private ComboBox<String> cbFiltros;
    @FXML
    private TextField txtFiltros;
    @FXML
    private Button btnFiltrar;
    @FXML
    private Button btnRestaurar;
    @FXML
    private TabPane tabPane;
    @FXML
    private TableView<Agendamento> tbViewAgendamento;
    @FXML
    private TableColumn<Agendamento, Integer> colIdAgendamento;
    @FXML
    private TableColumn<Agendamento, Integer> colIdDono;
    @FXML
    private TableColumn<Agendamento, Integer> colIdPet;
    @FXML
    private TableColumn<Agendamento, Integer> colIdVeterinario;
    @FXML
    private TableColumn<Agendamento, Integer> colIdUnidade;
    @FXML
    private TableColumn<Agendamento, String> colEspecialidade;
    @FXML
    private TableColumn<Agendamento, LocalDate> colData;
    @FXML
    private TableColumn<Agendamento, LocalTime> colHorario;
    @FXML
    private TableView<Dono> tbViewDonos;
    @FXML
    private TableColumn<Dono, Integer> colIdDonos;
    @FXML
    private TableColumn<Dono, String> colNomeDono;
    @FXML
    private TableColumn<Dono, String> colEmailDono;
    @FXML
    private TableColumn<Dono, String> colCpfDono;
    @FXML
    private TableColumn<Dono, String> colFormaPagamentoDono;
    @FXML
    private TableColumn<Dono, String> colContatoDono;
    @FXML
    private TableView<Pet> tbViewPets;
    @FXML
    private TableColumn<Pet, Integer> colIdPets;
    @FXML
    private TableColumn<Pet, String> colNomePet;
    @FXML
    private TableColumn<Pet, String> colEspeciePet;
    @FXML
    private TableColumn<Pet, Integer> colIdDonoPet;
    @FXML
    private TableColumn<Pet, String> colNumeroConveniadoPet;
    @FXML
    private TableColumn<Pet, String> colRacaPet;
    @FXML
    private TableColumn<Pet, LocalDate> colDataNascimentoPet;
    @FXML
    private TableView<Unidade> tbViewUnidades;
    @FXML
    private TableColumn<Unidade, Integer> colIdUnidades;
    @FXML
    private TableColumn<Unidade, String> colNomeUnidade;
    @FXML
    private TableColumn<Unidade, Integer> colCepUnidade;
    @FXML
    private TableColumn<Unidade, String> colRuaUnidade;
    @FXML
    private TableColumn<Unidade, String> colBairroUnidade;
    @FXML
    private TableColumn<Unidade, String> colCidadeUnidade;
    @FXML
    private TableColumn<Unidade, String> colUfUnidade;
    @FXML
    private TableColumn<Unidade, String> colNumeroUnidade;
    @FXML
    private TableView<Veterinario> tbViewVeterinarios;
    @FXML
    private TableColumn<Veterinario, Integer> colIdVeterinarios;
    @FXML
    private TableColumn<Veterinario, String> colNomeVeterinario;
    @FXML
    private TableColumn<Veterinario, String> colCrmvVeterinario;
    @FXML
    private TableColumn<Veterinario, Integer> colIdUnidadeVeterinario;
    @FXML
    private TableColumn<Veterinario, String> colStatusVeterinario;
    @FXML
    private TableColumn<Veterinario, String> colEspecialidadeVeterinario;

    private ObservableList<Agendamento> dadosAgendamentos;
    private ObservableList<Dono> dadosDonos;
    private ObservableList<Pet> dadosPets;
    private ObservableList<Unidade> dadosUnidades;
    private ObservableList<Veterinario> dadosVeterinarios;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbFiltros.setItems(FXCollections.observableArrayList("ID Agendamento", "ID Dono", "ID Pet", "ID Veterinario", "ID Unidade", "Especialidade", "Data", "Horario"));

        popularTabelaAgendamento();
        popularTabelaDonos();
        popularTabelaPets();
        popularTabelaUnidades();
        popularTabelaVeterinarios();

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            ObservableList<String> items;
            if (newTab.getText().equals("Agendamentos")) {
                items = FXCollections.observableArrayList("ID Agendamento", "ID Dono", "ID Pet", "ID Veterinario", "ID Unidade", "Especialidade", "Data", "Horario");
            } else if (newTab.getText().equals("Donos")) {
                items = FXCollections.observableArrayList("ID Dono", "Nome", "Email", "CPF", "Forma de Pagamento", "Contato");
            } else if (newTab.getText().equals("Pets")) {
                items = FXCollections.observableArrayList("ID Pet", "Nome", "Espécie", "ID Dono", "Número Conveniado", "Raça", "Data de Nascimento");
            } else if (newTab.getText().equals("Unidades")) {
                items = FXCollections.observableArrayList("ID Unidade", "Nome", "CEP", "Rua", "Bairro", "Cidade", "UF", "Número");
            } else if (newTab.getText().equals("Veterinários")) {
                items = FXCollections.observableArrayList("ID Veterinário", "Nome", "CRMV", "ID Unidade", "Status", "Especialidade");
            } else {
                items = FXCollections.observableArrayList();
            }
            cbFiltros.setItems(items);
        });
    }    

    @FXML
    private void lblMenu_Click(MouseEvent event) {
        try {
            App.setRoot("view/Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void popularTabelaAgendamento() {
        try {
            AgendamentoDAO agendamentoDAO = new AgendamentoDAO();

            Collection<Agendamento> agendamentos = agendamentoDAO.lista("");


            dadosAgendamentos = FXCollections.observableArrayList(agendamentos);

            tbViewAgendamento.setItems(dadosAgendamentos);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.WARNING, "ERRO DE DADOS", "ERRO AO CONSULTAR DADOS DO BANCO DE DADOS.\n" + e.getMessage());
        }

        colIdAgendamento.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdAgendamento()).asObject());
        colIdDono.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDono().getIdDono()).asObject());
        colIdPet.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPet().getId()).asObject());
        colIdVeterinario.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getVeterinario().getIdVeterinario()).asObject());
        colIdUnidade.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUnidade().getIdUnidade()).asObject());
        colEspecialidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEspecialidade()));
        colData.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getData()));
        colHorario.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getHorario()));
    }

    private void popularTabelaDonos() {
        try {
            DonoDAO donoDAO = new DonoDAO();

            Collection<Dono> agendamentos = donoDAO.lista("");

            dadosDonos = FXCollections.observableArrayList(agendamentos);

            tbViewDonos.setItems(dadosDonos);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.WARNING, "ERRO DE DADOS", "ERRO AO CONSULTAR DADOS DO BANCO DE DADOS.\n" + e.getMessage());
        }

        colIdDonos.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdDono()).asObject());
        colNomeDono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        colEmailDono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        colCpfDono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCpf()));
        colFormaPagamentoDono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormaPagamento()));
        colContatoDono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContato()));
    }

    private void popularTabelaPets() {
        try {
            PetDAO petDAO = new PetDAO();

            Collection<Pet> agendamentos = petDAO.lista("");

            dadosPets = FXCollections.observableArrayList(agendamentos);

            tbViewPets.setItems(dadosPets);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.WARNING, "ERRO DE DADOS", "ERRO AO CONSULTAR DADOS DO BANCO DE DADOS.\n" + e.getMessage());
        }

        colIdPets.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNomePet.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        colEspeciePet.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEspecie()));
        colIdDonoPet.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDono().getIdDono()).asObject());
        colNumeroConveniadoPet.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumeroConveniado()));
        colRacaPet.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRaca()));
        colDataNascimentoPet.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDataNascimento()));
    }

    private void popularTabelaUnidades() {
        try {
            UnidadeDAO unidadeDAO = new UnidadeDAO();

            Collection<Unidade> agendamentos = unidadeDAO.lista("");

            dadosUnidades = FXCollections.observableArrayList(agendamentos);

            tbViewUnidades.setItems(dadosUnidades);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.WARNING, "ERRO DE DADOS", "ERRO AO CONSULTAR DADOS DO BANCO DE DADOS.\n" + e.getMessage());
        }

        colIdUnidades.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdUnidade()).asObject());
        colNomeUnidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        colCepUnidade.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCep()).asObject());
        colRuaUnidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRua()));
        colBairroUnidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBairro()));
        colCidadeUnidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCidade()));
        colUfUnidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUf()));
        colNumeroUnidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumero()));
    }

    private void popularTabelaVeterinarios() {
        try {
            VeterinarioDAO veterinarioDAO = new VeterinarioDAO();

            Collection<Veterinario> agendamentos = veterinarioDAO.lista("");

            dadosVeterinarios = FXCollections.observableArrayList(agendamentos);

            tbViewVeterinarios.setItems(dadosVeterinarios);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.WARNING, "ERRO DE DADOS", "ERRO AO CONSULTAR DADOS DO BANCO DE DADOS.\n" + e.getMessage());
        }

        colIdVeterinarios.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdVeterinario()).asObject());
        colNomeVeterinario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        colCrmvVeterinario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCrmv()));
        colIdUnidadeVeterinario.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUnidade().getIdUnidade()).asObject());
        colStatusVeterinario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        colEspecialidadeVeterinario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEspecialidade()));
    }


    public void btnFiltrar_Click(ActionEvent actionEvent) {
        String campo = cbFiltros.getValue();
        String filtro = txtFiltros.getText();

        if (tabPane.getSelectionModel().getSelectedItem().getText().equals("Agendamentos")) {
            ObservableList<Agendamento> dadosFiltrados = filtrarAgendamentos(campo, filtro);
            tbViewAgendamento.setItems(dadosFiltrados);
        } else if (tabPane.getSelectionModel().getSelectedItem().getText().equals("Donos")) {
            ObservableList<Dono> dadosFiltrados = filtrarDonos(campo, filtro);
            tbViewDonos.setItems(dadosFiltrados);
        } else if (tabPane.getSelectionModel().getSelectedItem().getText().equals("Pets")) {
            ObservableList<Pet> dadosFiltrados = filtrarPets(campo, filtro);
            tbViewPets.setItems(dadosFiltrados);
        } else if (tabPane.getSelectionModel().getSelectedItem().getText().equals("Unidades")) {
            ObservableList<Unidade> dadosFiltrados = filtrarUnidades(campo, filtro);
            tbViewUnidades.setItems(dadosFiltrados);
        } else if (tabPane.getSelectionModel().getSelectedItem().getText().equals("Veterinários")) {
            ObservableList<Veterinario> dadosFiltrados = filtrarVeterinarios(campo, filtro);
            tbViewVeterinarios.setItems(dadosFiltrados);
        }

    }

    public void btnRestaurar_Click(ActionEvent actionEvent) {
        popularTabelaAgendamento();
        popularTabelaDonos();
        popularTabelaPets();
        popularTabelaUnidades();
        popularTabelaVeterinarios();
    }

    private ObservableList<Agendamento> filtrarAgendamentos(String campo, String filtro) {
        return dadosAgendamentos.filtered(agendamento -> {
            switch (campo) {
                case "ID Agendamento":
                    return String.valueOf(agendamento.getIdAgendamento()).toLowerCase().contains(filtro.toLowerCase());
                case "ID Dono":
                    return String.valueOf(agendamento.getDono().getIdDono()).toLowerCase().contains(filtro.toLowerCase());
                case "ID Pet":
                    return String.valueOf(agendamento.getPet().getId()).toLowerCase().contains(filtro.toLowerCase());
                case "ID Veterinario":
                    return String.valueOf(agendamento.getVeterinario().getIdVeterinario()).toLowerCase().contains(filtro.toLowerCase());
                case "ID Unidade":
                    return String.valueOf(agendamento.getUnidade().getIdUnidade()).toLowerCase().contains(filtro.toLowerCase());
                case "Especialidade":
                    return agendamento.getEspecialidade().toLowerCase().contains(filtro.toLowerCase());
                case "Data":
                    return agendamento.getData().toString().toLowerCase().contains(filtro.toLowerCase());
                case "Horario":
                    return agendamento.getHorario().toString().toLowerCase().contains(filtro.toLowerCase());
                default:
                    return false;
            }
        });
    }

    private ObservableList<Dono> filtrarDonos(String campo, String filtro) {
        return dadosDonos.filtered(dono -> {
            switch (campo) {
                case "ID Dono":
                    return String.valueOf(dono.getIdDono()).toLowerCase().contains(filtro.toLowerCase());
                case "Nome":
                    return dono.getNome().toLowerCase().contains(filtro.toLowerCase());
                case "Email":
                    return dono.getEmail().toLowerCase().contains(filtro.toLowerCase());
                case "CPF":
                    return dono.getCpf().toLowerCase().contains(filtro.toLowerCase());
                case "Forma de Pagamento":
                    return dono.getFormaPagamento().toLowerCase().contains(filtro.toLowerCase());
                case "Contato":
                    return dono.getContato().toLowerCase().contains(filtro.toLowerCase());
                default:
                    return false;
            }
        });
    }

    private ObservableList<Pet> filtrarPets(String campo, String filtro) {
        return dadosPets.filtered(pet -> {
            switch (campo) {
                case "ID Pet":
                    return String.valueOf(pet.getId()).toLowerCase().contains(filtro.toLowerCase());
                case "Nome":
                    return pet.getNome().toLowerCase().contains(filtro.toLowerCase());
                case "Espécie":
                    return pet.getEspecie().toLowerCase().contains(filtro.toLowerCase());
                case "ID Dono":
                    return String.valueOf(pet.getDono().getIdDono()).toLowerCase().contains(filtro.toLowerCase());
                case "Número Conveniado":
                    return pet.getNumeroConveniado().toLowerCase().contains(filtro.toLowerCase());
                case "Raça":
                    return pet.getRaca().toLowerCase().contains(filtro.toLowerCase());
                case "Data de Nascimento":
                    return pet.getDataNascimento().toString().toLowerCase().contains(filtro.toLowerCase());
                default:
                    return false;
            }
        });
    }

    private ObservableList<Unidade> filtrarUnidades(String campo, String filtro) {
        return dadosUnidades.filtered(unidade -> {
            switch (campo) {
                case "ID Unidade":
                    return String.valueOf(unidade.getIdUnidade()).toLowerCase().contains(filtro.toLowerCase());
                case "Nome":
                    return unidade.getNome().toLowerCase().contains(filtro.toLowerCase());
                case "CEP":
                    return String.valueOf(unidade.getCep()).toLowerCase().contains(filtro.toLowerCase());
                case "Rua":
                    return unidade.getRua().toLowerCase().contains(filtro.toLowerCase());
                case "Bairro":
                    return unidade.getBairro().toLowerCase().contains(filtro.toLowerCase());
                case "Cidade":
                    return unidade.getCidade().toLowerCase().contains(filtro.toLowerCase());
                case "UF":
                    return unidade.getUf().toLowerCase().contains(filtro.toLowerCase());
                case "Número":
                    return unidade.getNumero().toLowerCase().contains(filtro.toLowerCase());
                default:
                    return false;
            }
        });
    }

    private ObservableList<Veterinario> filtrarVeterinarios(String campo, String filtro) {
        return dadosVeterinarios.filtered(veterinario -> {
            switch (campo) {
                case "ID Veterinário":
                    return String.valueOf(veterinario.getIdVeterinario()).toLowerCase().contains(filtro.toLowerCase());
                case "Nome":
                    return veterinario.getNome().toLowerCase().contains(filtro.toLowerCase());
                case "CRMV":
                    return veterinario.getCrmv().toLowerCase().contains(filtro.toLowerCase());
                case "ID Unidade":
                    return String.valueOf(veterinario.getUnidade().getIdUnidade()).toLowerCase().contains(filtro.toLowerCase());
                case "Status":
                    return veterinario.getStatus().toLowerCase().contains(filtro.toLowerCase());
                case "Especialidade":
                    return veterinario.getEspecialidade().toLowerCase().contains(filtro.toLowerCase());
                default:
                    return false;
            }
        });
    }
}