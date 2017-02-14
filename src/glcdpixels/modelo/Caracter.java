/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package glcdpixels.modelo;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 *
 * @author andy
 */
public class Caracter {
    private Procesamientos p;
    private Archivo archivo;
    
    private Canvas Dibujo;
    private int columnas, filas;    
    private int mapa[][];

    public Caracter(Canvas Dibujo, int columnas, int filas) {
        this.Dibujo = Dibujo;
        this.columnas = columnas;
        this.filas = filas;
       // this.estructurarMapa();
        
        this.estructurarMapa();
        this.limpiarMapa();
        
        p = new Procesamientos(Dibujo, columnas, filas);        
        p.limpiarPantalla();
        p.crearRejillas();
        
        archivo = new Archivo();
    }
    
    private void estructurarMapa(){
        int dimensionX = columnas;
        int dimensionY = filas;
        
        if(columnas%8 != 0) dimensionX = ((columnas/8)+1)*8;
        if(filas%8 != 0) dimensionY = ((filas/8)+1)*8;
        
        //System.out.println("sx:"+dimensionX+" - sy:"+dimensionY);
        this.mapa = new int[dimensionY][dimensionX];
        
    }
    
    private void limpiarMapa(){
        for(int f=0; f<filas; f++){
            for(int c=0; c<columnas; c++){
                mapa[f][c] = 0;
            }
        }
    }
    
    private void escribirRam(int f, int c, int bit){
        mapa[f][c] = bit;
    }
    
    /**Este metodo determina en que bloque se encuetra segun las dimensiones suministradas;
     @param longitud: dimension total de la pantalla
     @param bloques: cantidad de bloques que divide la pantalla
     @param posicion: valor de la ubicacion donde se encuentra el elemento
     
     *@return la posicion del bloque donde se encuentra el elemento dentro del contexto de la pantalla.*/
    private int bloque(double longitud, int bloques, double posicion){
        double ancho = (longitud/bloques);
        int i = 0;
        
        for(i=0; i<bloques; i++){
            if(posicion >= (i*ancho) && posicion < ((i*ancho)+ancho) ){               
               break;
            }
        }
        
        return (i);
    }
    
    private int coordenadaX(double posX){
       return this.bloque(Dibujo.getWidth(), columnas, posX);
    }
    
    private int coordenadaY(double posY){
       return this.bloque(Dibujo.getHeight(), filas, posY);
    }
    
    public void pintar(double posX, double posY){        
        System.out.println("pX: "+this.coordenadaX(posX)+", pY: "+this.coordenadaY(posY));
        int f = this.coordenadaY(posY);
        int c = this.coordenadaX(posX);
        int bit = mapa[f][c];
        
        if(bit == 0){
            p.crearPixeles(c, f, true);
            bit = 1;
        }else{
            p.crearPixeles(c, f, false);
            bit = 0;           
        }
        
        this.escribirRam(f, c, bit);                
       
    }
    
    public String mostrarRam(){
        DecimalFormat df = new DecimalFormat("00");
        String texto = "";
        System.out.println("filas: "+filas+" | columnas: "+columnas);
        
        for(int f=0; f<filas; f++){
            for(int c=0; c<columnas; c++){
                texto = texto.concat(String.valueOf(mapa[f][c]));
            }
            System.out.println("L"+df.format(f)+": "+texto);
            texto = "";
        }
        
        return "s";
    }
    
    public String escribirHexadecimal(){
        String combinacion = "0b";
        String salida = "";
        LinkedList<String> elementos = new LinkedList<>();
        
        
        elementos.add(filas+":"+columnas); //agregando descripcion del archivo
        
        for(int f=0; f<filas; f=f+8){
         for(int c=0; c<columnas; c++){ 
              
              combinacion = combinacion.concat(String.valueOf(mapa[f+7][c]));
              combinacion = combinacion.concat(String.valueOf(mapa[f+6][c]));
              combinacion = combinacion.concat(String.valueOf(mapa[f+5][c]));
              combinacion = combinacion.concat(String.valueOf(mapa[f+4][c]));
              
              combinacion = combinacion.concat(String.valueOf(mapa[f+3][c]));
              combinacion = combinacion.concat(String.valueOf(mapa[f+2][c]));
              combinacion = combinacion.concat(String.valueOf(mapa[f+1][c]));
              combinacion = combinacion.concat(String.valueOf(mapa[f+0][c]));
              
            // System.out.println(combinacion);
              elementos.add(combinacion);              
              salida = salida.concat(combinacion+",\n");    
              combinacion = "0b";
              
          }
        }        
        
        archivo.escribirArchivo(elementos); // escribiendo en texto plano
        salida = salida.substring(0, salida.length()-2); // eliminando la ultima coma
        
        return salida;
    }
    
    private LinkedList<String> cargarDatos(String nombreArchivo){       
        return archivo.leerArchivo(nombreArchivo);               
    }
    
    private void reescribirRam(LinkedList<String> datos){
        AtomicInteger x = new AtomicInteger(); // declarando variable atomica
      //  System.out.println("datos: "+datos);
        
        
        x.set(0);
        datos.stream()
                .filter(s -> s.startsWith("0b"))
                .forEach(d -> {                    
                    d = d.substring(2); //eliminando 0b
                    //System.out.println(" -> "+d);
                    for(int i=0; i<d.length(); i++){
                       int coordX =  (x.get()-((x.get()/columnas)*columnas));
                       int coordY =  i+(((x.get()/columnas)*8));
                       // System.out.println("("+coordY+","+coordX+") = "+d.charAt(d.length()-1-i));
                       this.escribirRam(
                                         coordY,
                                         coordX ,
                                         Integer.parseInt(""+d.charAt(d.length()-1-i))
                       );
                     //  System.out.println("directo: "+mapa[coordY][coordX]);
                    }
                    
                    x.getAndIncrement();
                });
    }
    
    private void estructurarDatos(LinkedList<String> datos, Canvas c){        
        AtomicInteger ancho = new AtomicInteger(); // declarando variable atomica        
        AtomicInteger x = new AtomicInteger(); // declarando variable atomica
        int alto = 0;
        
        // System.out.println("descripcion: "+datos.getFirst().split(":")[1]);
        ancho.set(Integer.valueOf(datos.getFirst().split(":")[1]));
        alto = Integer.valueOf(datos.getFirst().split(":")[0]);
        
        Procesamientos p = new Procesamientos(c, ancho.get(), alto);
        p.crearRejillas();
        
        x.set(0);
        datos.stream()
                .filter(s -> s.startsWith("0b"))
                .forEach(d -> {
                   // System.out.println("analizando: "+d);
                    d = d.substring(2); //eliminando 0b
                 
                    for(int i=0; i<d.length(); i++){
                       // System.out.println("coord: ("+(x.get()-((x.get()/ancho.get())*ancho.get()))+", "+(((x.get()/ancho.get())*8)) +") - bit:"+(d.charAt(d.length()-1-i)=='1'));
                        p.crearPixeles((x.get()-((x.get()/ancho.get())*ancho.get())),i+(((x.get()/ancho.get())*8)) , d.charAt(d.length()-1-i)=='1');                        
                    }
                    
                    x.getAndIncrement();
                });
        
    }
    
    private Canvas preVisualizacion(String nombreArchivo){        
        Canvas vista = new Canvas(80, 80);
        
        this.estructurarDatos(this.cargarDatos(nombreArchivo), vista);
        
        return vista;
    }
    
    public void cargarDiseños(TreeView listado){
        TreeItem<String> raiz = new TreeItem<>("Diseños");
        raiz.setExpanded(true);
    
        LinkedList<File> lista = new LinkedList<>();         
        Arrays.stream(archivo.listarArchivos())
               .sorted((s1, s2) -> s1.getName().toLowerCase().compareTo(s2.getName().toLowerCase()))
               .forEach(s -> lista.add(s));
       
        lista.stream()
             .forEach(elemento -> 
                     raiz.getChildren().add(new TreeItem<String>(elemento.getName(),this.preVisualizacion(elemento.getName())))
             );

        listado.setRoot(raiz); 
    }
    
    public void modificarCaracter(String seleccionado){
        LinkedList<String> datos = this.cargarDatos(seleccionado);
       String descripcion = datos.getFirst();
       this.filas = Integer.valueOf(descripcion.split(":")[0]); //estableciendo estos parametros a partir del archivo de texto plano
       this.columnas = Integer.valueOf(descripcion.split(":")[1]);
       
       this.estructurarMapa();
       this.limpiarMapa();        
       p = new Procesamientos(Dibujo, columnas, filas);        
       p.limpiarPantalla();
       p.crearRejillas(); 
        
       this.estructurarDatos(datos, Dibujo);
       this.reescribirRam(datos);
       this.mostrarRam();
       
    }
    
    
    
    public void procesarCaracter(TreeView listado, String solicitud){
        System.out.println(listado.getSelectionModel().isEmpty()+"-"+listado.getSelectionModel().getSelectedItem());
        
        
        if(!listado.getSelectionModel().isEmpty() && listado.getSelectionModel().getSelectedItem() != null){
            
            String seleccionado = listado.getTreeItem(listado.getSelectionModel().getSelectedIndex()).getValue().toString();
           // System.out.println("seleccionado: "+seleccionado);
            
            switch(solicitud){
                case "mostrar":
                
                break;
                case "modificar":
                    this.modificarCaracter(seleccionado);
                break; 
                case "borrar":
                    archivo.borrarArchivo(seleccionado);
                break;
                default:
                    System.out.println("No se pudo procesar la solicitud: "+solicitud);
                break;    
            }
        }else{
            System.out.println("seleccion incorrecta");
        }
        
    }
    
    
    // 96 Caracteres = abcdario y simbolos comunes = 768 bytes
    // ??? caracters adicionales y gadgets
    
    public void exportarCaracteres(){
        AtomicReference texto = new AtomicReference("");
       
        LinkedList<File> lista = new LinkedList<>();         
        Arrays.stream(archivo.listarArchivos())
              .sorted((s1, s2) -> s1.getName().compareTo(s2.getName()))
              .forEach(lista::add);
    
        lista.stream()
             .filter(doc -> doc.getName().startsWith("Letra"))
             .forEach(elemento -> {
                     archivo.leerArchivo(elemento.getName()).stream()
                            .filter(linea -> linea.startsWith("0b"))
                            .forEach(linea -> texto.set(texto.get().toString().concat(linea+", ")));
//                     System.out.println("archivo: "+elemento.getName());
//                     System.out.println("texto: "+texto.getAndSet(""));
                     System.out.println(texto.getAndSet("").toString()//.replace(" 0b00000000, 0b00000000, 0b00000000,", "")/*solo para eliminar tres columnas de puros ceros*/
                                                           .concat(" //"+elemento.getName()
                                                                                 .substring("Letra_".length())
                                                                                 .replace(".txt", ""))
                     );
                             }
                             
             );
    }
    
    
    
}
