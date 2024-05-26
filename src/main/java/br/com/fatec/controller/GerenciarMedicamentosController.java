/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.fatec.controller;

import br.com.fatec.App;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.time.LocalDate;

import br.com.fatec.model.Medicamento;
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
public class GerenciarMedicamentosController implements Initializable {

    @FXML
    private AnchorPane gerenciarMedicamentos;
    @FXML
    private TextField txtNomeMedicamento;
    @FXML
    private TextField txtDosagem;
    @FXML
    private TextField txtPreco;
    @FXML
    private TextField txtDescricao;
    @FXML
    private DatePicker datePickerValidade;
    @FXML
    private Button btnRegistrarMedicamentos;
    @FXML
    private Button btnAlterarMedicamentos;
    @FXML
    private Button btnExcluirMedicamentos;
    @FXML
    private Button btnConsultarMedicamentos;
    @FXML
    private Label lblMenu;

    private static List<Medicamento> medicamentoList = new ArrayList<>();

    static {
        medicamentoList.add(new Medicamento("Benazepril", "2,5mg/kg", 10.0, LocalDate.now().plusMonths(1), "Indicado para Hipertensão e outros."));
        medicamentoList.add(new Medicamento("Pimobendana", "0,3mg/kg", 20.0, LocalDate.now().plusMonths(1), "É indicado para o tratamento dos sinais leves, moderados ou severos de ICC em cães."));
        medicamentoList.add(new Medicamento("Furosemida", "2mg/kg", 30.0, LocalDate.now().plusMonths(1), "Sua principal ação é no segmento ascendente da alça de Henle"));
    }

    private List<TextField> fields;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        fields = Arrays.asList(txtNomeMedicamento, txtDosagem, txtPreco, txtDescricao);

    }

    @FXML
    private void btnRegistrarMedicamentos_Click(ActionEvent event) {
        if(fields.stream().anyMatch( field -> field.getText().isEmpty() || datePickerValidade.getValue() == null)) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA TODOS OS CAMPOS ANTES DE CADASTRAR UM MEDICAMENTO.");
        } else {
            Medicamento medicamento = new Medicamento(
                    txtNomeMedicamento.getText(),
                    txtDosagem.getText(),
                    Double.parseDouble(txtPreco.getText()),
                    datePickerValidade.getValue(),
                    txtDescricao.getText()
            );
            medicamentoList.add(medicamento);
            fields.forEach(field -> field.clear());
            datePickerValidade.setValue(null);
            showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "MEDICAMENTO CADASTRADO COM SUCESSO.");
        }
    }

    @FXML
    private void btnAlterarMedicamentos_Click(ActionEvent event) {
        for (Medicamento medicamento : medicamentoList) {
            if (medicamento.getNome().equalsIgnoreCase(txtNomeMedicamento.getText())) {
                medicamento.setDosagem(txtDosagem.getText());
                medicamento.setPreco(Double.parseDouble(txtPreco.getText()));
                medicamento.setValidade(datePickerValidade.getValue());
                medicamento.setDescricao(txtDescricao.getText());
                fields.forEach(field -> field.clear());
                datePickerValidade.setValue(null);
                showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "MEDICAMENTO ALTERADO COM SUCESSO.");
                return;
            }
        }
        showAlert(Alert.AlertType.WARNING, "AVISO", "MEDICAMENTO NÃO ENCONTRADO.");

    }

    @FXML
    private void btnExcluirMedicamentos_Click(ActionEvent event) {
        boolean removed = medicamentoList.removeIf(medicamento -> medicamento.getNome().equalsIgnoreCase(txtNomeMedicamento.getText()));
        if (removed) {
            fields.forEach(field -> field.clear());
            datePickerValidade.setValue(null);
            showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "MEDICAMENTO EXCLUÍDO COM SUCESSO.");
        } else {
            showAlert(Alert.AlertType.WARNING, "AVISO", "Medicamento não encontrado.");
        }
    }

    @FXML
    private void btnConsultarMedicamentos_Click(ActionEvent event) {
        for (Medicamento medicamento : medicamentoList) {
            if (medicamento.getNome().equalsIgnoreCase(txtNomeMedicamento.getText())) {
                txtDosagem.setText(medicamento.getDosagem());
                txtPreco.setText(String.valueOf(medicamento.getPreco()));
                datePickerValidade.setValue(medicamento.getValidade());
                txtDescricao.setText(medicamento.getDescricao());
                return;
            }
        }
        showAlert(Alert.AlertType.WARNING, "AVISO", "Medicamento não encontrado.");
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

    public static List<Medicamento> getMedicamentoList() {
        return medicamentoList;
    }
    
}
