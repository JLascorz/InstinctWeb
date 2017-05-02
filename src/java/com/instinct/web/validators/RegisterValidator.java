/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.web.validators;
import com.instinct.web.objects.Usuario;
import static java.lang.Integer.parseInt;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;

/**
 *
 * @author daw2017
 */

@ManagedBean(name="registerValidator")
@SessionScoped
@FacesValidator("registerValidator")
public class RegisterValidator implements Validator{
    

    @Override
    public void validate(FacesContext context, UIComponent component, Object obj) throws ValidatorException {
        String pass1 = null;
        if(component.getId().contains("password1")){
            pass1 = (String) obj;
            String pattern = "\"^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$\"";
            
            if(pass1.length() < 8){
               FacesMessage msg = new FacesMessage(
                    "La contraseña no puede tener menos de 8 caracteres!");
               msg.setSeverity(FacesMessage.SEVERITY_ERROR);
               throw new ValidatorException(msg); 
            }else if(!pass1.matches(pattern)){
               FacesMessage msg = new FacesMessage(
                    "La contraseña debe tener minimo 8 caracteres, incluyendo 1 mayuscula y 1 minuscula.");
               msg.setSeverity(FacesMessage.SEVERITY_ERROR);
               throw new ValidatorException(msg); 
            }
        }
        
        if(component.getId().contains("password2")){

            String confirmPassword = (String) obj;
            if (!confirmPassword.equals(pass1)) {
                FacesMessage msg = new FacesMessage(
                    "Las contraseñas no coinciden.");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }
        }
        
        if(component.getId().contains("fecnac")){
            String fecNac = (String) obj;
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date dt = new Date();
            
            String str[] = fecNac.split("/");
            int day = Integer.parseInt(str[0]);
            int month = Integer.parseInt(str[1]);
            int year = Integer.parseInt(str[2]);
            //String fechaActual = dateFormat.format(dt);
            if(year >= dt.getYear() || year <= dt.getYear()-130){
                FacesMessage msg = new FacesMessage(
                    "El año no es valido.");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }else 
            if(month > 12 || month < 1){
                FacesMessage msg = new FacesMessage(
                    "El mes no es valido.");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }else if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 11){
                if(day > 31 || day < 1){
                    FacesMessage msg = new FacesMessage(
                    "El dia del mes no es valido.");
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    throw new ValidatorException(msg);
                }
            }else if(month == 4 || month == 6 || month == 9 || month == 11){
                if(day > 30 || day < 1){
                    FacesMessage msg = new FacesMessage(
                    "El dia del mes no es valido.");
                    msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                    throw new ValidatorException(msg);
                }
            }else if(month == 2){
                if((year % 4 == 0) && ((year % 100 != 0) || (year % 400 == 0))){
                    if(day > 29 || day < 1){
                        FacesMessage msg = new FacesMessage(
                        "El dia del mes no es valido.");
                        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                        throw new ValidatorException(msg);
                    }
                }else{
                    if(day > 28 || day < 1){
                        FacesMessage msg = new FacesMessage(
                        "El dia del mes no es valido.");
                        msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                        throw new ValidatorException(msg);
                    }
                }
            }
        }
        
        if(component.getId().contains("nif")){
            String nif = (String) obj;
            String pattern = "/^(\\d{8})([A-Z])$/";
            if(!nif.matches(pattern)){
               FacesMessage msg = new FacesMessage(
                    "El nif tiene que ser de 8 numeros y 1 letra.");
               msg.setSeverity(FacesMessage.SEVERITY_ERROR);
               throw new ValidatorException(msg); 
            }else if((nif.length() > 9) || (nif.length() < 9 && nif.length() > 0)){
               FacesMessage msg = new FacesMessage(
                    "El nif tiene que ser de 8 numeros y 1 letra.");
               msg.setSeverity(FacesMessage.SEVERITY_ERROR);
               throw new ValidatorException(msg); 
            }
            
        }
    }
}
