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
    private ComboBox<?> cbDonos;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnRegistrarPet_Click(ActionEvent event) {
    }

    @FXML
    private void btnAlterarPet_Click(ActionEvent event) {
    }

    @FXML
    private void btnExcluirPet_Click(ActionEvent event) {
    }

    @FXML
    private void btnConsultarPet_Click(ActionEvent event) {
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
