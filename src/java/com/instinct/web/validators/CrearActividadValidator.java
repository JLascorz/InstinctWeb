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

@ManagedBean(name="actValidator")
@SessionScoped
@FacesValidator("actValidator")
public class CrearActividadValidator implements Validator{
    

    @Override
    public void validate(FacesContext context, UIComponent component, Object obj) throws ValidatorException {
        

        if(component.getId().contains("fecnac")){
            String fecNac = (String) obj;
            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date dt = new Date();
            String fechaActual = dateFormat.format(dt);
            String fac[] = fechaActual.split("/");
            int actyear = Integer.parseInt(fac[0]);
            int actmonth = Integer.parseInt(fac[1]);
            int actday = Integer.parseInt(fac[2]);
            
            
            String str[] = fecNac.split("-");
            int year = Integer.parseInt(str[0]);
            int month = Integer.parseInt(str[1]);
            int day = Integer.parseInt(str[2]);
            //String fechaActual = dateFormat.format(dt);
            if(year >= actyear || year <= actyear-130 || year > actyear - 14){
                FacesMessage msg = new FacesMessage(
                    "Tienes que tener minimo 14 años");
                msg.setSeverity(FacesMessage.SEVERITY_ERROR);
                throw new ValidatorException(msg);
            }else if(month > 12 || month < 1){
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
        
    }
}
