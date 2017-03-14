<%-- 
    Document   : aaa
    Created on : 2016-8-2, 10:26:35
    Author     : tangchao
--%>

<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@page contentType="text/html" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">    
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <!--
        <link rel="stylesheet" type="text/css" href="styles.css">
        -->
        <script type="text/javascript"> 
	    function createCommit(){
                var obj = document.getElementById("apiName");
                var index = obj.selectedIndex; 
                var text = obj.options[index].text; 
                var value = obj.options[index].value; 
                var api = document.getElementById("api").value;
	        document.getElementById("addForm").action="test/add?apiName="+value+"&api="+api;
	        document.getElementById("addForm").submit();
	    }
        </script>
        <title>CC(check change) System</title>
    </head>

    <body>
        <center>
            <form action="test/add" method="post" id="addForm">
                <table border="0">
                    <tr>
                        <td>name</td>
                        <td>
                            <select id="apiName" name="apiName" >
                                <c:forEach var="var" items="${list}">
                                    <option >${var}</option>
                                </c:forEach>
                            </select>
                        </td>
                    </tr>

                    <tr>
                        <td>api</td>
                        <td><input type="text" id="api" name="api" value=""/></td>
                    </tr>
                </table>


                <input type="submit" value="commit" />
                <input type="button" value="return" onclick="javascript:history.go(-1)"/>

            </form>
        </center>
    </body>
</html>
