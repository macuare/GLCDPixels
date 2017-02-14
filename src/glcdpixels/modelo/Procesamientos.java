/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glcdpixels.modelo;

import java.util.LinkedList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.paint.Color;

/**
 *
 * @author andy
 */
public class Procesamientos {
   private Canvas Pantalla;
   private GraphicsContext pincel;
   private int ancho, alto;
   private Image imagen;
   private double umbral = 0; 
   private LinkedList<Integer> memoria;
   
    public Procesamientos(Canvas Pantalla, int ancho, int alto) {
        this.Pantalla = Pantalla;
        this.pincel = Pantalla.getGraphicsContext2D();
        this.ancho = ancho;
        this.alto = alto;
       }

    public Canvas getPantalla() {
        return Pantalla;
    }

    public void setPantalla(Canvas Pantalla) {
        this.Pantalla = Pantalla;
    }
    
    public double getUmbral() {
        return umbral;
    }

    public void setUmbral(double umbral) {
        this.umbral = umbral;
    }
    
    
    
    
    
    private void cargarImagen(){         
        imagen = new Image("file:/home/andy/Imágenes/imagenprueba.png",240,64,false,false);
        System.out.println("imagen: "+imagen.getWidth()+"x"+imagen.getHeight());
    }
    
    private void condicionarImagen(){
       PixelReader pr = imagen.getPixelReader();
       Color color = pr.getColor(0, 0);
       double promedio = 0;
       
       for(int i=0; i<imagen.getWidth(); i++){//ancho de la imagen
         for(int j=0; j<imagen.getHeight(); j++){//alto de la imagen
             color = pr.getColor(i, j);
             
             promedio = (color.getBlue()+color.getGreen()+color.getRed())/3.0;
            // System.out.println("r:"+color.getRed()+", g:"+color.getGreen()+", b:"+color.getBlue());
             promedio*=100;
            // System.out.println(i+","+j+" - "+promedio);
             
             if(promedio >= umbral){ 
                 this.crearPixeles(i, j, true);
                 this.grabarMemoria(1);
             }else{ 
                 this.crearPixeles(i, j, false);
                 this.grabarMemoria(0);
             }
         } 
       }
       
       
    }
    
    public void crearRejillas(){
        double limiteAncho = (Pantalla.getWidth()/ancho);
        double limiteAlto = (Pantalla.getHeight()/alto);
      //  System.out.println("dimensiones: "+Pantalla.getWidth()+"x"+Pantalla.getHeight());
        
        for(int c=0; c<=ancho; c++){
            pincel.strokeLine(c*limiteAncho, 0, c*limiteAncho, Pantalla.getHeight());
        }
        
        for(int f=0; f<=alto; f++){
            pincel.strokeLine(0,f*limiteAlto, Pantalla.getWidth(), f*limiteAlto);
        }
    }
    
    public void limpiarPixel(double coordX, double coorY, double dX, double dY){
     pincel.clearRect(coordX, coorY, dX, dY);
    }
    
    public void crearPixeles(int x, int y, boolean pintar){
        double limiteAncho = (Pantalla.getWidth()/ancho);
        double limiteAlto = (Pantalla.getHeight()/alto);
        
        if(pintar){ 
            pincel.fillRoundRect(x*limiteAncho,y*limiteAlto,limiteAncho,limiteAlto,5,5);
           
        }else{
            this.limpiarPixel(x*limiteAncho,y*limiteAlto,limiteAncho,limiteAlto);
            pincel.strokeRoundRect(x*limiteAncho,y*limiteAlto,limiteAncho,limiteAlto,5,5);
           
        }
        
    }
    
    public void limpiarPantalla(){
       pincel.clearRect(0, 0, Pantalla.getWidth(), Pantalla.getHeight());
    }
    
    public void pruebas(){ 
       this.limpiarPantalla();
       memoria = new LinkedList<>();
       
       pincel.setFill(Color.GREEN);
       pincel.setStroke(Color.BLACK);
       pincel.setLineWidth(0.1);
      
       this.crearRejillas();
       
       this.cargarImagen();
       this.condicionarImagen();
    //  this.crearPixeles(5, 5, true);
        //System.out.println("elementos guardados: "+memoria.size());
       // this.bloques();
    }
    
    
    public void grabarMemoria(int valor){
        memoria.add(valor);
    }
    
    public String arreglosBits(int b){
        String cadena = new String("0b");
        // System.out.println("bit 0:"+memoria.subList(0, 8).get(0));
        for(int i=0; i<8; i++){
        //    System.out.println("valor i: "+i+" bit:"+memoria.subList(0, 8).get(i));
            cadena = cadena.concat(""+memoria.subList(0+b, 7+b+1).get(i));
        }
        //System.out.println("bloque: "+cadena);
        return cadena;
    }
   
    
    
    
    
}
