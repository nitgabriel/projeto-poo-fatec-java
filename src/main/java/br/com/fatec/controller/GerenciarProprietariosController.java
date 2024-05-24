/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.fatec.controller;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnRegistrarProprietario_Click(ActionEvent event) {
    }

    @FXML
    private void btnAlterarProprietario_Click(ActionEvent event) {
    }

    @FXML
    private void btnExcluirProprietario_Click(ActionEvent event) {
    }

    @FXML
    private void btnConsultarProprietario_Click(ActionEvent event) {
    }
    
}
