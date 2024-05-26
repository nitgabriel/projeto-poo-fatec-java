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

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class GerenciarAgendamentosController implements Initializable {

    @FXML
    private Button btnRegistrarAgendamento;
    @FXML
    private Button btnAlterarAgendamento;
    @FXML
    private Button btnExcluirAgendamento;
    @FXML
    private Button btnConsultarAgendamento;
    @FXML
    private ComboBox<?> cbDonos;
    @FXML
    private TextField txtEspecialidade;
    @FXML
    private ComboBox<?> cbPets;
    @FXML
    private DatePicker dpDataAgend;
    @FXML
    private ComboBox<?> cbUnidade;
    @FXML
    private ComboBox<?> cbHorarios;
    @FXML
    private ComboBox<?> cbMedicamentos;
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
    private void btnRegistrarAgendamento_Click(ActionEvent event) {
    }

    @FXML
    private void btnAlterarAgendamento_Click(ActionEvent event) {
    }

    @FXML
    private void btnExcluirAgendamento_Click(ActionEvent event) {
    }

    @FXML
    private void btnConsultarAgendamento_Click(ActionEvent event) {
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
