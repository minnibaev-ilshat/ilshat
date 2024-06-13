<%@page import="ua.foxminded.dynamicweb.DatabaseFacade"%>

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

	<h1>List of books 1 </h1>

	<%
	
	for (String name : DatabaseFacade.getBooks()) {
		out.print(name + "<br/>");

	}
	%>
</body>
</html>