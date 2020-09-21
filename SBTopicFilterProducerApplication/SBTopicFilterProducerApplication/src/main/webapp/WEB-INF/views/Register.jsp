<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Product Ordering Detail Form</title>
<style>
type ="text /css ">
input {
	margin-top: 5px;
}
</style>
</head>
<body>
	<h2>Enter Product Information</h2>
	<form:form modelAttribute="order" action="/TopicFilter" method="post">
		<table>
			<tr>
				<td>Product Name:</td>
				<td><form:input path="name" /></td>
			</tr>
			<tr>
				<td>Product Color:</td>
				<td><form:input path="color" /></td>
			</tr>
			<tr>
				<td>Product quantity:</td>
				<td><form:input path="quantity" /></td>
			</tr>
			<tr>
				<td>Product priority:</td>
				<td><form:input path="priority" /></td>
			</tr>
			<tr>
				<td colspan="2"><input type="submit" value="Submit Order" /></td>
			</tr>
		</table>
	</form:form>

</body>
</html>