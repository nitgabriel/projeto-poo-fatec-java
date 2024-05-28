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
import java.util.function.Predicate;

import br.com.fatec.DAO.*;
import br.com.fatec.model.*;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.util.Pair;

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

    // FilteredList para filtrar eficientemente.
    private FilteredList<Agendamento> filteredAgendamentos;
    private FilteredList<Dono> filteredDonos;
    private FilteredList<Pet> filteredPets;
    private FilteredList<Unidade> filteredUnidades;
    private FilteredList<Veterinario> filteredVeterinarios;

    // Critérios de filtragem
    private Map<String, Pair<String, String>> tabFilters = new HashMap<>();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cbFiltros.setItems(FXCollections.observableArrayList("ID Agendamento", "ID Dono", "ID Pet", "ID Veterinario", "ID Unidade", "Especialidade", "Data", "Horario"));

        carregarESetarDadoseFiltros();
        setarListenerMudancaDeTabela();

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

    // FUNÇÕES PARA POPULAR TABELAS COM OS DADOS DO BANCO DE DADOS

    private void carregarESetarDadoseFiltros() {
        try {
            // Carregar dados dos DAOs
            dadosAgendamentos = FXCollections.observableArrayList(new AgendamentoDAO().lista(""));
            dadosDonos = FXCollections.observableArrayList(new DonoDAO().lista(""));
            dadosPets = FXCollections.observableArrayList(new PetDAO().lista(""));
            dadosUnidades = FXCollections.observableArrayList(new UnidadeDAO().lista(""));
            dadosVeterinarios = FXCollections.observableArrayList(new VeterinarioDAO().lista(""));

            filteredAgendamentos = new FilteredList<>(dadosAgendamentos);
            filteredDonos = new FilteredList<>(dadosDonos);
            filteredPets = new FilteredList<>(dadosPets);
            filteredUnidades = new FilteredList<>(dadosUnidades);
            filteredVeterinarios = new FilteredList<>(dadosVeterinarios);

            // Colocando os tbView com os dados
            tbViewAgendamento.setItems(filteredAgendamentos);
            tbViewDonos.setItems(filteredDonos);
            tbViewPets.setItems(filteredPets);
            tbViewUnidades.setItems(filteredUnidades);
            tbViewVeterinarios.setItems(filteredVeterinarios);

            setarCellValueFactories();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.WARNING, "ERRO DE DADOS", "ERRO AO CONSULTAR DADOS DO BANCO DE DADOS.\n" + e.getMessage());
        }
    }

    private void setarCellValueFactories() {
        // Agendamento
        colIdAgendamento.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdAgendamento()).asObject());
        colIdDono.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDono().getIdDono()).asObject());
        colIdPet.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPet().getId()).asObject());
        colIdVeterinario.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getVeterinario().getIdVeterinario()).asObject());
        colIdUnidade.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUnidade().getIdUnidade()).asObject());
        colEspecialidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEspecialidade()));
        colData.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getData()));
        colHorario.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getHorario()));

        // Donos
        colIdDonos.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdDono()).asObject());
        colNomeDono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        colEmailDono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        colCpfDono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCpf()));
        colFormaPagamentoDono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormaPagamento()));
        colContatoDono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getContato()));

        // Pets
        colIdPets.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNomePet.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        colEspeciePet.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEspecie()));
        colIdDonoPet.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDono().getIdDono()).asObject());
        colNumeroConveniadoPet.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumeroConveniado()));
        colRacaPet.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRaca()));
        colDataNascimentoPet.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDataNascimento()));

        // Unidades
        colIdUnidades.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdUnidade()).asObject());
        colNomeUnidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        colCepUnidade.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getCep()).asObject());
        colRuaUnidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRua()));
        colBairroUnidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getBairro()));
        colCidadeUnidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCidade()));
        colUfUnidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getUf()));
        colNumeroUnidade.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNumero()));

        // Veterinarios
        colIdVeterinarios.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIdVeterinario()).asObject());
        colNomeVeterinario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNome()));
        colCrmvVeterinario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCrmv()));
        colIdUnidadeVeterinario.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getUnidade().getIdUnidade()).asObject());
        colStatusVeterinario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));
        colEspecialidadeVeterinario.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEspecialidade()));
    }

    public void btnFiltrar_Click(ActionEvent actionEvent) {
        String tabName = tabPane.getSelectionModel().getSelectedItem().getText();
        String campo = cbFiltros.getValue();
        String filtro = txtFiltros.getText().toLowerCase();

        tabFilters.put(tabName, new Pair<>(campo, filtro));
        switch (tabName) {
            case "Agendamentos":
                filteredAgendamentos.setPredicate(createAgendamentoPredicate(campo, filtro));
                break;
            case "Donos":
                filteredDonos.setPredicate(createDonoPredicate(campo, filtro));
                break;
            case "Pets":
                filteredPets.setPredicate(createPetPredicate(campo, filtro));
                break;
            case "Unidades":
                filteredUnidades.setPredicate(createUnidadePredicate(campo, filtro));
                break;
            case "Veterinários":
                filteredVeterinarios.setPredicate(createVeterinarioPredicate(campo, filtro));
                break;
        }
    }

    public void btnRestaurar_Click(ActionEvent actionEvent) {
        // Tirando os filtros
        filteredAgendamentos.setPredicate(null);
        filteredDonos.setPredicate(null);
        filteredPets.setPredicate(null);
        filteredUnidades.setPredicate(null);
        filteredVeterinarios.setPredicate(null);

        // Tirando os critérios
        tabFilters.clear();
    }

    // FUNÇÕES PARA FILTRAR O AGENDAMENTO DE ACORDO COM O PARÂMETRO PASSADO

    private Predicate<Agendamento> createAgendamentoPredicate(String campo, String filtro) {
        return agendamento -> {
            switch (campo) {
                case "ID Agendamento":
                    return String.valueOf(agendamento.getIdAgendamento()).contains(filtro);
                case "ID Dono":
                    return String.valueOf(agendamento.getDono().getIdDono()).contains(filtro);
                case "ID Pet":
                    return String.valueOf(agendamento.getPet().getId()).contains(filtro);
                case "ID Veterinario":
                    return String.valueOf(agendamento.getVeterinario().getIdVeterinario()).contains(filtro);
                case "ID Unidade":
                    return String.valueOf(agendamento.getUnidade().getIdUnidade()).contains(filtro);
                case "Especialidade":
                    return agendamento.getEspecialidade().toLowerCase().contains(filtro);
                case "Data":
                    return agendamento.getData().toString().contains(filtro);
                case "Horario":
                    return agendamento.getHorario().toString().contains(filtro);
                default:
                    return true; // Mostrar todos se não houver filtros
            }
        };
    }

    private Predicate<Dono> createDonoPredicate(String campo, String filtro) {
        return dono -> {
            switch (campo) {
                case "ID Dono":
                    return String.valueOf(dono.getIdDono()).contains(filtro);
                case "Nome":
                    return dono.getNome().toLowerCase().contains(filtro);
                case "Email":
                    return dono.getEmail().toLowerCase().contains(filtro);
                case "CPF":
                    return dono.getCpf().toLowerCase().contains(filtro);
                case "Forma de Pagamento":
                    return dono.getFormaPagamento().toLowerCase().contains(filtro);
                case "Contato":
                    return dono.getContato().toLowerCase().contains(filtro);
                default:
                    return true;
            }
        };
    }

    private Predicate<Pet> createPetPredicate(String campo, String filtro) {
        return pet -> {
            switch (campo) {
                case "ID Pet":
                    return String.valueOf(pet.getId()).contains(filtro);
                case "Nome":
                    return pet.getNome() != null && pet.getNome().toLowerCase().contains(filtro);
                case "Espécie":
                    return pet.getEspecie() != null && pet.getEspecie().toLowerCase().contains(filtro);
                case "ID Dono":
                    return pet.getDono() != null && String.valueOf(pet.getDono().getIdDono()).contains(filtro);
                case "Número Conveniado":
                    return pet.getNumeroConveniado() != null && pet.getNumeroConveniado().toLowerCase().contains(filtro);
                case "Raça":
                    return pet.getRaca() != null && pet.getRaca().toLowerCase().contains(filtro);
                case "Data de Nascimento":
                    return pet.getDataNascimento() != null && pet.getDataNascimento().toString().contains(filtro);
                default:
                    return true;
            }
        };
    }

    private Predicate<Unidade> createUnidadePredicate(String campo, String filtro) {
        return unidade -> {
            switch (campo) {
                case "ID Unidade":
                    return String.valueOf(unidade.getIdUnidade()).contains(filtro);
                case "Nome":
                    return unidade.getNome().toLowerCase().contains(filtro);
                case "CEP":
                    return String.valueOf(unidade.getCep()).contains(filtro);
                case "Rua":
                    return unidade.getRua().toLowerCase().contains(filtro);
                case "Bairro":
                    return unidade.getBairro().toLowerCase().contains(filtro);
                case "Cidade":
                    return unidade.getCidade().toLowerCase().contains(filtro);
                case "UF":
                    return unidade.getUf().toLowerCase().contains(filtro);
                case "Número":
                    return unidade.getNumero().toLowerCase().contains(filtro);
                default:
                    return true;
            }
        };
    }

    private Predicate<Veterinario> createVeterinarioPredicate(String campo, String filtro) {
        return veterinario -> {
            switch (campo) {
                case "ID Veterinário":
                    return String.valueOf(veterinario.getIdVeterinario()).contains(filtro);
                case "Nome":
                    return veterinario.getNome() != null && veterinario.getNome().toLowerCase().contains(filtro);
                case "CRMV":
                    return veterinario.getCrmv() != null && veterinario.getCrmv().toLowerCase().contains(filtro);
                case "ID Unidade":
                    return veterinario.getUnidade() != null && String.valueOf(veterinario.getUnidade().getIdUnidade()).contains(filtro);
                case "Status":
                    return veterinario.getStatus() != null && veterinario.getStatus().toLowerCase().contains(filtro);
                case "Especialidade":
                    return veterinario.getEspecialidade() != null && veterinario.getEspecialidade().toLowerCase().contains(filtro);
                default:
                    return true;
            }
        };
    }

    // Setando Listener para quando o TabPane for alterado
    // Alterarmos também os items do cbFiltros.
    private void setarListenerMudancaDeTabela() {
        Map<String, List<String>> tabFilterOptions = new HashMap<>();
        tabFilterOptions.put("Agendamentos", Arrays.asList("ID Agendamento", "ID Dono", "ID Pet", "ID Veterinario", "ID Unidade", "Especialidade", "Data", "Horario"));
        tabFilterOptions.put("Donos", Arrays.asList("ID Dono", "Nome", "Email", "CPF", "Forma de Pagamento", "Contato"));
        tabFilterOptions.put("Pets", Arrays.asList("ID Pet", "Nome", "Espécie", "ID Dono", "Número Conveniado", "Raça", "Data de Nascimento"));
        tabFilterOptions.put("Unidades", Arrays.asList("ID Unidade", "Nome", "CEP", "Rua", "Bairro", "Cidade", "UF", "Número"));
        tabFilterOptions.put("Veterinários", Arrays.asList("ID Veterinário", "Nome", "CRMV", "ID Unidade", "Status", "Especialidade"));

        tabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldTab, newTab) -> {
            String tabName = newTab.getText();
            cbFiltros.setItems(FXCollections.observableArrayList(tabFilterOptions.getOrDefault(tabName, new ArrayList<>())));

            // Automatically apply the filter when switching tabs
            if (tabFilters.containsKey(tabName)) {
                Pair<String, String> filter = tabFilters.get(tabName);
                btnFiltrar_Click(null);
            } else {
                btnRestaurar_Click(null);
            }
        });
    }

}