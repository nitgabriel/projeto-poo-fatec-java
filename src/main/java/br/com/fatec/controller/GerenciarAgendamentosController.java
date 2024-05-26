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
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import br.com.fatec.DAO.*;
import br.com.fatec.exceptions.AgendamentoInvalidoException;
import br.com.fatec.exceptions.InvalidDateException;
import br.com.fatec.exceptions.InvalidOwnerException;
import br.com.fatec.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
    private ComboBox<Agendamento> cbAgendamentos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        controls = Arrays.asList(txtEspecialidade, dpDataAgend, cbDonos, cbPets, cbUnidade, cbHorarios, cbVeterinarios);
        dpDataAgend.valueProperty().addListener((observable, oldValue, newValue) -> {
            //  Criando uma lista de horários disponíveis.
            List<LocalTime> horariosDisponiveis = new ArrayList<>();
            for (int i = 10; i <= 16; i++) {
                horariosDisponiveis.add(LocalTime.of(i, 0));
            }

            // Consultando o banco de dados para obter os horários ocupados.
            AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
            Collection<Agendamento> agendamentos = null;
            if (newValue != null) {
                try {
                    agendamentos = agendamentoDAO.lista("data = '" + newValue + "'");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }


                // Removendo os horários ocupados da lista de horários disponíveis.
                for (Agendamento agendamento : agendamentos) {
                    horariosDisponiveis.remove(agendamento.getHorario());
                }

                // Adiciona os horários disponíveis ao ComboBox.
                ObservableList<LocalTime> horarios = FXCollections.observableArrayList(horariosDisponiveis);
                cbHorarios.setItems(horarios);
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

            atualizarAgendamentos();

            cbAgendamentos.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
                if (newValue != null) {
                    Agendamento selectedAgendamento = newValue;
                    cbDonos.setValue(selectedAgendamento.getDono());
                    cbPets.setValue(selectedAgendamento.getPet());
                    cbVeterinarios.setValue(selectedAgendamento.getVeterinario());
                    cbUnidade.setValue(selectedAgendamento.getUnidade());
                    txtEspecialidade.setText(selectedAgendamento.getEspecialidade());
                    dpDataAgend.setValue(selectedAgendamento.getData());
                    cbHorarios.setValue(selectedAgendamento.getHorario());
                } else {
                    controls.forEach(control -> {
                        if (control instanceof TextField) {
                            ((TextField) control).clear();
                        } else if (control instanceof ComboBox) {
                            ((ComboBox<?>) control).setValue(null);
                        } else if (control instanceof DatePicker) {
                            ((DatePicker) control).setValue(null);
                        }
                    });
                }
            });


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
    }

    @FXML
    private void btnRegistrarAgendamento_Click(ActionEvent event) {
        if(areFieldsEmpty(controls)) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA TODOS OS CAMPOS ANTES DE CADASTRAR UM MEDICAMENTO.");
        } else {
            try {
                validateDate(dpDataAgend.getValue());
                validateOwner(cbPets.getValue(), cbDonos.getValue());
                validateAgendamentoSelection(cbAgendamentos.getValue());

                Agendamento agendamento = new Agendamento(cbDonos.getValue(), cbPets.getValue(), cbVeterinarios.getValue(), cbUnidade.getValue());
                agendamento.setEspecialidade(txtEspecialidade.getText());
                agendamento.setData(dpDataAgend.getValue());
                agendamento.setHorario(cbHorarios.getValue());

                AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
                if (agendamentoDAO.insere(agendamento)) {
                    showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "AGENDAMENTO REGISTRADO COM SUCESSO.");
                    controls.forEach(control -> {
                        if (control instanceof TextField) {
                            ((TextField) control).clear();
                        } else if (control instanceof ComboBox) {
                            ((ComboBox<?>) control).setValue(null);
                        } else if (control instanceof DatePicker) {
                            ((DatePicker) control).setValue(null);
                        }
                        atualizarAgendamentos();
                    });
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO REGISTRAR AGENDAMENTO.");
                }
            } catch (InvalidDateException | InvalidOwnerException | SQLException | AgendamentoInvalidoException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO REGISTRAR AGENDAMENTO:\n" + e.getMessage());
            }
        }
    }

    @FXML
    private void btnAlterarAgendamento_Click(ActionEvent event) {
        if(areFieldsEmpty(controls)) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA TODOS OS CAMPOS ANTES DE ALTERAR UM AGENDAMENTO.");
        } else {
            if( cbAgendamentos.getValue() != null) {
                try {
                    validateDate(dpDataAgend.getValue());
                    validateOwner(cbPets.getValue(), cbDonos.getValue());

                    Agendamento selectedAgendamento = cbAgendamentos.getValue();

                    if (selectedAgendamento != null) {
                        selectedAgendamento.setDono(cbDonos.getValue());
                        selectedAgendamento.setPet(cbPets.getValue());
                        selectedAgendamento.setVeterinario(cbVeterinarios.getValue());
                        selectedAgendamento.setUnidade(cbUnidade.getValue());
                        selectedAgendamento.setEspecialidade(txtEspecialidade.getText());
                        selectedAgendamento.setData(dpDataAgend.getValue());
                        selectedAgendamento.setHorario(cbHorarios.getValue());

                        AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
                        if (agendamentoDAO.altera(selectedAgendamento)) {
                            showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "AGENDAMENTO ALTERADO COM SUCESSO.");
                            controls.forEach(control -> {
                                if (control instanceof TextField) {
                                    ((TextField) control).clear();
                                } else if (control instanceof ComboBox) {
                                    ((ComboBox<?>) control).setValue(null);
                                } else if (control instanceof DatePicker) {
                                    ((DatePicker) control).setValue(null);
                                }
                                atualizarAgendamentos();
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
            } else {
                showAlert(Alert.AlertType.WARNING, "AVISO", "SELECIONE UM AGENDAMENTO PARA ALTERAR.");
            }
        }
    }

    @FXML
    private void btnExcluirAgendamento_Click(ActionEvent event) {
        if(cbAgendamentos.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "SELECIONE UM AGENDAMENTO PARA EXCLUIR.");
        } else {
            try {
                AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
                Agendamento agendamento = cbAgendamentos.getValue();

                if (agendamento != null) {
                    if (agendamentoDAO.remove(agendamento)) {
                        showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "AGENDAMENTO EXCLUÍDO COM SUCESSO.");
                        controls.forEach(control -> {
                            if (control instanceof TextField) {
                                ((TextField) control).clear();
                            } else if (control instanceof ComboBox) {
                                ((ComboBox<?>) control).setValue(null);
                            } else if (control instanceof DatePicker) {
                                ((DatePicker) control).setValue(null);
                            }
                            atualizarAgendamentos();
                        });
                    } else {
                        showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO EXCLUIR AGENDAMENTO.\n");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "NÃO FOI POSSÍVEL EXCLUIR.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO EXCLUIR AGENDAMENTO: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnConsultarAgendamento_Click(ActionEvent event) {
        Agendamento selectedAgendamento = cbAgendamentos.getValue();
        if (selectedAgendamento != null) {
            cbDonos.setValue(selectedAgendamento.getDono());
            cbPets.setValue(selectedAgendamento.getPet());
            cbVeterinarios.setValue(selectedAgendamento.getVeterinario());
            cbUnidade.setValue(selectedAgendamento.getUnidade());
            txtEspecialidade.setText(selectedAgendamento.getEspecialidade());
            dpDataAgend.setValue(selectedAgendamento.getData());
            cbHorarios.setValue(selectedAgendamento.getHorario());
        } else {
            showAlert(Alert.AlertType.WARNING, "AVISO", "SELECIONE UM AGENDAMENTO PARA CONSULTAR.");
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

    public static void validateAgendamentoSelection(Agendamento agendamento) throws AgendamentoInvalidoException {
        if (agendamento != null) {
            throw new AgendamentoInvalidoException("Desselecione o agendamento selecionado para registrar um novo agendamento.");
        }
    }

    private void atualizarAgendamentos() {
        AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
        try {
            Collection<Agendamento> agendamentos = agendamentoDAO.lista("");
            List<Agendamento> agendamentosList = new ArrayList<>();
            agendamentosList.add(null); // Adiciona um item "null" à lista
            agendamentosList.addAll(agendamentos); // Adiciona todos os agendamentos após o "null"
            cbAgendamentos.setItems(FXCollections.observableArrayList(agendamentosList));
        } catch (SQLException e) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO LOCALIZAR OS AGENDAMENTOS.");
        }
    }

}
