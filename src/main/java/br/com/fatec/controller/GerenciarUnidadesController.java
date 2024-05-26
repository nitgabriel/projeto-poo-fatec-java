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
    private TextField txtUF;
    @FXML
    private TextField txtNumero;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnRegistrarUnidade_Click(ActionEvent event) {
    }

    @FXML
    private void btnAlterarUnidade_Click(ActionEvent event) {
    }

    @FXML
    private void btnExcluirUnidade_Click(ActionEvent event) {
    }

    @FXML
    private void btnConsultarUnidade_Click(ActionEvent event) {
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
