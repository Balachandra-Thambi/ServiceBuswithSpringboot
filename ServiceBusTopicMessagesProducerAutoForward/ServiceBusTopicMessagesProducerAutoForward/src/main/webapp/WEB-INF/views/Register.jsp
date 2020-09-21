<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
<form:form modelAttribute="userDetails" action="/autoforward" method="post">
      <table>
          <tr>
              <td>First Name:</td>
              <td><form:input path="userName" /></td>
          </tr>
          <tr>
              <td>Address:</td>
              <td><form:input path="address" /></td>
          </tr>
          <tr>
              <td>state:</td>
              <td><form:input path="state" /></td>
          </tr>
          <tr>
              <td colspan="2">
                  <input type="submit" value="Submit" />
              </td>
          </tr>
      </table>
  </form:form>

</body>
</html>