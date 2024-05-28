/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.fatec.controller;

import br.com.fatec.App;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import br.com.fatec.DAO.UnidadeDAO;
import br.com.fatec.DAO.VeterinarioDAO;
import br.com.fatec.model.Unidade;
import br.com.fatec.model.Veterinario;
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
public class GerenciarVeterinariosController implements Initializable {

    @FXML
    private TextField txtNomeVeterinario;
    @FXML
    private Button btnRegistrarVeterinario;
    @FXML
    private Button btnAlterarVeterinario;
    @FXML
    private Button btnExcluirVeterinario;
    @FXML
    private Button btnConsultarVeterinario;
    @FXML
    private ComboBox<Unidade> cbUnidadeVet;
    @FXML
    private TextField txtEspecialidade;
    @FXML
    private TextField txtCrmv;
    @FXML
    private TextField txtStatus;
    @FXML
    private Label lblMenu;

    private List<TextField> fields;

    private ObservableList<Unidade> unidadesList = FXCollections.observableArrayList();
    @FXML
    private TextField txtIdVeterinario;

    private VeterinarioDAO vetDAO = new VeterinarioDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fields = Arrays.asList(txtIdVeterinario, txtCrmv, txtEspecialidade, txtStatus, txtNomeVeterinario);
        UnidadeDAO unidadeDAO = new UnidadeDAO();
        try{
            Collection<Unidade> unidades = unidadeDAO.lista("");
            unidadesList.addAll(unidades);
            cbUnidadeVet.setItems(unidadesList);
            setNextAvailableId();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO LOCALIZAR AS UNIDADES.");
        }
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getControlNewText();

            if (text.matches("\\d*") && text.length() <= 13) {
                return change;
            }

            return null;
        };
        txtCrmv.setTextFormatter(new TextFormatter<String>(filter));
        btnAlterarVeterinario.setDisable(true);
        btnExcluirVeterinario.setDisable(true);

    }

    @FXML
    private void btnRegistrarVeterinario_Click(ActionEvent event) {
        if (fields.stream().anyMatch(field -> field.getText().isEmpty()) || cbUnidadeVet.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA TODOS OS CAMPOS ANTES DE TENTAR INSERIR DADOS NO SISTEMA.");
        } else {
            Veterinario veterinario = carregar_Veterinario();
            try {
                if (vetDAO.insere(veterinario)) {
                    showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "VETERINÁRIO REGISTRADO COM SUCESSO.");
                    fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                    cbUnidadeVet.setValue(null); // Limpa o ComboBox
                    setNextAvailableId();
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO REGISTRAR VETERINÁRIO.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO REGISTRAR VETERINÁRIO: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnAlterarVeterinario_Click(ActionEvent event) {
        if (fields.stream().anyMatch(field -> field.getText().isEmpty()) || cbUnidadeVet.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA TODOS OS CAMPOS ANTES DE TENTAR ALTERAR DADOS NO SISTEMA.");
        } else {
            try {
                Veterinario veterinario = new Veterinario(null);
                veterinario.setIdVeterinario(Integer.parseInt(txtIdVeterinario.getText()));
                veterinario = vetDAO.buscaID(veterinario);
                if (veterinario != null) {
                    veterinario = carregar_Veterinario(); // Atualiza os dados do veterinario com os valores dos campos
                    if (vetDAO.altera(veterinario)) {
                        showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "VETERINÁRIO ALTERADO COM SUCESSO.");
                        fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                        cbUnidadeVet.setValue(null); // Limpa o ComboBox
                        setNextAvailableId();
                        btnAlterarVeterinario.setDisable(true); // Bloqueando botões para solicitar nova consulta
                        btnExcluirVeterinario.setDisable(true);
                    } else {
                        showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO ALTERAR VETERINÁRIO.");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "VETERINÁRIO COM ID INFORMADO NÃO ENCONTRADO.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO ALTERAR VETERINÁRIO: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnExcluirVeterinario_Click(ActionEvent event) {
        if (txtIdVeterinario.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA O ID DO VETERINÁRIO PARA EXCLUIR OS DADOS.");
        } else {
            try {
                Veterinario veterinario = new Veterinario(null);
                veterinario.setIdVeterinario(Integer.parseInt(txtIdVeterinario.getText()));
                veterinario = vetDAO.buscaID(veterinario);
                if (veterinario != null) {
                    if (vetDAO.remove(veterinario)) {
                        showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "VETERINÁRIO REMOVIDO COM SUCESSO.");
                        fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                        cbUnidadeVet.setValue(null); // Limpa o ComboBox
                        setNextAvailableId();
                        btnAlterarVeterinario.setDisable(true); // Bloqueando botões para solicitar nova consulta
                        btnExcluirVeterinario.setDisable(true);
                    } else {
                        showAlert(Alert.AlertType.WARNING, "AVISO", "NÃO FOI POSSÍVEL REMOVER O VETERINÁRIO.");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "VETERINÁRIO COM ID INFORMADO NÃO ENCONTRADO.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO REMOVER VETERINÁRIO: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnConsultarVeterinario_Click(ActionEvent event) {
        if (txtIdVeterinario.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA O ID DO VETERINÁRIO PARA CONSULTAR OS DADOS.");
        } else {
            try {
                Veterinario veterinario = new Veterinario(null);
                veterinario.setIdVeterinario(Integer.parseInt(txtIdVeterinario.getText()));
                veterinario = vetDAO.buscaID(veterinario);
                if (veterinario != null) {
                    carregar_Campos(veterinario);
                    // Habilita os botões após a consulta bem-sucedida
                    btnAlterarVeterinario.setDisable(false);
                    btnExcluirVeterinario.setDisable(false);
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "VETERINÁRIO COM ID INFORMADO NÃO ENCONTRADO.");
                    // Desabilita os botões se a consulta falhar
                    btnAlterarVeterinario.setDisable(true);
                    btnExcluirVeterinario.setDisable(true);
                    // Define o objeto veterinario como null se a consulta falhar
                    veterinario = null;
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO CONSULTAR VETERINÁRIO: " + e.getMessage());
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
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private Veterinario carregar_Veterinario() {
        Veterinario veterinario = new Veterinario(cbUnidadeVet.getValue());
        veterinario.setIdVeterinario(Integer.parseInt(txtIdVeterinario.getText()));
        veterinario.setNome(txtNomeVeterinario.getText());
        veterinario.setCrmv(txtCrmv.getText());
        veterinario.setStatus(txtStatus.getText());
        veterinario.setEspecialidade(txtEspecialidade.getText());
        return veterinario;
    }

    private void carregar_Campos(Veterinario veterinario) {
        txtIdVeterinario.setText(String.valueOf(veterinario.getIdVeterinario()));
        txtNomeVeterinario.setText(veterinario.getNome());
        txtCrmv.setText(veterinario.getCrmv());
        txtStatus.setText(veterinario.getStatus());
        txtEspecialidade.setText(veterinario.getEspecialidade());
        cbUnidadeVet.setValue(veterinario.getUnidade());
    }

    private void setNextAvailableId() throws SQLException {
        int nextId = vetDAO.getNextId();
        txtIdVeterinario.setPromptText(String.valueOf(nextId));
    }

}
