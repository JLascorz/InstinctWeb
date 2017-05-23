/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.instinct.primefaces.showcase.view.input;

import com.instinct.web.objects.Actividad;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.servlet.ServletContext;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.primefaces.model.UploadedFile;

/**
 * FileUploadView te atributs que s'utilitzen per a la pujada de resultats a la base de dades i al servidor.
 * @author Jordi Lascorz
 * @since 19/05/2017
 * @version 1.0
 */
@ManagedBean(name="FileUploadView")
@ViewScoped
public class FileUploadView implements Serializable {
    
    //<editor-fold defaultstate="collapsed" desc="Atributs">
    private UploadedFile file;
    //</editor-fold>
    
    
    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
    public UploadedFile getFile(){
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }
    //</editor-fold>
    
    
    /**
     * Upload Resultado - Aquesta funció no s'utilitza per que ya existeix en ActividadDAO
     * @param resultado
     * @param activ
     * @return
     * @throws FileNotFoundException
     * @throws IOException 
     */
    public String uploadResultado(UploadedFile resultado, Actividad activ) throws FileNotFoundException, IOException{
        UploadedFile uploadedResultado = getFile();
        String filePath = "../applications/InstinctWeb/resources/uploads/actividades/resultados/";
        String filename = "actividad_" + activ.getIdAct() + "_resultado";
        String key = ".pdf";
        byte[] bytes = null;
        
        if(null!= uploadedResultado){
            bytes = uploadedResultado.getContents();
            //String filename = FilenameUtils.getName(uploadedResultado.getFileName());
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(filePath+filename+key)));
            stream.write(bytes);
            stream.close();
        }
        return "Success";
    }
}
