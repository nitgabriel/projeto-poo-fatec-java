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
        btnAlterarUnidade.setDisable(true);
        btnExcluirUnidade.setDisable(true);
    }

    private void setNextAvailableId() throws SQLException {
        nextId = unidadeDAO.getNextId();
        txtUnidade.setPromptText(String.valueOf(nextId));
        txtUnidade.setText(""); // Limpar o campo de texto
    }

    private Unidade carregar_Unidade() {
        Unidade unidade = new Unidade();
        unidade.setIdUnidade(Integer.parseInt(txtUnidade.getText()));
        unidade.setNome(txtNome.getText());
        unidade.setCep(Integer.parseInt(txtCep.getText()));
        unidade.setRua(txtRua.getText());
        unidade.setBairro(txtBairro.getText());
        unidade.setCidade(txtCidade.getText());
        unidade.setUf(cbUF.getValue());
        unidade.setNumero(txtNumero.getText());
        return unidade;
    }

    private void carregar_Campos(Unidade unidade) {
        txtUnidade.setText(String.valueOf(unidade.getIdUnidade()));
        txtNome.setText(unidade.getNome());
        txtCep.setText(String.valueOf(unidade.getCep()));
        txtRua.setText(unidade.getRua());
        txtBairro.setText(unidade.getBairro());
        txtCidade.setText(unidade.getCidade());
        cbUF.setValue(unidade.getUf());
        txtNumero.setText(unidade.getNumero());
    }

    @FXML
    private void btnRegistrarUnidade_Click(ActionEvent event) {
        if (fields.stream().anyMatch(field -> field.getText().isEmpty()) || cbUF.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA TODOS OS CAMPOS ANTES DE TENTAR INSERIR DADOS NO SISTEMA.");
        } else {
            Unidade unidade = carregar_Unidade();
            try {
                if (unidadeDAO.insere(unidade)) {
                    showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "UNIDADE REGISTRADA COM SUCESSO.");
                    fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                    cbUF.setValue(null); // Limpa o ComboBox
                    setNextAvailableId();
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO REGISTRAR UNIDADE.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO REGISTRAR UNIDADE: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnAlterarUnidade_Click(ActionEvent event) {
        if (fields.stream().anyMatch(field -> field.getText().isEmpty()) || cbUF.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA TODOS OS CAMPOS ANTES DE TENTAR ALTERAR DADOS NO SISTEMA.");
        } else {
            try {
                Unidade unidade = new Unidade();
                unidade.setIdUnidade(Integer.parseInt(txtUnidade.getText()));
                unidade = unidadeDAO.buscaID(unidade);
                if (unidade != null) {
                    unidade = carregar_Unidade(); // Atualiza os dados da unidade com os valores dos campos
                    if (unidadeDAO.altera(unidade)) {
                        showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "UNIDADE ALTERADA COM SUCESSO.");
                        fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                        cbUF.setValue(null); // Limpa o ComboBox
                        setNextAvailableId();
                        btnAlterarUnidade.setDisable(true); // Bloqueando botões para solicitar nova consulta
                        btnExcluirUnidade.setDisable(true);
                    } else {
                        showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO ALTERAR UNIDADE.");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "UNIDADE COM ID INFORMADO NÃO ENCONTRADA.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO ALTERAR UNIDADE: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnExcluirUnidade_Click(ActionEvent event) {
        if (txtUnidade.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA O ID DA UNIDADE PARA EXCLUIR OS DADOS.");
        } else {
            try {
                Unidade unidade = new Unidade();
                unidade.setIdUnidade(Integer.parseInt(txtUnidade.getText()));
                unidade = unidadeDAO.buscaID(unidade);
                if (unidade != null) {
                    if (unidadeDAO.remove(unidade)) {
                        showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "UNIDADE REMOVIDA COM SUCESSO.");
                        fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                        cbUF.setValue(null); // Limpa o ComboBox
                        btnAlterarUnidade.setDisable(true); // Bloqueando botões para solicitar nova consulta
                        btnExcluirUnidade.setDisable(true);
                    } else {
                        showAlert(Alert.AlertType.WARNING, "AVISO", "NÃO FOI POSSÍVEL REMOVER A UNIDADE.");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "UNIDADE COM ID INFORMADO NÃO ENCONTRADA.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO REMOVER UNIDADE: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnConsultarUnidade_Click(ActionEvent event) {
        if (txtUnidade.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA O ID DA UNIDADE PARA CONSULTAR OS DADOS.");
        } else {
            try {
                Unidade unidade = new Unidade();
                unidade.setIdUnidade(Integer.parseInt(txtUnidade.getText()));
                unidade = unidadeDAO.buscaID(unidade);
                if (unidade != null) {
                    carregar_Campos(unidade);
                    // Habilita os botões após a consulta bem-sucedida
                    btnAlterarUnidade.setDisable(false);
                    btnExcluirUnidade.setDisable(false);
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "UNIDADE COM ID INFORMADO NÃO ENCONTRADA.");
                    // Desabilita os botões se a consulta falhar
                    btnAlterarUnidade.setDisable(true);
                    btnExcluirUnidade.setDisable(true);
                    // Define o objeto unidade como null se a consulta falhar
                    unidade = null;
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO CONSULTAR UNIDADE: " + e.getMessage());
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
