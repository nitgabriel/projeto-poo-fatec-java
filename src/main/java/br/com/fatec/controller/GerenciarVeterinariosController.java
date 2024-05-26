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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fields = Arrays.asList(txtCrmv, txtEspecialidade, txtStatus, txtNomeVeterinario);
        UnidadeDAO unidadeDAO = new UnidadeDAO();
        try{
            Collection<Unidade> unidades = unidadeDAO.lista("");
            unidadesList.addAll(unidades);
            cbUnidadeVet.setItems(unidadesList);
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
    }    

    @FXML
    private void btnRegistrarVeterinario_Click(ActionEvent event) {
        if (fields.stream().anyMatch(field -> field.getText().isEmpty()) || cbUnidadeVet.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA TODOS OS CAMPOS ANTES DE TENTAR INSERIR DADOS NO SISTEMA.");
        } else {
            Veterinario veterinario = new Veterinario(cbUnidadeVet.getValue());
            veterinario.setNome(txtNomeVeterinario.getText());
            veterinario.setCrmv(txtCrmv.getText());
            veterinario.setStatus(txtStatus.getText());
            veterinario.setEspecialidade(txtEspecialidade.getText());
            VeterinarioDAO veterinarioDAO = new VeterinarioDAO();
            try {
                if (veterinarioDAO.insere(veterinario)) {
                    showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "VETERINÁRIO REGISTRADO COM SUCESSO.");
                    fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                    cbUnidadeVet.setValue(null); // Limpa o ComboBox
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
            VeterinarioDAO veterinarioDAO = new VeterinarioDAO();
            try {
                Veterinario veterinario = veterinarioDAO.buscaPorCrmv(txtCrmv.getText());
                if (veterinario != null) {
                    veterinario.setNome(txtNomeVeterinario.getText());
                    veterinario.setUnidade(cbUnidadeVet.getValue());
                    veterinario.setStatus(txtStatus.getText());
                    veterinario.setEspecialidade(txtEspecialidade.getText());

                    if (veterinarioDAO.altera(veterinario)) {
                        showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "VETERINÁRIO ALTERADO COM SUCESSO.");
                        fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                        cbUnidadeVet.setValue(null); // Limpa o ComboBox
                    } else {
                        showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO ALTERAR VETERINÁRIO.");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "VETERINÁRIO COM CRMV INFORMADO NÃO ENCONTRADO.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO ALTERAR VETERINÁRIO: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnExcluirVeterinario_Click(ActionEvent event) {
        if (txtCrmv.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA O CRMV DO VETERINÁRIO PARA EXCLUIR OS DADOS.");
        } else {
            VeterinarioDAO veterinarioDAO = new VeterinarioDAO();
            try {
                Veterinario veterinario = veterinarioDAO.buscaPorCrmv(txtCrmv.getText());
                if (veterinario != null) {
                    if (veterinarioDAO.remove(veterinario)) {
                        showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "VETERINÁRIO REMOVIDO COM SUCESSO.");
                        fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                        cbUnidadeVet.setValue(null); // Limpa o ComboBox
                    } else {
                        showAlert(Alert.AlertType.WARNING, "AVISO", "NÃO FOI POSSÍVEL REMOVER O VETERINÁRIO.");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "VETERINÁRIO COM CRMV INFORMADO NÃO ENCONTRADO.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO REMOVER VETERINÁRIO: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnConsultarVeterinario_Click(ActionEvent event) {
        if (txtCrmv.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA O CRMV DO VETERINÁRIO PARA CONSULTAR OS DADOS.");
        } else {
            VeterinarioDAO veterinarioDAO = new VeterinarioDAO();
            try {
                Veterinario veterinario = veterinarioDAO.buscaPorCrmv(txtCrmv.getText());
                if (veterinario != null) {
                    txtNomeVeterinario.setText(veterinario.getNome());
                    txtEspecialidade.setText(veterinario.getEspecialidade());
                    txtStatus.setText(veterinario.getStatus());
                    cbUnidadeVet.setValue(veterinario.getUnidade());
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "VETERINÁRIO COM CRMV INFORMADO NÃO ENCONTRADO.");
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
    
}
