<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
<head>
	<title>Catálogo - Categorías</title>
	<%@ include file="/WEB-INF/jspf/headers.jspf"%>
</head>

<body>
	<%@ include file="/WEB-INF/jspf/buscador.jspf"%>

	<div id="container" class="animated fadeIn">

		<form method="post" action="subcategoria" onsubmit="return check_categoria_seleccionada(this)" class="niceform">
			<fieldset class="action">
				<jsp:include page="/WEB-INF/jsp/menu.jsp"></jsp:include>
			</fieldset>
			<fieldset>
				<p class="titulo">Catálogo peluquería y cosmética</p>
			</fieldset>
			<fieldset>
				<legend><c:out value="${sessionScope['catPadre']}" /></legend>
				<dl>
					<dt><label for="cmbCategorias">Categorias:</label></dt>
					<dd>
						<c:set var="categorias" value="${sessionScope['categorias']}"/>
						<select size="15" name="cmbCategorias" id="cmbCategorias" ondblclick="visitarCat(form)">
							<c:forEach var="categoria" items="${categorias}">
								<option value="${categoria.id}"><c:out value="${categoria.descripcion}" /></option>
							</c:forEach>
						</select>
					</dd>
				</dl>
			</fieldset>

			<fieldset class="action">
				<div align="center">
					<input type="submit" name="btnSubmit" id="btnSubmit" value="Acceder" />
				</div>
			</fieldset>
		</form>
	</div>
</body>
</html>