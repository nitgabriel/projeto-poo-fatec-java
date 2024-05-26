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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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
    private ComboBox<?> cbUnidadeVet;
    @FXML
    private TextField txtEspecialidade;
    @FXML
    private TextField txtCrmv;
    @FXML
    private TextField txtStatus;
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

    @FXML
    private void lblMenu_Click(MouseEvent event) {
        try {
            App.setRoot("view/Menu");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
