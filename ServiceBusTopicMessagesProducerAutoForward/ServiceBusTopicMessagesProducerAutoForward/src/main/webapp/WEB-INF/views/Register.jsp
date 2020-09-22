<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Register New User</title>
</head>
<body>
<h2>Register New User</h2>
<form:form modelAttribute="userDetails" action="/autoforward" method="post">
      <table>
          <tr>
              <td style="height:30px">First Name:</td>
              <td><form:input path="userName" /></td>
          </tr>
          <tr>
              <td style="height:30px">Address:</td>
              <td><form:input path="address" /></td>
          </tr>
          <tr>
              <td style="height:30px">state:</td>
              <td><form:input path="state" /></td>
          </tr>
          <tr>
              <td colspan="2" style="height:10px">
                  <input type="submit" value="Register" />
              </td>
          </tr>
      </table>
  </form:form>

</body>
</html>