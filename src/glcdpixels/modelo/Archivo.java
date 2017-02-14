/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glcdpixels.modelo;

import glcdpixels.GLCDPixels;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

/**
 *
 * @author andy
 */
public class Archivo {
    private File directorio;

    public Archivo() {
        directorio = new File("/home/andy/Documentos/CaracteresKS0108/Caracteres");   
    }
    
 public File[] listarArchivos(){ 
     
     return directorio.listFiles();
 }   

    public File getDirectorio() {
        return directorio;
    }

    public void setDirectorio(File directorio) {
        this.directorio = directorio;
    }
 
 
 
 public void guardar(){
     
     FileChooser fc = new FileChooser();
     fc.setTitle("Guardar Archivo");
     fc.setInitialDirectory(directorio);
     fc.getExtensionFilters().addAll(
         new ExtensionFilter("Archivo de Texto", "*.txt"),
         //new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
         //new ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aac"),
         new ExtensionFilter("Todos los Archivos", "*.*")
     );
 
     Window ventana = GLCDPixels.scene.getWindow();
     
     File documento = fc.showSaveDialog(ventana);
     //System.out.println("documento:"+documento);
     if(documento != null) directorio = documento;
     
 }   
        
 public LinkedList<String> leerArchivo(String nombreArchivo){
         LinkedList<String> elementos = new LinkedList<>();
         //System.out.println("dir: "+directorio.getAbsolutePath());
        try (Stream<String> flujo = Files.lines(Paths.get(directorio.getAbsolutePath()+"/"+nombreArchivo))){               
            flujo.forEach(elementos::add);
        } catch (IOException e) {
            System.out.println("Error leyendo archivo. "+nombreArchivo);
            e.printStackTrace();
	}
        
        return elementos;
    }
 
    
public void escribirArchivo(LinkedList<String> datos){
        FileWriter escritor = null;
        PrintWriter lapiz = null;
      //  System.out.println(direccion);
     
      this.guardar();
     
     // System.out.println("1ro:"+(direccion != null)+"\n2do:"+(datos != null)+"\n3ro:"+direccion.exists()+"\n4to:"+direccion.isFile()+"\n5to:"+direccion.isAbsolute());
      
    if(directorio != null && datos != null && directorio.getAbsolutePath().endsWith(".txt")){
        try {
            File archivo = directorio;//new File(direccion);
           // System.out.println("direccion interna: "+direccion.getAbsolutePath());
            escritor = new FileWriter(archivo,false);
            lapiz = new PrintWriter(escritor);
            
            datos.stream().forEach(lapiz::println);
            directorio = directorio.getParentFile(); // retornando como direccion base la carpeta del archivo actual
            
        } catch (IOException ex) {
            Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if(escritor != null) escritor.close();
            } catch (IOException ex) {
                Logger.getLogger(Archivo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    
    }else{
        System.out.println("El archivo no existe");
    }
}

public void borrarArchivo(String documento){
    File elemento = new File(directorio+"/"+documento);
    if(elemento.delete()) System.out.println("Archivo borrado exitosamente...!!!");
    else System.out.println("El archivo seleccionado no pudo ser borrado...!!!");
}
    
    
}//fin de la clase
    

