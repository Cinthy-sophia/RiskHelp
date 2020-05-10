package com.cinthyasophia.riskhelp.Util;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class Lib implements Serializable {
    private Random rnd = new Random();
    /**
     * Transforma en string el vector de enteros que reciba
     * @param vector
     * @return String
     */
    public String toString(int[] vector){
        String cadena= "";
        for (int i = 0; i <vector.length ; i++) {
            cadena= cadena.concat(Integer.toString(vector[i]))+" ";
        }
        return cadena;
    }

    /**
     * Ordena el vector de enteros que reciba y lo devuelve ordenado
     * @param vector
     * @return int[]
     */
    public int[] ordernarCombinacion(int[] vector) {
        boolean hayCambios = true;
        int aux;
        
        while(hayCambios) {
            hayCambios = false;
            for(int i = 0; i < vector.length - 1; i++) {
                if(vector[i] > vector[i+1]) {
                    aux = vector[i];
                    vector[i] = vector[i+1];
                    vector[i+1] = aux;
                    hayCambios = true;
                }
            }
        }
        return vector;
    }

    /**
     * Recibe el vector y un numero, y comprueba que no se encuentre en el
     * vector. Si lo encuentra devuelve true, si no, devuelve false.
     * @param vector
     * @param val
     * @return boolean
     */
    public boolean existeValor(int[] vector,int val){
        boolean existeValor= false;
        for (int i = 0; i < vector.length; i++) {
            if (vector[i]== val){
                existeValor= true;
            }

        }
        return existeValor;
    }
    public boolean existeValor(double[] vector, double val){
        boolean existeValor= false;
        for (int i = 0; i < vector.length; i++) {
            if (vector[i]== val){
                existeValor= true;
            }

        }
        return existeValor;
    }
    //Devuelve un String para indicar un error en el menu
    public String errorMenu(){
        return "Elija una opción del menú.";
    }

    public void errorDatos(){ System.out.println("Debes introducir un numero y no una letra.");}

    //Devuelve un String al volver al menu principal
    public String volverMenu(){
        return "Volviendo al menu principal.";

    }
    //Felicitacion al jugador
    public String felicitacion(){
        return "\u001B[1;0;0m¡ENHORABUENA!\u001B[0m";
    }

    public void pausa(){
        Scanner lector = new Scanner(System.in);
        System.out.println("Presiona Intro para continuar.");
        lector.nextLine();
    }

    /**
     * Le resta el ultimo numero al vector que recibe y lo mueve a otro array con un tamaño menor
     * @param vectorP
     * @param num
     * @return int[]
     * Devuelve el array con el numero menos
     */
    public int[] numeroMenos(int[] vectorP, int num){
        int[] vector = new int[num];
        for (int i = 0; i < vector.length; i++) {

            vector[i]=vectorP[i];
        }
        return vector;
    }
    public int aleatorio(int min, int max) {
        return rnd.nextInt(max - min +1 ) + min;
    }
    public double aleatorio(double min, double max) {
        return rnd.nextDouble()*(max - min);
    }

    /**
     * Cambia la fecha de string a GregorianCalendar
     * @param f
     * @return GregorianCalendar
     * Devuelve la fecha en GregorianCalendar
     */
    public GregorianCalendar getFecha(String f){
        SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy HH:mm");
        GregorianCalendar fechaN= new GregorianCalendar();
        Date d= new Date();
        try{
            d= format.parse(f);
        }catch (ParseException pe){
            pe.printStackTrace();
        }
        fechaN.setTime(d);
        return fechaN;
    }
    /**
     * Recibe un GregorianCalendar como parametro y devuelve la fecha en String
     * @param fecha
     * @return String
     */
    public String getFecha(GregorianCalendar fecha){
        SimpleDateFormat format= new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String d;

        d= format.format(fecha.getTime());

        return d;
    }

    /**
     *Valida la opcion del menu y devuelve la opcion que el usuario haya elegido.
     * @param opcionMin
     * @param opcionMax
     * @return int
     */
    public int validarOpcion(int opcionMin, int opcionMax){
        Scanner lector = new Scanner(System.in);
        int opcion=0;
        boolean isNumber;
        do {
            try {
                opcion = lector.nextInt();
                isNumber = true;
            } catch (InputMismatchException ime) {
                errorDatos();
                isNumber = false;
            } finally {
                lector.nextLine();
            }
            if (opcion < opcionMin || opcion > opcionMax) {
                System.out.println(errorMenu());
            }

        } while (!isNumber || opcion < opcionMin || opcion > opcionMax);

        return opcion;
    }

    /**
     *Recibe la fecha y regresa la edad.
     * @param fechaNac
     * @return int
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public int getEdad(GregorianCalendar fechaNac){
        int year= fechaNac.get(Calendar.YEAR);
        int month= fechaNac.get(Calendar.MONTH);
        int day= fechaNac.get(Calendar.DAY_OF_MONTH);
        int edad;

        LocalDate birth= LocalDate.of(year,month,day);
        LocalDate now = LocalDate.now();
        Period p;
        p= Period.between(birth, now);

        if (birth.isBefore(now)){
            edad= p.getYears()-1;
        }else{
            edad= p.getYears();
        }
        return edad;
    }

    /**
     * Obtiene los dias que hay entre la fecha actual del sistema y la que recibe.
     * @param fecha
     * @return int
     */
    public int obtenerDias(GregorianCalendar fecha){
        int year= fecha.get(Calendar.YEAR);
        int month= fecha.get(Calendar.MONTH);
        int day= fecha.get(Calendar.DAY_OF_MONTH);
        int dias;

        LocalDate date= LocalDate.of(year,month,day);
        LocalDate now = LocalDate.now();
        Period p;
        p= Period.between(date, now);

        dias= p.getDays();
        return dias;

    }

    /**
     * Devuelve un boolean indicando si la fecha que recibe por parametro es antes de la fecha actual.
     * @param fecha
     * @return boolean
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean fechaIsBeforeNow(GregorianCalendar fecha){
        int year= fecha.get(Calendar.YEAR);
        int month= fecha.get(Calendar.MONTH+1);
        int day= fecha.get(Calendar.DAY_OF_MONTH);

        LocalDate fechaActual= LocalDate.of(year,month,day);
        LocalDate now = LocalDate.now();

        return fechaActual.isBefore(now);

    }
    public int[] getCombinacion( int numeroFichas, int sizeCombinacion) {
        int aux;
        int cont;
        int numAl;
        int[] fichas= new int[numeroFichas];
        int[] combinacion= new int[sizeCombinacion];

        //llena el array fichas de numeros aleatorios entre en 1 y el numero de fichas que recibe como parametro
        for (int i = 0; i < fichas.length ; i++) {
            fichas[i]= i+1;
        }

        cont= fichas.length-1;

        for (int i = 0; i <combinacion.length ; i++) {
            numAl= rnd.nextInt(cont);
            combinacion[i] = fichas[numAl];
            aux=fichas[numAl];
            fichas[numAl]= fichas[cont];
            fichas[cont]=aux;
            cont--;

        }
        //combinacion=ordernarCombinacion(combinacion);
        return combinacion;//Devuelve el vector con la combinacion

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Boolean validarFecha(String fecha){
        Scanner lector = new Scanner(System.in);
        boolean validado;
        do {
            validado= fecha.length() >= 10 && fechaIsBeforeNow(getFecha(fecha));

            if (!validado){
                return false;
            }

        }while(!validado);

        return true;
    }


}
