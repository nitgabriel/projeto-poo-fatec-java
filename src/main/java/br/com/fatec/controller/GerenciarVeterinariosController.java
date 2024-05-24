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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

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
    private ComboBox<?> cbUnidadeVet;
    @FXML
    private TextField txtEspecialidade;
    @FXML
    private TextField txtCrmv;
    @FXML
    private TextField txtStatus;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnRegistrarVeterinario_Click(ActionEvent event) {
    }

    @FXML
    private void btnAlterarVeterinario_Click(ActionEvent event) {
    }

    @FXML
    private void btnExcluirVeterinario_Click(ActionEvent event) {
    }

    @FXML
    private void btnConsultarVeterinario_Click(ActionEvent event) {
    }
    
}
