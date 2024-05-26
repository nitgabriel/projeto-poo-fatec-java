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
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

import br.com.fatec.DAO.UnidadeDAO;
import br.com.fatec.model.Unidade;
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
public class GerenciarUnidadesController implements Initializable {

    @FXML
    private Button btnRegistrarUnidade;
    @FXML
    private Button btnAlterarUnidade;
    @FXML
    private Button btnExcluirUnidade;
    @FXML
    private Button btnConsultarUnidade;
    @FXML
    private Label lblMenu;
    @FXML
    private TextField txtCidade;
    @FXML
    private TextField txtUnidade;
    @FXML
    private TextField txtCep;
    @FXML
    private TextField txtBairro;
    @FXML
    private TextField txtRua;
    @FXML
    private ComboBox<String> cbUF;
    @FXML
    private TextField txtNumero;

    private UnidadeDAO unidadeDAO = new UnidadeDAO();

    private List<TextField> fields;

    private int nextId;
    @FXML
    private TextField txtNome;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        fields = Arrays.asList(txtCidade, txtUnidade, txtCep, txtBairro, txtRua, txtNumero, txtNome);
        try {
            setNextAvailableId();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // FILTRO PARA VALIDAR MUDANÇAS NOS CAMPOS DE NÚMEROS
        // TAMBÉM VALIDA O TAMANHO MÀXIMO PARA CEP
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getControlNewText();

            if (text.matches("\\d*") && text.length() <= 8) {
                return change;
            }

            return null;
        };
        txtUnidade.setTextFormatter(new TextFormatter<String>(filter));
        txtCep.setTextFormatter(new TextFormatter<String>(filter));
        cbUF.getItems().addAll("AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO", "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI", "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO");

    }

    private void setNextAvailableId() throws SQLException {
        nextId = unidadeDAO.getNextId();
        txtUnidade.setText(String.valueOf(nextId));
    }

    @FXML
    private void btnRegistrarUnidade_Click(ActionEvent event) {
        if (fields.stream().anyMatch(field -> field.getText().isEmpty()) || cbUF.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA TODOS OS CAMPOS ANTES DE TENTAR INSERIR DADOS NO SISTEMA.");
        } else {
            Unidade unidade = new Unidade();
            unidade.setIdUnidade(Integer.parseInt(txtUnidade.getText()));
            unidade.setCidade(txtCidade.getText());
            unidade.setCep(Integer.parseInt(txtCep.getText()));
            unidade.setBairro(txtBairro.getText());
            unidade.setRua(txtRua.getText());
            unidade.setUf(cbUF.getValue());
            unidade.setNumero(txtNumero.getText());
            unidade.setNome(txtNome.getText());
            try {
                if(unidadeDAO.insere(unidade)) {
                    showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "DADOS INSERIDOS COM SUCESSO.");
                    setNextAvailableId(); // Atualiza o próximo ID disponível
                    fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                    setNextAvailableId();
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO INSERIR DADOS.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO INSERIR DADOS: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnAlterarUnidade_Click(ActionEvent event) {
        if (fields.stream().anyMatch(field -> field.getText().isEmpty()) || cbUF.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA TODOS OS CAMPOS ANTES DE TENTAR ALTERAR DADOS NO SISTEMA.");
        } else {
            Unidade unidade = new Unidade();
            unidade.setIdUnidade(Integer.parseInt(txtUnidade.getText()));
            unidade.setCidade(txtCidade.getText());
            unidade.setCep(Integer.parseInt(txtCep.getText()));
            unidade.setBairro(txtBairro.getText());
            unidade.setRua(txtRua.getText());
            unidade.setUf(cbUF.getValue());
            unidade.setNumero(txtNumero.getText());
            unidade.setNome(txtNome.getText());
            try {
                if(unidadeDAO.altera(unidade)) {
                    showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "DADOS ALTERADOS COM SUCESSO.");
                    setNextAvailableId(); // Atualiza o próximo ID disponível
                    fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                    setNextAvailableId();
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO ALTERAR DADOS.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO ALTERAR DADOS: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnExcluirUnidade_Click(ActionEvent event) {
        if (txtUnidade.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA O ID DA UNIDADE PARA EXCLUIR OS DADOS.");
        } else {
            Unidade unidade = new Unidade();
            unidade.setIdUnidade(Integer.parseInt(txtUnidade.getText()));
            try {
                if(unidadeDAO.remove(unidade)) {
                    showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "DADOS REMOVIDOS COM SUCESSO.");
                    setNextAvailableId(); // Atualiza o próximo ID disponível
                    fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                    setNextAvailableId();
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "ID NÃO ENCONTRADO.\nNÃO FOI POSSÍVEL REMOVER OS DADOS.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO EXCLUIR DADOS: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnConsultarUnidade_Click(ActionEvent event) {
        if (txtUnidade.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA O ID DA UNIDADE PARA CONSULTAR OS DADOS.");
        } else {
            Unidade unidade = new Unidade();
            unidade.setIdUnidade(Integer.parseInt(txtUnidade.getText()));
            try {
                unidade = unidadeDAO.buscaID(unidade);
                if (unidade != null) {
                    txtCidade.setText(unidade.getCidade());
                    txtCep.setText(String.valueOf(unidade.getCep()));
                    txtBairro.setText(unidade.getBairro());
                    txtRua.setText(unidade.getRua());
                    cbUF.setValue(unidade.getUf());
                    txtNumero.setText(unidade.getNumero());
                    txtNome.setText(unidade.getNome());
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "ID DA UNIDADE NÃO ENCONTRADO.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO CONSULTAR DADOS: " + e.getMessage());
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
