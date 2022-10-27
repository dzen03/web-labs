<%--
  Created by IntelliJ IDEA.
  User: dzen2
  Date: 10/7/2022
  Time: 10:41 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    
    <title>Title</title>
</head>
<body>
<jsp:useBean id="msg2" class="com.dzen03.BeanClass" scope="session"/>
<%
    String message = request.getParameter("message");
%>

<jsp:setProperty name="msg2" property="message" value="<%=message%>"/>

<a href="bean.jsp">link</a>


</body>
</html>
