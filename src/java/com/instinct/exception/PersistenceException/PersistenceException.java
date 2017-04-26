package com.instinct.exception.PersistenceException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * <p>La classe <b>PersistenceException</b> serveix per a mostrar un error,
 * quan surt algun problema a l'hora de conectarse a la BD.</p>
 * @author Jordi Lascorz
 * @since 06/03/2017
 * @version 1.0
 */
public class PersistenceException extends Exception{
    private Integer codiError;
    /**
     * Constructor de PersistenceException que guarda el codi de l'error que ha sortit.
     * @param codi - int
     */
    public PersistenceException(int codi){
        codiError = codi;
    }
    /**
     * Mostra per pantalla l'error que ha sortit.
     * @return missatge - String
     */
    public String toString(){
        String missatge="";
        StringBuilder sb = new StringBuilder();
        sb.append("Excepció provocada en accedir a la base de dades.");
        sb.append("Codi Error");
        sb.append(codiError.toString());
        missatge=sb.toString();
        return missatge;
    }
}