/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glcdpixels.controlador;

import glcdpixels.modelo.Caracter;
import glcdpixels.modelo.Procesamientos;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author andy
 */
public class InicioController implements Initializable {
    private Procesamientos procesamientos;
    private Caracter caracter;
    
    @FXML
    private Canvas Pantalla;

     @FXML
    private Slider Umbral;
 
   @FXML
    private TextField C_Columnas;

    @FXML
    private TextField C_Filas;

    @FXML
    private Canvas Caracter;

    @FXML
    private TextArea C_Ram;
     
    @FXML
    private TreeView<String> Listado;
    
    @FXML
    private ContextMenu Opciones;
    
    @FXML
    void DETECCION(MouseEvent event) {
        //System.out.println("detectado 4");
        procesamientos = new Procesamientos(Pantalla, 240, 64);        
        procesamientos.setUmbral(Umbral.getValue());
        procesamientos.pruebas();
    }
    
      @FXML
    void CREAR(ActionEvent event) {
          System.out.println("procesando");
         // C_Ram.setText(caracter.mostrarRam());
          C_Ram.setText(caracter.escribirHexadecimal());
          caracter.cargarDiseños(Listado);
    }
    
     @FXML
    void SELECCION(MouseEvent event) {
         //System.out.println("seleccionando pixcel: "+event.getX()+", "+event.getY());
         caracter.pintar(event.getX(), event.getY());
    }
    
     @FXML
    void ACTUALIZAR(ActionEvent event) {
        caracter = new Caracter(Caracter, Integer.valueOf(C_Columnas.getText()), Integer.valueOf(C_Filas.getText()));
        
    }
    
    @FXML
    void ELIMINAR_CARACTER(ActionEvent event) {
        caracter.procesarCaracter(Listado, "borrar");
        caracter.cargarDiseños(Listado);
    }

    @FXML
    void MODIFICAR_CARACTER(ActionEvent event) {
       caracter.procesarCaracter(Listado, "modificar");
    }

    @FXML
    void MOSTRAR_CARACTER(ActionEvent event) {

    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("inciando procesamientos");
        procesamientos = new Procesamientos(Pantalla, 240, 64);
        procesamientos.pruebas();
        
        caracter = new Caracter(Caracter, Integer.valueOf(C_Columnas.getText()), Integer.valueOf(C_Filas.getText()));
        caracter.cargarDiseños(Listado);
        
        caracter.exportarCaracteres();
    }    
  
}
