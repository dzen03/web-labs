<%--
  Created by IntelliJ IDEA.
  User: dzen2
  Date: 10/7/2022
  Time: 10:23 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Using JavaBeans in JSP</title>
</head>
<body>
<h2>Using JavaBeans in JSP</h2>
<jsp:useBean id="msg1" class="com.dzen03.BeanClass" scope="session" />
<jsp:useBean id="msg2" class="com.dzen03.BeanClass" scope="session" />

<form action="BeanServlet" method="get">
    <label>
        <p>Via Servlet</p>
        <jsp:getProperty name="msg1" property="message" />
        <input type="text" name="message">
    </label>
    <input type="submit">
</form>


<form action="bean2.jsp" method="post">
    <label>
        <p>Via JSP</p>
        <jsp:getProperty name="msg2" property="message" />
        <input type="text" name="message">
    </label>
    <input type="submit">
</form>
</body>
</html>
