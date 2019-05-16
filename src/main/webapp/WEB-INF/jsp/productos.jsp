<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/pager-taglib.tld" prefix="pg"%>

<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="es">
<head>
	<title>Catálogo - Productos de la categoría</title>
	<%@ include file="/WEB-INF/jspf/headers.jspf"%>
	<%@ include file="/WEB-INF/jspf/headers-prod.jspf"%>
</head>

<body>
	<%@ include file="/WEB-INF/jspf/buscador.jspf"%>

	<div id="container" class="animated fadeIn">
		<form method="post" class="niceform">

			<fieldset class="action">
				<jsp:include page="/WEB-INF/jsp/menuProductos.jsp"></jsp:include>
			</fieldset>
			<fieldset>
				<p class="titulo">Catálogo peluquería y cosmética</p>
			</fieldset>
			<fieldset>
				<legend>
					<c:out value="${sessionScope['catPadre']}" />
				</legend>

				<%@ include file="/WEB-INF/jspf/paginador.jspf"%>

				<br />
			</fieldset>
			<fieldset class="action">
				<jsp:include page="/WEB-INF/jsp/menuProductos.jsp"></jsp:include>
			</fieldset>
		</form>
	</div>
</body>
</html>