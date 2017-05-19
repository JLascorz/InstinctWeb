
function getSessionId(){
    var scripts = document.getElementsByTagName('script');
    var index = scripts.length - 1;
    var myScript = scripts[index];
    var str = myScript.src;
    var loc = str.indexOf(";");
    var valor = str.substr(loc);
    if(valor == "l"){
        return "";
    }
    return valor;
}

function detectarBody(){
    var href = location.pathname;
    var dir = href.substring(0, href.lastIndexOf('/')) + "/";
    var session = getSessionId();
    var data = null;
    var datajs = null;
    if(document.getElementById("index")){
        data = dir+"javax.faces.resource/css/index.css.xhtml";
    }else if(document.getElementById("register")){
        data = dir+"javax.faces.resource/css/register.css.xhtml";
    }else if(document.getElementById("login")){
        data = dir+"javax.faces.resource/css/login.css.xhtml";
    }else if(document.getElementById("perfil")){
        data = dir+"javax.faces.resource/css/perfil.css.xhtml";
    }else if(document.getElementById("misActividades")){
        data = dir+"javax.faces.resource/css/misActividades.css.xhtml";
    }else if(document.getElementById("crearActividad")){
        data = dir+"javax.faces.resource/css/crear_actividad.css.xhtml";
    }else if(document.getElementById("calendario")){
        data = dir+"javax.faces.resource/css/calendario.css.xhtml";
    }else if(document.getElementById("actividad")){
        data = dir+"javax.faces.resource/css/actividad.css.xhtml";
        datajs = dir+"javax.faces.resource/js/actividad.js.xhtml";
        cargarCSS(datajs);
    }else if(document.getElementById("misInscripciones")){
        data = dir+"javax.faces.resource/css/misInscripciones.css.xhtml";
    }else if(document.getElementById("backoffice")){
        data = dir+"javax.faces.resource/css/backoffice.css.xhtml";
    }
    //alert(location.pathname);
    cargarCSS(data);
}

function cargarCSS(data){

    var head  = document.getElementsByTagName('head')[0];
    var link  = document.createElement('link');
    link.rel  = 'stylesheet';
    link.type = 'text/css';
    link.href = data;
    link.media = 'all';
    head.appendChild(link);
    
}

window.onload = detectarBody;

function redirect(){
    window.location.href='index.xhtml';
}

