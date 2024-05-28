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
    @FXML
    private TextField txtIdPet;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fields = Arrays.asList(txtNomePet, txtEspeciePet, txtRacaPet, txtNumeroConveniado, txtIdPet);
        DonoDAO donoDAO = new DonoDAO();
        try {
            Collection<Dono> donos = donoDAO.lista("");
            ObservableList<Dono> observableDonos = FXCollections.observableArrayList(donos);
            cbDonos.setItems(observableDonos);
            setNextAvailableId();
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

        btnAlterarPet.setDisable(true);
        btnExcluirPet.setDisable(true);
    }

    @FXML
    private void btnRegistrarPet_Click(ActionEvent event) {
        if (fields.stream().anyMatch(field -> field.getText().isEmpty()) || cbDonos.getValue() == null || datePickerNascimento.getValue() == null) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA TODOS OS CAMPOS ANTES DE TENTAR INSERIR DADOS NO SISTEMA.");
        } else {
            Pet pet = carregar_Pet();
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
                Pet pet = new Pet(null);
                pet.setId(Integer.parseInt(txtIdPet.getText()));
                pet = petDAO.buscaID(pet);
                if (pet != null) {
                    pet = carregar_Pet(); // Atualiza os dados do pet com os valores dos campos
                    if (petDAO.altera(pet)) {
                        showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "PET ALTERADO COM SUCESSO.");
                        fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                        datePickerNascimento.setValue(null); // Limpa o DatePicker
                        cbDonos.setValue(null); // Limpa o ComboBox
                        setNextAvailableId();
                        btnAlterarPet.setDisable(true); // Bloqueando botões para solicitar nova consulta
                        btnExcluirPet.setDisable(true);
                    } else {
                        showAlert(Alert.AlertType.WARNING, "AVISO", "ERRO AO ALTERAR PET.");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "PET COM ID INFORMADO NÃO ENCONTRADO.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO ALTERAR PET: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnExcluirPet_Click(ActionEvent event) {
        if (txtIdPet.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA O ID DO PET PARA EXCLUIR OS DADOS.");
        } else {
            try {
                Pet pet = new Pet(null);
                pet.setId(Integer.parseInt(txtIdPet.getText()));
                pet = petDAO.buscaID(pet);
                if (pet != null) {
                    if (petDAO.remove(pet)) {
                        showAlert(Alert.AlertType.INFORMATION, "INFORMAÇÃO", "PET REMOVIDO COM SUCESSO.");
                        fields.forEach(field -> field.clear()); // Limpa os campos para a próxima entrada
                        datePickerNascimento.setValue(null); // Limpa o DatePicker
                        cbDonos.setValue(null); // Limpa o ComboBox
                        btnAlterarPet.setDisable(true); // Bloqueando botões para solicitar nova consulta
                        btnExcluirPet.setDisable(true);
                    } else {
                        showAlert(Alert.AlertType.WARNING, "AVISO", "NÃO FOI POSSÍVEL REMOVER O PET.");
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "PET COM ID INFORMADO NÃO ENCONTRADO.");
                }
            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "ERRO", "ERRO AO REMOVER PET: " + e.getMessage());
            }
        }
    }

    @FXML
    private void btnConsultarPet_Click(ActionEvent event) {
        if (txtIdPet.getText().isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "AVISO", "PREENCHA O ID DO PET PARA CONSULTAR OS DADOS.");
        } else {
            try {
                Pet pet = new Pet(null);
                pet.setId(Integer.parseInt(txtIdPet.getText()));
                pet = petDAO.buscaID(pet);
                if (pet != null) {
                    carregar_Campos(pet);
                    // Habilita os botões após a consulta bem-sucedida
                    btnAlterarPet.setDisable(false);
                    btnExcluirPet.setDisable(false);
                } else {
                    showAlert(Alert.AlertType.WARNING, "AVISO", "PET COM ID INFORMADO NÃO ENCONTRADO.");
                    // Desabilita os botões se a consulta falhar
                    btnAlterarPet.setDisable(true);
                    btnExcluirPet.setDisable(true);
                    // Define o objeto pet como null se a consulta falhar
                    pet = null;
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

    private Pet carregar_Pet() {
        Pet pet = new Pet(null);
        pet.setId(Integer.parseInt(txtIdPet.getText()));
        pet.setNome(txtNomePet.getText());
        pet.setEspecie(txtEspeciePet.getText());
        pet.setRaca(txtRacaPet.getText());
        pet.setNumeroConveniado(txtNumeroConveniado.getText());
        pet.setDataNascimento(datePickerNascimento.getValue());
        pet.setDono(cbDonos.getValue());

        return pet;
    }

    private void carregar_Campos(Pet pet) {
        txtIdPet.setText(String.valueOf(pet.getId()));
        txtNomePet.setText(pet.getNome());
        txtEspeciePet.setText(pet.getEspecie());
        txtRacaPet.setText(pet.getRaca());
        txtNumeroConveniado.setText(pet.getNumeroConveniado());
        datePickerNascimento.setValue(pet.getDataNascimento());
        cbDonos.setValue(pet.getDono());
    }

    private void setNextAvailableId() throws SQLException {
        int nextId = petDAO.getNextId();
        txtIdPet.setPromptText(String.valueOf(nextId));
    }

}
