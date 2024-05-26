/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.fatec.controller;

import br.com.fatec.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnRegistrarMedicamentos_Click(ActionEvent event) {
    }

    @FXML
    private void btnAlterarMedicamentos_Click(ActionEvent event) {
    }

    @FXML
    private void btnExcluirMedicamentos_Click(ActionEvent event) {
    }

    @FXML
    private void btnConsultarMedicamentos_Click(ActionEvent event) {
    }

    @FXML
    private void lblMenu_Click(MouseEvent event) {
        try {
            App.setRoot("view/Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
