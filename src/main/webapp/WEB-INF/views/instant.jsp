<%--
  Created by IntelliJ IDEA.
  User: admin
  Date: 2018/5/22
  Time: 10:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>instant</title>
</head>
<body>
<form action="/instant" method="post">
    <input type="text" name="facilityId" value="facilityId">
    <input type="text" name="clientId" value="clientId">
    <input type="text" name="ak" value="ak">
    <input type="submit" value="提交">
    ${message}
</form>
</body>
</html>
