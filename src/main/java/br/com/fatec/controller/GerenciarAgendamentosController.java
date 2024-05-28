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
import br.com.fatec.exceptions.InvalidDateException;
import br.com.fatec.exceptions.InvalidOwnerException;
import br.com.fatec.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class GerenciarAgendamentosController implements Initializable {

    @FXML
    private Button btnRegistrarAgendamento;
    @FXML
    private Button btnAlterarAgendamento;
    @FXML
    private Button btnExcluirAgendamento;
    @FXML
    private Button btnConsultarAgendamento;
    @FXML
    private ComboBox<Dono> cbDonos;
    @FXML
    private TextField txtEspecialidade;
    @FXML
    private ComboBox<Pet> cbPets;
    @FXML
    private DatePicker dpDataAgend;
    @FXML
    private ComboBox<Unidade> cbUnidade;
    @FXML
    private ComboBox<LocalTime> cbHorarios;
    @FXML
    private ComboBox<Medicamento> cbMedicamentos;
    @FXML
    private Label lblMenu;

    private List<Control> controls;

    private ObservableList<Agendamento> agendamentoObservableList = FXCollections.observableArrayList();

    private List<LocalTime> horariosDisponiveis = new ArrayList<>();

    private List<Medicamento> medicamentoList = new ArrayList<>();
    @FXML
    private ComboBox<Veterinario> cbVeterinarios;
    @FXML
    private RadioButton rbAgendar;
    @FXML
    private RadioButton rbVisualizarAgend;
    @FXML
    private TableView<Agendamento> tbViewAgendamentos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controls = Arrays.asList(txtEspecialidade, dpDataAgend, cbDonos, cbPets, cbUnidade, cbHorarios, cbVeterinarios);
        dpDataAgend.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    List<LocalTime> horarios;
                    if (rbAgendar.isSelected()) {
                        horarios = getHorariosDisponiveisPorData(newValue);
                    } else {
                        horarios = getHorariosAgendadosPorData(newValue);
                    }
                    // Adicione os horários ao ComboBox.
                    ObservableList<LocalTime> horariosList = FXCollections.observableArrayList(horarios);
                    cbHorarios.setItems(horariosList);
                    // Atualize a TableView
                    if (rbAgendar.isSelected()) {
                        tbViewAgendamentos.setItems(getHorariosDisponiveis(newValue));
                    } else if (rbVisualizarAgend.isSelected()) {
                        tbViewAgendamentos.setItems(getAgendamentosPorData(newValue));
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        DonoDAO donoDAO = new DonoDAO();
        PetDAO petDAO = new PetDAO();
        UnidadeDAO unidadeDAO = new UnidadeDAO();
        VeterinarioDAO veterinarioDAO = new VeterinarioDAO();

        try {
            Collection<Dono> donos = donoDAO.lista("");
            cbDonos.setItems(FXCollections.observableArrayList(donos));
            cbDonos.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                if (newValue != null) {
                    Dono selectedDono = newValue;
                    try {
                        Collection<Pet> pets = petDAO.lista("idDono = " + selectedDono.getIdDono());
                        cbPets.setItems(FXCollections.observableArrayList(pets));
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO LOCALIZAR OS PETS.");
                    }
                }
            });

            Collection<Unidade> unidades = unidadeDAO.lista("");
            cbUnidade.setItems(FXCollections.observableArrayList(unidades));
            cbUnidade.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                if (newValue != null) {
                    Unidade selectedUnidade = newValue;
                    try {
                        Collection<Veterinario> veterinarios = veterinarioDAO.lista("idUnidade = " + selectedUnidade.getIdUnidade());
                        cbVeterinarios.setItems(FXCollections.observableArrayList(veterinarios));
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO LOCALIZAR OS VETERINÁRIOS.");
                    }
                }
            });

            List<Medicamento> medicamentos = GerenciarMedicamentosController.getMedicamentoList();
            cbMedicamentos.setItems(FXCollections.observableArrayList(medicamentos));

        } catch (SQLException e) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO LOCALIZAR OS DADOS.");
        }

        final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
                        try {
                            Collection<Agendamento> agendamentos = agendamentoDAO.lista("data = '" + item + "'");
                            if (agendamentos.size() == 7) { // Se todos os 7 horários do dia estiverem agendados
                                setStyle("-fx-background-color: #ff4444;");
                            }
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                };
            }
        };
        dpDataAgend.setDayCellFactory(dayCellFactory);

        btnRegistrarAgendamento.setDisable(true);
        btnAlterarAgendamento.setDisable(true);
        btnExcluirAgendamento.setDisable(true);
        btnConsultarAgendamento.setDisable(true);

        addListenerRadioButtons();
    }

    @FXML
    private void btnRegistrarAgendamento_Click(ActionEvent event) {
        if(areFieldsEmpty(controls)) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA TODOS OS CAMPOS ANTES DE CADASTRAR UM MEDICAMENTO.");
        } else {
            try {
                validateDate(dpDataAgend.getValue());
                validateOwner(cbPets.getValue(), cbDonos.getValue());

                Agendamento agendamento = carrega_Agendamento();
                AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
                if (agendamentoDAO.insere(agendamento)) {
                    showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "AGENDAMENTO REGISTRADO COM SUCESSO.");
                    controls.forEach(control -> {
                        if (control instanceof TextField) {
                            ((TextField) control).clear();
                        } else if (control instanceof ComboBox) {
                            ((ComboBox<?>) control).getSelectionModel().clearSelection();
                        } else if (control instanceof DatePicker) {
                            ((DatePicker) control).getEditor().clear();
                        }
                    });
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO REGISTRAR AGENDAMENTO.");
                }
            } catch (InvalidDateException | InvalidOwnerException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO REGISTRAR AGENDAMENTO:\n" + e.getMessage());
            }
        }
    }

    @FXML
    private void btnAlterarAgendamento_Click(ActionEvent event) {
        if(areFieldsEmpty(controls)) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA TODOS OS CAMPOS ANTES DE ALTERAR UM AGENDAMENTO.");
        } else {
            try {
                validateDate(dpDataAgend.getValue());
                validateOwner(cbPets.getValue(), cbDonos.getValue());

                AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
                Agendamento selectedAgendamento = agendamentoDAO.buscaPorDataHorario(dpDataAgend.getValue(), cbHorarios.getValue());

                if (selectedAgendamento != null) {
                    Agendamento agendamento = carrega_Agendamento();
                    agendamento.setIdAgendamento(selectedAgendamento.getIdAgendamento());

                    if (agendamentoDAO.altera(agendamento)) {
                        showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "AGENDAMENTO ALTERADO COM SUCESSO.");
                        controls.forEach(control -> {
                            if (control instanceof TextField) {
                                ((TextField) control).clear();
                            } else if (control instanceof ComboBox) {
                                ((ComboBox<?>) control).getSelectionModel().clearSelection();
                            } else if (control instanceof DatePicker) {
                                ((DatePicker) control).getEditor().clear();
                            }
                        });
                    } else {
                        showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO ALTERAR AGENDAMENTO.");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "NÃO FOI ENCONTRADO UM AGENDAMENTO COM A DATA E HORÁRIO INFORMADOS.");
                }
            } catch (InvalidDateException | InvalidOwnerException | SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO REGISTRAR AGENDAMENTO:\n" + e.getMessage());
            }
        }
    }

    @FXML
    private void btnExcluirAgendamento_Click(ActionEvent event) {
        if(dpDataAgend.getValue() == null || cbHorarios.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "SELECIONE UMA DATA E HORÁRIO PARA EXCLUIR UM AGENDAMENTO.");
        } else {
            try {
                AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
                Agendamento selectedAgendamento = agendamentoDAO.buscaPorDataHorario(dpDataAgend.getValue(), cbHorarios.getValue());

                if (selectedAgendamento != null) {
                    if (agendamentoDAO.remove(selectedAgendamento)) {
                        showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "AGENDAMENTO EXCLUÍDO COM SUCESSO.");
                        controls.forEach(control -> {
                            if (control instanceof TextField) {
                                ((TextField) control).clear();
                            } else if (control instanceof ComboBox) {
                                ((ComboBox<?>) control).getSelectionModel().clearSelection();
                            } else if (control instanceof DatePicker) {
                                ((DatePicker) control).getEditor().clear();
                            }
                        });
                    } else {
                        showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO EXCLUIR AGENDAMENTO.");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "NÃO FOI ENCONTRADO UM AGENDAMENTO COM A DATA E HORÁRIO INFORMADOS.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO EXCLUIR AGENDAMENTO: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnConsultarAgendamento_Click(ActionEvent event) {
        if(dpDataAgend.getValue() == null || cbHorarios.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "SELECIONE UMA DATA E HORÁRIO PARA EXCLUIR UM AGENDAMENTO.");
        } else {
            try {
                AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
                Agendamento selectedAgendamento = agendamentoDAO.buscaPorDataHorario(dpDataAgend.getValue(), cbHorarios.getValue());

                if (selectedAgendamento != null) {
                    carregar_Campos(selectedAgendamento);
                    // Habilita os botões de alterar e excluir
                    btnAlterarAgendamento.setDisable(false);
                    btnExcluirAgendamento.setDisable(false);
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "NÃO FOI ENCONTRADO UM AGENDAMENTO COM A DATA E HORÁRIO INFORMADOS.");
                    // Desabilita os botões de alterar e excluir
                    btnAlterarAgendamento.setDisable(true);
                    btnExcluirAgendamento.setDisable(true);
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO CONSULTAR AGENDAMENTO: " + e.getMessage());
            }
        }
    }

    @FXML
    private void lblMenu_Click(MouseEvent event) {
        try {
            App.setRoot("view/Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean areFieldsEmpty(List<Control> controls) {
        for (Control control : controls) {
            if (control instanceof TextField) {
                TextField textField = (TextField) control;
                if (textField.getText().isEmpty()) {
                    return true;
                }
            } else if (control instanceof ComboBox) {
                ComboBox<?> comboBox = (ComboBox<?>) control;
                if (comboBox.getValue() == null) {
                    return true;
                }
            } else if (control instanceof DatePicker) {
                DatePicker datePicker = (DatePicker) control;
                if (datePicker.getValue() == null) {
                    return true;
                }
            }
        }
        return false;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void validateDate(LocalDate date) throws InvalidDateException {
        if (date.isBefore(LocalDate.now())) {
            throw new InvalidDateException("Não é possível agendar um agendamento para uma data anterior ao dia atual.");
        }
    }

    public static void validateOwner(Pet pet, Dono dono) throws InvalidOwnerException {
        if (!pet.getDono().equals(dono)) {
            throw new InvalidOwnerException("O pet não está associado ao dono correto.");
        }
    }

    public void addListenerRadioButtons() {
        ToggleGroup group = new ToggleGroup();
        rbAgendar.setToggleGroup(group);
        rbVisualizarAgend.setToggleGroup(group);

        group.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioButton selectedRadioButton = (RadioButton) newValue;
            if (selectedRadioButton == rbAgendar) {
                atualizarTbViewAgendar();
                btnRegistrarAgendamento.setDisable(false);
                btnAlterarAgendamento.setDisable(true);
                btnExcluirAgendamento.setDisable(true);
                btnConsultarAgendamento.setDisable(true);
            } else if (selectedRadioButton == rbVisualizarAgend) {
                atualizarTbViewVisualizar();
                btnRegistrarAgendamento.setDisable(true);
                btnAlterarAgendamento.setDisable(true);
                btnExcluirAgendamento.setDisable(true);
                btnConsultarAgendamento.setDisable(false);
            }
        });
    }

    private void atualizarTbViewAgendar() {
        tbViewAgendamentos.getItems().clear();
        tbViewAgendamentos.getColumns().clear();

        // Adicione as colunas para mostrar a data e o horário
        TableColumn<Agendamento, LocalDate> dateColumn = new TableColumn<>("Data");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("data"));

        TableColumn<Agendamento, LocalTime> timeColumn = new TableColumn<>("Horário");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("horario"));

        tbViewAgendamentos.getColumns().add(dateColumn);
        tbViewAgendamentos.getColumns().add(timeColumn);

        // Adicione os dados dos horários disponíveis
        LocalDate selectedDate = dpDataAgend.getValue();
        if (selectedDate != null) {
            tbViewAgendamentos.setItems(getHorariosDisponiveis(selectedDate));
        }
    }

    private void atualizarTbViewVisualizar() {
        tbViewAgendamentos.getItems().clear();
        tbViewAgendamentos.getColumns().clear();

        // Adicione as colunas para mostrar os detalhes do agendamento
        TableColumn<Agendamento, LocalDate> dateColumn = new TableColumn<>("Data");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("data"));

        TableColumn<Agendamento, LocalTime> timeColumn = new TableColumn<>("Horário");
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("horario"));

        TableColumn<Agendamento, Dono> ownerColumn = new TableColumn<>("Dono");
        ownerColumn.setCellValueFactory(new PropertyValueFactory<>("dono"));

        TableColumn<Agendamento, Pet> petColumn = new TableColumn<>("Pet");
        petColumn.setCellValueFactory(new PropertyValueFactory<>("pet"));

        TableColumn<Agendamento, Unidade> unitColumn = new TableColumn<>("Unidade");
        unitColumn.setCellValueFactory(new PropertyValueFactory<>("unidade"));

        TableColumn<Agendamento, Veterinario> vetColumn = new TableColumn<>("Veterinário");
        vetColumn.setCellValueFactory(new PropertyValueFactory<>("veterinario"));

        // Adicione as colunas na ordem desejada
        tbViewAgendamentos.getColumns().addAll(dateColumn, timeColumn, ownerColumn, petColumn, unitColumn, vetColumn);

        // Adicione os dados dos agendamentos existentes
        tbViewAgendamentos.setItems(getAgendamentosExistentes());
    }

    private List<LocalTime> getHorariosDisponiveisPorData(LocalDate date) throws SQLException {
        // Criando uma lista de horários disponíveis.
        List<LocalTime> horariosDisponiveis = new ArrayList<>();
        for (int i = 10; i <= 16; i++) {
            horariosDisponiveis.add(LocalTime.of(i, 0));
        }

        // Consultando o banco de dados para obter os horários ocupados.
        AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
        Collection<Agendamento> agendamentos = agendamentoDAO.lista("data = '" + date + "'");

        // Removendo os horários ocupados da lista de horários disponíveis.
        for (Agendamento agendamento : agendamentos) {
            horariosDisponiveis.remove(agendamento.getHorario());
        }

        return horariosDisponiveis;
    }

    private ObservableList<Agendamento> getHorariosDisponiveis(LocalDate date) {
        try {
            List<LocalTime> availableTimes = getHorariosDisponiveisPorData(date);
            ObservableList<Agendamento> availableAppointments = FXCollections.observableArrayList();
            for (LocalTime time : availableTimes) {
                Agendamento agendamento = new Agendamento(null, null, null, null);
                agendamento.setData(date);
                agendamento.setHorario(time);
                availableAppointments.add(agendamento);
            }
            return availableAppointments;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ObservableList<Agendamento> getAgendamentosExistentes() {
        try {
            AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
            Collection<Agendamento> agendamentos = agendamentoDAO.lista("");
            return FXCollections.observableArrayList(agendamentos);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private List<LocalTime> getHorariosAgendadosPorData(LocalDate date) throws SQLException {
        // Criando uma lista de horários ocupados.
        List<LocalTime> horariosOcupados = new ArrayList<>();

        // Consultando o banco de dados para obter os horários ocupados.
        AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
        Collection<Agendamento> agendamentos = agendamentoDAO.lista("data = '" + date + "'");

        // Adicionando os horários ocupados à lista de horários ocupados.
        for (Agendamento agendamento : agendamentos) {
            horariosOcupados.add(agendamento.getHorario());
        }

        return horariosOcupados;
    }

    private ObservableList<Agendamento> getAgendamentosPorData(LocalDate date) {
        try {
            AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
            Collection<Agendamento> agendamentos = agendamentoDAO.lista("data = '" + date + "'");
            return FXCollections.observableArrayList(agendamentos);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Agendamento carrega_Agendamento() {
        Agendamento agendamento = new Agendamento(null, null, null, null);
        agendamento.setDono(cbDonos.getValue());
        agendamento.setPet(cbPets.getValue());
        agendamento.setVeterinario(cbVeterinarios.getValue());
        agendamento.setUnidade(cbUnidade.getValue());
        agendamento.setEspecialidade(txtEspecialidade.getText());
        agendamento.setData(dpDataAgend.getValue());
        agendamento.setHorario(cbHorarios.getValue());
        return agendamento;
    }

    private void carregar_Campos(Agendamento agendamento) {
        cbDonos.setValue(agendamento.getDono());
        cbPets.setValue(agendamento.getPet());
        cbVeterinarios.setValue(agendamento.getVeterinario());
        cbUnidade.setValue(agendamento.getUnidade());
        txtEspecialidade.setText(agendamento.getEspecialidade());
        dpDataAgend.setValue(agendamento.getData());
        cbHorarios.setValue(agendamento.getHorario());
    }
}