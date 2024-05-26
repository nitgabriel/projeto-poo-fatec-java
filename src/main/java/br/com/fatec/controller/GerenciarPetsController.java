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

import br.com.fatec.DAO.DonoDAO;
import br.com.fatec.DAO.PetDAO;
import br.com.fatec.model.Dono;
import br.com.fatec.model.Pet;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
public class GerenciarPetsController implements Initializable {

    @FXML
    private AnchorPane gerenciarPets;
    @FXML
    private TextField txtNomePet;
    @FXML
    private TextField txtEspeciePet;
    @FXML
    private TextField txtRacaPet;
    @FXML
    private TextField txtNumeroConveniado;
    @FXML
    private ComboBox<Dono> cbDonos;
    @FXML
    private DatePicker datePickerNascimento;
    @FXML
    private Button btnRegistrarPet;
    @FXML
    private Button btnAlterarPet;
    @FXML
    private Button btnExcluirPet;
    @FXML
    private Button btnConsultarPet;
    @FXML
    private Label lblMenu;

    private List<TextField> fields;

    private PetDAO petDAO = new PetDAO();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fields = Arrays.asList(txtNomePet, txtEspeciePet, txtRacaPet, txtNumeroConveniado);
        DonoDAO donoDAO = new DonoDAO();
        try {
            Collection<Dono> donos = donoDAO.lista("");
            ObservableList<Dono> observableDonos = FXCollections.observableArrayList(donos);
            cbDonos.setItems(observableDonos);
        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO CARREGAR DONOS: " + e.getMessage());
        }
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getControlNewText();

            if (text.matches("\\d*") && text.length() <= 13) {
                return change;
            }

            return null;
        };
        txtNumeroConveniado.setTextFormatter(new TextFormatter<String>(filter));
    }    

    @FXML
    private void btnRegistrarPet_Click(ActionEvent event) {
        if (fields.stream().anyMatch(field -> field.getText().isEmpty()) || cbDonos.getValue() == null || datePickerNascimento.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA TODOS OS CAMPOS ANTES DE TENTAR INSERIR DADOS NO SISTEMA.");
        } else {
            Pet pet = new Pet(cbDonos.getValue());
            pet.setNome(txtNomePet.getText());
            pet.setEspecie(txtEspeciePet.getText());
            pet.setRaca(txtRacaPet.getText());
            pet.setNumeroConveniado(txtNumeroConveniado.getText());
            pet.setDataNascimento(datePickerNascimento.getValue());
            try {
                if (petDAO.insere(pet)) {
                    showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "PET REGISTRADO COM SUCESSO.");
                    fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                    datePickerNascimento.setValue(null); // Limpa o DatePicker
                    cbDonos.setValue(null);
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO REGISTRAR PET.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO REGISTRAR PET: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnAlterarPet_Click(ActionEvent event) {
        if (fields.stream().anyMatch(field -> field.getText().isEmpty()) || cbDonos.getValue() == null || datePickerNascimento.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA TODOS OS CAMPOS ANTES DE TENTAR ALTERAR DADOS NO SISTEMA.");
        } else {
            try {
            Pet pet = petDAO.buscaPorNumeroConveniado(txtNumeroConveniado.getText());
                if (pet != null) {
                    pet.setNome(txtNomePet.getText());
                    pet.setEspecie(txtEspeciePet.getText());
                    pet.setRaca(txtRacaPet.getText());
                    pet.setDono(cbDonos.getValue());
                    pet.setNumeroConveniado(txtNumeroConveniado.getText());
                    pet.setDataNascimento(datePickerNascimento.getValue());

                    if (petDAO.altera(pet)) {
                        showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "PET ALTERADO COM SUCESSO.");
                        fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                        datePickerNascimento.setValue(null); // Limpa o DatePicker
                        cbDonos.setValue(null); // Limpa o ComboBox
                    } else {
                        showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO ALTERAR PET.");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "PET COM NÚMERO CONVENIADO INFORMADO NÃO ENCONTRADO.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO ALTERAR PET: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnExcluirPet_Click(ActionEvent event) {
        if (txtNumeroConveniado.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA O NÚMERO CONVENIADO DO PET PARA EXCLUIR OS DADOS.");
        } else {
            try {
                Pet pet = petDAO.buscaPorNumeroConveniado(txtNumeroConveniado.getText());
                if (pet != null) {
                    if (petDAO.remove(pet)) {
                        showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "PET REMOVIDO COM SUCESSO.");
                        fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                        datePickerNascimento.setValue(null); // Limpa o DatePicker
                        cbDonos.setValue(null); // Limpa o ComboBox
                    } else {
                        showAlert(Alert.AlertType.WARNING, "AVISO", "NÃO FOI POSSÍVEL REMOVER O PET.");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "PET COM NÚMERO CONVENIADO INFORMADO NÃO ENCONTRADO.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO REMOVER PET: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnConsultarPet_Click(ActionEvent event) {
        if (txtNumeroConveniado.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA O NÚMERO CONVENIADO DO PET PARA CONSULTAR OS DADOS.");
        } else {
            try {
                Pet pet = petDAO.buscaPorNumeroConveniado(txtNumeroConveniado.getText());
                if (pet != null) {
                    txtNomePet.setText(pet.getNome());
                    txtEspeciePet.setText(pet.getEspecie());
                    txtRacaPet.setText(pet.getRaca());
                    cbDonos.setValue(pet.getDono());
                    datePickerNascimento.setValue(pet.getDataNascimento());
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "PET COM NÚMERO CONVENIADO INFORMADO NÃO ENCONTRADO.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO CONSULTAR PET: " + e.getMessage());
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
