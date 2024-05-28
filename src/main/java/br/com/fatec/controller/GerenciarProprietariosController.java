/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.fatec.controller;

import br.com.fatec.App;
import br.com.fatec.model.Dono;
import br.com.fatec.DAO.DonoDAO;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class GerenciarProprietariosController implements Initializable {

    @FXML
    private AnchorPane gerenciarProprietarios;
    @FXML
    private Button btnRegistrarProprietario;
    @FXML
    private Button btnAlterarProprietario;
    @FXML
    private Button btnExcluirProprietario;
    @FXML
    private Button btnConsultarProprietario;
    @FXML
    private TextField txtNomeProprietario;
    @FXML
    private TextField txtCpfProprietario;
    @FXML
    private TextField txtContatoProprietario;
    @FXML
    private TextField txtEmailProprietario;
    @FXML
    private TextField txtPagamentoProprietario;
    @FXML
    private Label lblMenu;

    private DonoDAO donoDAO = new DonoDAO();

    private List<TextField> fields;
    @FXML
    private TextField txtIdDono;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getControlNewText();

            if (text.matches("\\d*") && text.length() <= 11) { // this is the important line
                return change;
            }

            return null;
        };
        txtCpfProprietario.setTextFormatter(new TextFormatter<String>(filter));
        txtContatoProprietario.setTextFormatter(new TextFormatter<String>(filter));
        // Bloqueando botões para apenas for possível realizar operações após fazer uma consulta.
        btnAlterarProprietario.setDisable(true);
        btnExcluirProprietario.setDisable(true);

        fields = Arrays.asList(txtIdDono, txtNomeProprietario, txtCpfProprietario, txtContatoProprietario, txtEmailProprietario, txtPagamentoProprietario);
        try {
            setNextAvailableId();
        } catch (SQLException e) {
            showAlert(Alert.AlertType.WARNING, "ERRO", "ERRO AO RESGATAR DADOS!" + e.getMessage());
        }

    }

    @FXML
    private void btnRegistrarProprietario_Click(ActionEvent event) {
        if (fields.stream().anyMatch(field -> field.getText().isEmpty())) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA TODOS OS CAMPOS ANTES DE TENTAR INSERIR DADOS NO SISTEMA.");
        } else {
            Dono dono = carregar_Dono();
            try {
                if (donoDAO.insere(dono)) {
                    showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "DONO REGISTRADO COM SUCESSO.");
                    fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                    setNextAvailableId();
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO REGISTRAR DONO.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO REGISTRAR DONO: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnAlterarProprietario_Click(ActionEvent event) {
        if (fields.stream().anyMatch(field -> field.getText().isEmpty())) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA TODOS OS CAMPOS ANTES DE TENTAR ALTERAR DADOS NO SISTEMA.");
        } else {
            try {
                Dono dono = new Dono();
                dono.setIdDono(Integer.parseInt(txtIdDono.getText()));
                dono = donoDAO.buscaID(dono);
                if (dono != null) {
                    dono = carregar_Dono(); // Atualiza os dados do dono com os valores dos campos
                    if (donoDAO.altera(dono)) {
                        showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "DONO ALTERADO COM SUCESSO.");
                        fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                        setNextAvailableId();
                        // Bloqueando botões para ser necessário fazer uma nova consulta para realizar as seguintes operações
                        btnAlterarProprietario.setDisable(true);
                        btnExcluirProprietario.setDisable(true);
                    } else {
                        showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO ALTERAR DONO.");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "DONO COM ID INFORMADO NÃO ENCONTRADO.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO ALTERAR DONO: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnExcluirProprietario_Click(ActionEvent event) {
        if (txtIdDono.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA O ID DO DONO PARA EXCLUIR OS DADOS.");
        } else {
            try {
                Dono dono = new Dono();
                dono.setIdDono(Integer.parseInt(txtIdDono.getText()));
                dono = donoDAO.buscaID(dono);
                if (dono != null) {
                    if (donoDAO.remove(dono)) {
                        showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "DONO REMOVIDO COM SUCESSO.");
                        fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                        setNextAvailableId();

                        // Bloqueando botões para ser necessário fazer uma nova consulta para realizar as seguintes operações
                        btnAlterarProprietario.setDisable(true);
                        btnExcluirProprietario.setDisable(true);
                    } else {
                        showAlert(Alert.AlertType.WARNING, "AVISO", "NÃO FOI POSSÍVEL REMOVER O DONO.");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "DONO COM ID INFORMADO NÃO ENCONTRADO.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO REMOVER DONO: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnConsultarProprietario_Click(ActionEvent event) {
        if (txtIdDono.getText().isEmpty()) {
            // ...
        } else {
            try {
                Dono dono = new Dono();
                dono.setIdDono(Integer.parseInt(txtIdDono.getText()));
                dono = donoDAO.buscaID(dono);
                if (dono != null) {
                    carregar_Campos(dono);
                    // Habilita os botões após a consulta bem-sucedida
                    btnAlterarProprietario.setDisable(false);
                    btnExcluirProprietario.setDisable(false);
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "DONO COM ID INFORMADO NÃO ENCONTRADO.");
                    // Desabilita os botões se a consulta falhar
                    btnAlterarProprietario.setDisable(true);
                    btnExcluirProprietario.setDisable(true);
                }
            } catch (SQLException e) {
                // ...
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

    private Dono carregar_Dono() {
        Dono dono = new Dono();
        dono.setIdDono(Integer.parseInt(txtIdDono.getText()));
        dono.setNome(txtNomeProprietario.getText());
        dono.setCpf(txtCpfProprietario.getText());
        dono.setContato(txtContatoProprietario.getText());
        dono.setEmail(txtEmailProprietario.getText());
        dono.setFormaPagamento(txtPagamentoProprietario.getText());

        return dono;
    }

    // Adicione este método na classe GerenciarProprietariosController
    private void carregar_Campos(Dono dono) {
        txtIdDono.setText(String.valueOf(dono.getIdDono()));
        txtNomeProprietario.setText(dono.getNome());
        txtCpfProprietario.setText(dono.getCpf());
        txtContatoProprietario.setText(dono.getContato());
        txtEmailProprietario.setText(dono.getEmail());
        txtPagamentoProprietario.setText(dono.getFormaPagamento());
    }

    private void setNextAvailableId() throws SQLException {
        int nextId = donoDAO.getNextId();
        txtIdDono.setPromptText(String.valueOf(nextId));
    }
    
}
