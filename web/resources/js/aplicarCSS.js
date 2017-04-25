
function getSessionId(){
    var scripts = document.getElementsByTagName('script');
    var index = scripts.length - 1;
    var myScript = scripts[index];
    var str = myScript.src;
    var loc = str.indexOf(";");
    var valor = str.substr(loc);
    
    return valor;
}

function detectarBody(){
    var href = location.pathname;
    var dir = href.substring(0, href.lastIndexOf('/')) + "/";
    var session = getSessionId();
    var data = null;
    if(document.getElementById("register")){
        data = dir+"javax.faces.resource/css/register.css.xhtml"+session;
    }
    
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