/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.primefaces.showcase.view.input;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;

/**
 * backofficeView te atributs que s'utilitzen per a agafar atributs,
 * les clases de vista no es poden fer abstractes, perque si no no deixa
 * utilitzarles.
 * @author Jordi Lascorz
 * @since 19/05/2017
 * @version 1.0
 */
@ManagedBean(name="adminView")
@SessionScoped
public class backofficeView {
    
    //<editor-fold defaultstate="collapsed" desc="Atributs">
    public String funcion;
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }
    //</editor-fold>
    
    
    
}
