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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    @FXML
    private void lblMenu_Click(MouseEvent event) {
        try {
            App.setRoot("view/Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
