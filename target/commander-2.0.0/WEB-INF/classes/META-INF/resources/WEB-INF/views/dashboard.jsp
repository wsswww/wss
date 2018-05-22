<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>设备</title>
</head>
<body>
<form>
    <table width="100%" border=1>
        <tr>
            <td>设备id</td>
            <td>imei</td>
            <td>oem</td>
            <td>apkVersionCode</td>
            <td>channel</td>
            <td>在线否</td>
            <td>操作</td>
        </tr>
        <c:forEach items="${list}" var="deviceChannel">
            <tr>
                <td>${deviceChannel.deviceId}</td>
                <td>${deviceChannel.imei}</td>
                <td>${deviceChannel.oem}</td>
                <td>${deviceChannel.apkVersionCode}</td>
                <td>${deviceChannel.channel}</td>
                <td>${deviceChannel.channel.active}</td>
                <td><a href="/instant?facilityId=${deviceChannel.deviceId}&clientId=${deviceChannel.channel.id()}&ak=${deviceChannel.apkVersionCode}">升级</a></td>
            </tr>
        </c:forEach>
    </table>
</form>
</body>

</html>
