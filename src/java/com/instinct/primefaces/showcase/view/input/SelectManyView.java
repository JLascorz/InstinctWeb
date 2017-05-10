/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.primefaces.showcase.view.input;

import com.instinct.daos.impl.jdbc.allMySQLDAO.ServicioMySQLDAO;
import com.instinct.web.objects.Servicio;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
 
 
@ManagedBean(name="SelectManyView")
@ViewScoped
public class SelectManyView {
     
    private List<String> selectedOptions;
    private List<String> selectedServices;
    private List<Servicio> services;
     
    @ManagedProperty("#{themeService}")
    private Servicio service;
      
    public List<Servicio> getServicios() {
        return services;
    }
 
    public void setService(Servicio service) {
        this.service = service;
    }
 
    public List<String> getSelectedOptions() {
        return selectedOptions;
    }
 
    public void setSelectedOptions(List<String> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }
 
    public List<String> getSelectedServices() {
        return selectedServices;
    }
 
    public void setSelectedServices(List<String> selectedServices) {
        this.selectedServices = selectedServices;
    }
}
