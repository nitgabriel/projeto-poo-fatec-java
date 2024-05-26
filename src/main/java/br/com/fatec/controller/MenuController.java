/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package br.com.fatec.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import br.com.fatec.App;

/**
 * FXML Controller class
 *
 * @author Gabriel
 */
public class MenuController implements Initializable {

    @FXML
    private AnchorPane main;
    @FXML
    private Button btnGerenciarPets;
    @FXML
    private Button btnGerenciarDonos;
    @FXML
    private Button btnGerenciarVeterinarios;
    @FXML
    private Button btnGerenciarAgendamentos;
    @FXML
    private Button btnGerenciarMedicamentos;
    @FXML
    private Button btnGerenciarUnidades;
    @FXML
    private Button btnConsultarInfo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        
    }    

    @FXML
    private void btnGerenciarPets_Click(ActionEvent event) {
        try {
            App.setRoot("view/GerenciarPets");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnGerenciarDonos_Click(ActionEvent event) {
        try {
            App.setRoot("view/GerenciarProprietarios");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnGerenciarVeterinarios_Click(ActionEvent event) {
        try {
            App.setRoot("view/GerenciarVeterinarios");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnGerenciarAgendamentos_Click(ActionEvent event) {
        try {
            App.setRoot("view/GerenciarAgendamentos");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnGerenciarMedicamentos_Click(ActionEvent event) {
        try {
            App.setRoot("view/GerenciarMedicamentos");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnGerenciarUnidades_Click(ActionEvent event) {
        try {
            App.setRoot("view/GerenciarUnidades");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void btnConsultarInfo_Click(ActionEvent event) {
        try {
            App.setRoot("view/ConsultarInformacoes");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
