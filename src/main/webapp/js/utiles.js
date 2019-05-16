function go_login() {
	window.location="login.html";		
}

function go_comeback(catPrevia) {
	if (catPrevia == 0) {
		go_inicio();
	} else {		
		window.location="comeBack?cmbCategorias=" + catPrevia;
	}	
}

function go_inicio() {
	window.location="home"; 
}

function go_categoria() {
	window.location="subcategoria"; 
}

function desconectar() {
	var log_out=confirm("Con esta accion se finaliza la sesion actual. Esta seguro?");
	if (log_out)
		window.location="desconexion"; 
}

function visitarCat(form) {
	if (check_categoria_seleccionada(form) == true) {
		form.submit();
	}
}

function check_categoria_seleccionada(formulario) {	 
	var comboCat = formulario.cmbCategorias.options;
	var sel = false;
	for (var i = 0; i < comboCat.length; i++) {  
		if (comboCat[i].selected && comboCat[i].value != '')  {   
			sel = true;
			break;
	 	}
	}
	
	if (!sel) {
		alert("Hay que seleccionar una categoria.");		
	} 
	
	return sel;	
}

jQuery(function() {
	jQuery(document).on("click", "#btnPdf", function() {
		jQuery.fileDownload(jQuery(this).attr('value'), {
        	preparingMessageHtml: "Preparando el catalogo, un momento por favor...",
	        failMessageHtml: "Ha ocurrido un problema al generar el catalogo. Por favor, intentelo de nuevo.",
        });
        return false;
    });
});