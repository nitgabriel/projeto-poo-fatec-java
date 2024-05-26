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

        fields = Arrays.asList(txtNomeProprietario, txtCpfProprietario, txtContatoProprietario, txtEmailProprietario, txtPagamentoProprietario);

    }

    @FXML
    private void btnRegistrarProprietario_Click(ActionEvent event) throws SQLException {
        if (fields.stream().anyMatch(field -> field.getText().isEmpty())) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA TODOS OS CAMPOS ANTES DE TENTAR INSERIR DADOS NO SISTEMA.");
        } else {
            Dono dono = new Dono();
            dono.setNome(txtNomeProprietario.getText());
            dono.setCpf(txtCpfProprietario.getText());
            dono.setContato(txtContatoProprietario.getText());
            dono.setEmail(txtEmailProprietario.getText());
            dono.setFormaPagamento(txtPagamentoProprietario.getText());
            if(donoDAO.insere(dono)) {
                showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "DADOS INSERIDOS COM SUCESSO.");
                fields.forEach(field -> field.clear());
            } else {
                showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO INSERIR DADOS.");
            }
        }
    }

    @FXML
    private void btnAlterarProprietario_Click(ActionEvent event) throws SQLException {
        if (fields.stream().anyMatch(field -> field.getText().isEmpty())) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA TODOS OS CAMPOS ANTES DE TENTAR ALTERAR DADOS NO SISTEMA.");
        } else {
            Dono dono = new Dono();
            dono.setCpf(txtCpfProprietario.getText());
            dono = donoDAO.buscaCPF(dono);
            if (dono != null) {
                dono.setNome(txtNomeProprietario.getText());
                dono.setCpf(txtCpfProprietario.getText());
                dono.setContato(txtContatoProprietario.getText());
                dono.setEmail(txtEmailProprietario.getText());
                dono.setFormaPagamento(txtPagamentoProprietario.getText());

                if(donoDAO.altera(dono)) {
                    showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "DADOS ALTERADOS COM SUCESSO.");
                    fields.forEach(field -> field.clear());
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "NÃO FOI POSSÍVEL ALTERAR OS DADOS.");
                }

            } else {
                showAlert(Alert.AlertType.WARNING, "AVISO", "CPF NÃO ENCONTRADO.");
            }
        }
    }

    @FXML
    private void btnExcluirProprietario_Click(ActionEvent event) throws SQLException {
        if (txtCpfProprietario.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA O CPF PARA EXCLUIR OS DADOS DO DONO.");
        } else {
            Dono dono = new Dono();
            dono.setCpf(txtCpfProprietario.getText());
            dono = donoDAO.buscaCPF(dono);
            if (dono != null)  {
                if(donoDAO.remove(dono)) {
                    showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "DADOS REMOVIDOS COM SUCESSO.");
                    fields.forEach(field -> field.clear());
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "NÃO FOI POSSÍVEL EXCLUIR OS DADOS.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "AVISO", "CPF NÃO ENCONTRADO.");
            }
        }
    }

    @FXML
    private void btnConsultarProprietario_Click(ActionEvent event) throws SQLException {
        if (txtCpfProprietario.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA O CPF PARA CONSULTAR OS DADOS DO DONO.");
        } else {
            Dono dono = new Dono();
            dono.setCpf(txtCpfProprietario.getText());
            dono = donoDAO.buscaCPF(dono);
            if (dono != null) {
                txtNomeProprietario.setText(dono.getNome());
                txtCpfProprietario.setText(dono.getCpf());
                txtContatoProprietario.setText(dono.getContato());
                txtEmailProprietario.setText(dono.getEmail());
                txtPagamentoProprietario.setText(dono.getFormaPagamento());
            } else {
                showAlert(Alert.AlertType.WARNING, "AVISO", "CPF NÃO ENCONTRADO.");
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
    
}
